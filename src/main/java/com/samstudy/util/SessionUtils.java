/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.samstudy.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletResponse;

public class SessionUtils {

    private SessionUtils() {}

    public static String getUserRole() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session.getAttribute("role") == null ? null : (String) session.getAttribute("role");
    }

    public static Session getCurrentSession() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.getSession();
    }

    public static ModelMap loginUser(String userId, String password, String role, HttpServletResponse response) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        if ( !currentUser.isAuthenticated() ) {
            UsernamePasswordToken token = new UsernamePasswordToken(userId, password);
            token.setRememberMe(true);
            session.setAttribute("role", role);
            try {
                currentUser.login(token);
                ModelMap mp = new ModelMap();
                mp.addAttribute("msg", getCurrentUserId() + " logged in successfully!");
                Object[] cookies = response.getHeaders("Set-Cookie").toArray();
                String rememberMe = "";
                for (Object cookie :
                        cookies) {
                    if ("rememberMe".equals(((String) cookie).split(";")[0].split("=")[0])) {
                        rememberMe = ((String) cookie).split(";")[0].split("=")[1];
                    }
                }
                mp.addAttribute("JSESSIONID", getCurrentSession().getId());
                mp.addAttribute("rememberMe", rememberMe);

                return mp;
            } catch (IncorrectCredentialsException ex) {
                ex.printStackTrace();
                throw new IncorrectCredentialsException("Check your credentials!");
            }
        } else {
            throw new IllegalStateException("Another user logged in already! Logout and try again!");
        }
    }

    public static String getCurrentUserId() {
        Subject currentUser = SecurityUtils.getSubject();
        return (String) currentUser.getPrincipal();
    }

    public static void logoutCurrentUser() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }
}
