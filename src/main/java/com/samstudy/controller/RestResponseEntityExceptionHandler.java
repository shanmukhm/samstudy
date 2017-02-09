/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.samstudy.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ModelAndView handleConflict(RuntimeException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("exception");
        mav.addObject("error", ex.getClass().getSimpleName());
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", 500);
        return mav;
    }

}
