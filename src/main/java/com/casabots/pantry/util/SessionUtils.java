/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.util;

import com.casabots.pantry.security.PantryAuthenticationToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    public static void loginUser(String userId, String password, String role, String loginType, HttpServletResponse response) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        if ( !currentUser.isAuthenticated() ) {
            PantryAuthenticationToken token = new PantryAuthenticationToken(userId, password);
            token.setRememberMe(true);
            token.setLoginType(loginType);
            session.setAttribute("role", role);
            try {
                currentUser.login(token);
            } catch (IncorrectCredentialsException ex) {
                ex.printStackTrace();
                response.setStatus(500);
            }
        } else {
            new RuntimeException("Another user logged in already! Logout and try again!");
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
