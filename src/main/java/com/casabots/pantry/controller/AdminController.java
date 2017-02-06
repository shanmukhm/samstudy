/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Admin;
import com.casabots.pantry.repository.AdminRepository;
import com.casabots.pantry.security.LoginUser;
import com.casabots.pantry.util.Constants;
import com.casabots.pantry.util.PasswordHashingUtil;
import com.casabots.pantry.util.SessionUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelMap login(@RequestBody LoginUser admin, HttpServletResponse response) {
        SessionUtils.loginUser(admin.getUserId(), admin.getPassword(), Constants.Roles.ADMIN_ROLE, null, response);
        return new ModelMap("msg", admin.getUserId() + " logged in successfully!!");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public Admin create(@RequestBody Admin admin) {
        admin.setHashedPwd(PasswordHashingUtil.hash(admin.getPassword()));
        return adminRepository.insert(admin);
    }
}
