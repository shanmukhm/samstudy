/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class PantryAuthenticationToken extends UsernamePasswordToken{

    private String loginType;

    public PantryAuthenticationToken() {
        super();
    }

    public PantryAuthenticationToken(String username, String password) {
        super(username, password);
    }

    public PantryAuthenticationToken(String username, String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
