/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.User;
import com.casabots.pantry.service.FBVerificationService;
import com.casabots.pantry.service.UserService;
import com.casabots.pantry.util.Constants;
import com.casabots.pantry.util.SessionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import static com.casabots.pantry.util.Constants.PropertyKeys.APP_ID;
import static com.casabots.pantry.util.Constants.PropertyKeys.APP_SECRET;
import static com.casabots.pantry.util.Constants.PropertyKeys.FB_REDIRECT_URI;

@Service
public class FBVerificationServiceImpl implements FBVerificationService {

    private String appId;
    private String appSecret;
    private String redirectUri;
    @Resource(name = "configProperties")
    private Properties loginProperties;
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        appId = loginProperties.getProperty(APP_ID);
        appSecret = loginProperties.getProperty(APP_SECRET);
        redirectUri = loginProperties.getProperty(FB_REDIRECT_URI);
    }

    @Override
    public void verify(String code, HttpServletResponse response) {
        if (code == null || code.equals("")) {
            throw new RuntimeException();
        }

        String token = null;

        try {
            String url = "https://graph.facebook.com/v2.8/oauth/access_token?"
                    + "client_id=" + appId
                    + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                    + "&client_secret=" + appSecret
                    + "&code=" + code;
            URL fbUrl = new URL(url);
            URLConnection fbConnction = fbUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(fbConnction.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = reader.readLine()) != null)
                b.append(inputLine + "\n");
            reader.close();
            JSONObject tokenResponse = new JSONObject(b.toString());
            if (tokenResponse.has("access_token")) {
                token = tokenResponse.getString("access_token");
            }
            if (token.startsWith("{"))
                throw new Exception("error on requesting token: " + token + " with code: " + code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            response.setStatus(500);
            e.printStackTrace();
        } catch (IOException e) {
            response.setStatus(500);
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace();
        }

        String graph = null;
        try {
            String g = "https://graph.facebook.com/me?access_token=" + token + "&fields=id,first_name,last_name,gender,age_range";
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine + "\n");
            in.close();
            graph = b.toString();
        } catch (Exception e) {
            response.setStatus(500);
        }

        String facebookId;
        String name;
        String firstName;
        String middleNames;
        String lastName;
        String email;
        Character gender=null;
        try {
            JSONObject json = new JSONObject(graph);
            facebookId = json.getString("id");
            firstName = json.getString("first_name");
            lastName = json.getString("last_name");
            String genderAsString = json.getString("gender");
            if ("male".equals(genderAsString)) {
                gender = 'M';
            } else if ("female".equals(genderAsString)) {
                gender = 'F';
            } else {
                gender = 'O';
            }

            if (userService.exists(facebookId)) {
                SessionUtils.loginUser(facebookId, "password",
                        Constants.Roles.USER_ROLE, Constants.UserLoginType.FBUSER, response);
            } else {
                User user = new User();
                user.setUserId(facebookId);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setGender(gender);

                userService.create(user);
                SessionUtils.loginUser(facebookId, "password",
                        Constants.Roles.USER_ROLE, Constants.UserLoginType.FBUSER, response);
            }
        } catch (JSONException e) {
            response.setStatus(500);
        }

    }
}
