/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.User;
import com.casabots.pantry.service.GoogleVerificationService;
import com.casabots.pantry.service.UserService;
import com.casabots.pantry.util.Constants;
import com.casabots.pantry.util.Constants.Roles;
import com.casabots.pantry.util.SessionUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Properties;

import static com.casabots.pantry.util.Constants.PropertyKeys.CLIENT_ID;

@Service
public class GoogleVerificationServiceImpl implements GoogleVerificationService {

    @Autowired
    private UserService userService;
    // This client id is for testing
    private String clientId;
    @Resource(name = "configProperties")
    private Properties loginProperties;

    @PostConstruct
    public void init() {
        clientId = loginProperties.getProperty(CLIENT_ID);
    }

    @Override
    public void verify(String idTokenString, HttpServletResponse response) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                JacksonFactory.getDefaultInstance()).setAudience(Collections.singletonList(clientId)).build();

        GoogleIdToken idToken = null;

        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String userId = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String lastName = (String) payload.get("family_name");
            String firstName = (String) payload.get("given_name");

            if (userService.exists(userId) || userService.existsWithEmail(userId)) {
                // Logging in the user as Google verified user
                SessionUtils.loginUser(userId, "password",
                        Roles.USER_ROLE, Constants.UserLoginType.GOOGLEUSER, response);
            } else {
                User user = new User();
                user.setUserId(userId);
                user.setEmail(userId);
                user.setFirstName(firstName);
                user.setLastName(lastName);

                userService.create(user);

                // Logging in the user as Google verified user
                SessionUtils.loginUser(userId, "password",
                        Roles.USER_ROLE, Constants.UserLoginType.GOOGLEUSER, response);
            }
        } else {
            throw new RuntimeException("Token is not verified");
        }
    }
}
