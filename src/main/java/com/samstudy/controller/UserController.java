/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.samstudy.controller;

import com.samstudy.model.User;
import com.samstudy.security.LoginUser;
import com.samstudy.service.UserService;
import com.samstudy.util.Constants;
import com.samstudy.util.SessionUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelMap login(@RequestBody LoginUser user, HttpServletResponse response) {
        return SessionUtils.loginUser(user.getUserId(), user.getPassword(), Constants.Roles.USER_ROLE, response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @RequiresRoles(value = Constants.Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path="/get")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @RequiresRoles(value = Constants.Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/update-current")
    public User updateCurrentUser(@RequestBody User user) {
        if (user.getUserId().equals(SessionUtils.getCurrentUserId())) {
            return userService.update(user);
        }
        throw new IllegalAccessError("You have no access!");
    }

    @RequiresRoles(value = Constants.Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.DELETE, path = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCurrentUser() {
        userService.deleteCurrentUser();
    }
}
