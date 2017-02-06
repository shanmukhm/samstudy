/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleVerificationService {

    void verify(String idTokenString, HttpServletResponse response) throws GeneralSecurityException, IOException;
}
