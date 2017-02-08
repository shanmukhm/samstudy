/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.samstudy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

//    @Autowired
//    private AdminRepository adminRepository;
//
//    @RequestMapping(method = RequestMethod.POST, value = "/login")
//    public ModelMap login(@RequestBody LoginUser admin, HttpServletResponse response) {
//        SessionUtils.loginUser(admin.getUserId(), admin.getPassword(), Constants.Roles.ADMIN_ROLE, null, response);
//        return new ModelMap("msg", admin.getUserId() + " logged in successfully!!");
//    }
//
//    @RequestMapping(method = RequestMethod.POST, value = "/create")
//    public Admin create(@RequestBody Admin admin) {
//        admin.setHashedPwd(PasswordHashingUtil.hash(admin.getPassword()));
//        return adminRepository.insert(admin);
//    }
}
