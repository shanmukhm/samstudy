/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.ItemStatus;
import com.casabots.pantry.model.Owner;
import com.casabots.pantry.model.Sally;
import com.casabots.pantry.security.LoginUser;
import com.casabots.pantry.service.OwnerService;
import com.casabots.pantry.util.Constants.Roles;
import com.casabots.pantry.util.SessionUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("owners")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelMap login(@RequestBody LoginUser owner, HttpServletResponse response) {
        SessionUtils.loginUser(owner.getUserId(), owner.getPassword(), Roles.OWNER_ROLE, null, response);
        return new ModelMap("msg", owner.getUserId() + " logged in successfully!");
    }

    @RequestMapping(method = RequestMethod.POST)
    public Owner createOwner(@RequestBody Owner owner) {
        return ownerService.create(owner);
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/get")
    public Owner getCurrentOwner() {
        return ownerService.get(SessionUtils.getCurrentUserId());
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/update-current")
    public Owner updateCurrentOwner(@RequestBody Owner owner) {
        if (owner.getOwnerId().equals(SessionUtils.getCurrentUserId())) {
            return ownerService.update(owner);
        } else {
            throw new RuntimeException();
        }
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.DELETE, path = "/delete")
    public void deleteCurrentOwner() {
        ownerService.remove(SessionUtils.getCurrentUserId());
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/sally-devices")
    public List<Sally> getCurrentSallyDevices() {
        return ownerService.getSallyDevices(SessionUtils.getCurrentUserId());
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/add-salads/{sallyId}")
    public List<ItemStatus> addSaladsToSally(@PathVariable String sallyId, @RequestParam("salads") String[] saladIds) {
        return ownerService.addSaladsToSally(sallyId, saladIds);
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/add-ingredients/{sallyId}")
    public List<ItemStatus> addIngredientsToSally(@PathVariable String sallyId, @RequestParam("ingredients") String[] ingredientIds) {
        return ownerService.addIngredientsToSally(sallyId, ingredientIds);
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/remove-salads/{sallyId}")
    public List<ItemStatus> removeSaladsFromSally(@PathVariable String sallyId, @RequestParam("salads") String[] saladIds) {
        return ownerService.removeSaladsFromSally(sallyId, saladIds);
    }

    @RequiresRoles(value = Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/remove-ingredients/{sallyId}")
    public List<ItemStatus> removeIngredientsFromSally(@PathVariable String sallyId, @RequestParam("ingredients") String[] ingredientIds) {
        return ownerService.removeIngredientsFromSally(sallyId, ingredientIds);
    }

    @RequiresRoles(value = Roles.ADMIN_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/{ownerId}")
    public Owner getOwner(@PathVariable String ownerId) {
        return ownerService.get(ownerId);
    }

    @RequiresRoles(value = Roles.ADMIN_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public Owner updateOwner(@RequestBody Owner owner) {
        return ownerService.update(owner);
    }

    @RequiresRoles(value = Roles.ADMIN_ROLE)
    @RequestMapping(method = RequestMethod.DELETE, path = "/{ownerId}")
    public void deleteOwner(@PathVariable String ownerId) {
        ownerService.remove(ownerId);
    }

    @RequiresRoles(value = Roles.ADMIN_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/{ownerId}/sally")
    public List<Sally> getSallyDevices(@PathVariable String ownerId) {
        return ownerService.getSallyDevices(ownerId);
    }

    private boolean isValidOwner(Owner owner) {
        return owner.getFirstName() != null && owner.getGender() != null;
    }
}
