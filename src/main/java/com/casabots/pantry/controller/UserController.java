/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.User;
import com.casabots.pantry.security.LoginUser;
import com.casabots.pantry.service.FBVerificationService;
import com.casabots.pantry.service.GoogleVerificationService;
import com.casabots.pantry.service.UserService;
import com.casabots.pantry.util.Constants.Roles;
import com.casabots.pantry.util.SessionUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoogleVerificationService googleVerificationService;
    @Autowired
    private FBVerificationService fbVerificationService;

    @RequestMapping(method = RequestMethod.POST, value = "/google-login")
    public ModelMap googleLogin(@RequestParam String idToken, HttpServletResponse response) throws GeneralSecurityException, IOException {
        googleVerificationService.verify(idToken, response);
        return new ModelMap("msg", "You logged in successfully!");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fb-login")
    public ModelMap fbLogin(@RequestParam("code") String code, HttpServletResponse response) {
        fbVerificationService.verify(code, response);
        return new ModelMap("msg", "You logged in successfully!");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelMap login(@RequestBody LoginUser user, HttpServletResponse response) {
        SessionUtils.loginUser(user.getUserId(), user.getPassword(), Roles.USER_ROLE, null, response);
        return new ModelMap("msg", user.getUserId() + " logged in successfully!");
    }

    @RequestMapping(method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        if (isValidUser(user) && !userService.exists(user.getUserId()) && !userService.existsWithEmail(user.getEmail())) {
            return userService.create(user);
        } else {
            throw new RuntimeException();
        }
    }

    @RequiresRoles(value = Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path="/get")
    public User getCurrentUser() {
        return userService.get(SessionUtils.getCurrentUserId());
    }

    @RequiresRoles(value = Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/update-current")
    public User updateCurrentUser(@RequestBody User user) {
        if (user.getUserId().equals(SessionUtils.getCurrentUserId()) && isValidUser(user)) {
            return userService.update(user);
        } else {
            throw new RuntimeException();
        }
    }

    @RequiresRoles(value = Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.DELETE, path = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCurrentUser() {
        userService.remove(SessionUtils.getCurrentUserId());
    }

    @RequiresRoles(value = Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/favorite-salads")
    public List<String> getFavoriteSalads() {
        return userService.get(SessionUtils.getCurrentUserId()).getFavouriteSalads();
    }

//    @RequiresRoles(value = Roles.USER_ROLE)
//    @RequestMapping(path = "/add-favorite-salads", method = RequestMethod.POST)
//    public List<String> addFavoriteSalads(@RequestParam("salads") String[] favoriteSalads) {
//        User user = userService.get(SessionUtils.getCurrentUserId());
//        List<String> currentFavSalads = user.getFavouriteSalads();
//        if (currentFavSalads == null) {
//            currentFavSalads = new SaladList();
//            currentFavSalads.setPreDefinedSalads(Arrays.asList(favoriteSalads));
//        } else {
//            currentFavSalads.getPreDefinedSalads().addAll(Arrays.asList(favoriteSalads));
//        }
//        user.setFavouriteSalads(currentFavSalads);
//        return userService.update(user).getFavouriteSalads();
//    }

//    @RequiresRoles(value = Roles.USER_ROLE)
//    @RequestMapping(path = "/add-favorite-custom-salads", method = RequestMethod.POST)
//    public List<String> addFavoriteCustomSalads(@RequestParam("salads") CustomSalad[] favoriteSalads) {
//        User user = userService.get(SessionUtils.getCurrentUserId());
//        List<String> currentFavSalads = user.getFavouriteSalads();
//        if (currentFavSalads == null) {
//            currentFavSalads = new SaladList();
//            currentFavSalads.setCustomSalads(Arrays.asList(favoriteSalads));
//        } else {
//            currentFavSalads.getCustomSalads().addAll(Arrays.asList(favoriteSalads));
//        }
//        user.setFavouriteSalads(currentFavSalads);
//        return userService.update(user).getFavouriteSalads();
//    }

//    @RequiresRoles(value = Roles.USER_ROLE)
//    @RequestMapping(path = "/remove-favorite-salads", method = RequestMethod.POST)
//    public List<String> removeFavoriteSalads(@RequestParam("salads") String[] favoriteSalads) {
//        User user = userService.get(SessionUtils.getCurrentUserId());
//        List<String> currentFavSalads = user.getFavouriteSalads();
//        currentFavSalads.getPreDefinedSalads().removeAll(Arrays.asList(favoriteSalads));
//        user.setFavouriteSalads(currentFavSalads);
//        return userService.update(user).getFavouriteSalads();
//    }

//    @RequiresRoles(value = Roles.USER_ROLE)
//    @RequestMapping(path = "/remove-favorite-custom-salads", method = RequestMethod.POST)
//    public List<String> removeFavoriteCustomSalads(@RequestParam("salads") CustomSalad[] favoriteSalads) {
//        User user = userService.get(SessionUtils.getCurrentUserId());
//        List<String> currentFavSalads = user.getFavouriteSalads();
//        currentFavSalads.getCustomSalads().removeAll(Arrays.asList(favoriteSalads));
//        user.setFavouriteSalads(currentFavSalads);
//        return userService.update(user).getFavouriteSalads();
//    }

    @RequiresRoles(value = Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/custom-salads")
    public List<String> getCustomSalads() {
        return userService.get(SessionUtils.getCurrentUserId()).getCustomSalads();
    }

//    @RequiresRoles(value = Roles.USER_ROLE)
//    @RequestMapping(path = "/add-custom-salads", method = RequestMethod.POST)
//    public List<String> addCustomSalads(@RequestBody List<String> customSalads) {
//        User user = userService.get(SessionUtils.getCurrentUserId());
//        List<String> currentCustomSalads = user.getCustomSalads();
//        if (currentCustomSalads == null) {
//            currentCustomSalads = customSalads;
//        } else {
//            currentCustomSalads.addAll(customSalads);
//        }
//        user.setCustomSalads(currentCustomSalads);
//        return userService.update(user).getCustomSalads();
//    }

    @RequiresRoles(value = Roles.ADMIN_ROLE)
    @RequestMapping(method = RequestMethod.GET, path="/{userId}")
    public User getUser(@PathVariable String userId) {
        return userService.get(userId);
    }

    @RequiresRoles(value = Roles.ADMIN_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public User updateUser(@RequestBody User user) {
        if (isValidUser(user)) {
            return userService.update(user);
        } else {
            throw new RuntimeException();
        }
    }

    @RequiresRoles(value = Roles.ADMIN_ROLE)
    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable String userId) {
        userService.remove(userId);
    }

    private boolean isValidUser(User user) {
        return user.getFirstName() != null && user.getGender() != null && user.getDob() != null
                && user.getLocation() != null;
    }
}
