/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import com.casabots.pantry.model.User;

import javax.servlet.http.HttpServletResponse;

public interface FBVerificationService {

    void verify(String code, HttpServletResponse response);
}
