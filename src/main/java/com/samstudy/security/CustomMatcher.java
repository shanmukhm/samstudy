/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.samstudy.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class CustomMatcher implements CredentialsMatcher{
    @Override
    public boolean doCredentialsMatch(AuthenticationToken at, AuthenticationInfo ai) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        char[] cred = (char[])at.getCredentials();
        String pw = String.valueOf(cred);
        byte[] dbPw = (byte[]) ai.getCredentials();
        byte[] dbArr = md.digest(pw.getBytes(StandardCharsets.UTF_8));
        return md.isEqual(dbArr, dbPw);
    }
}
