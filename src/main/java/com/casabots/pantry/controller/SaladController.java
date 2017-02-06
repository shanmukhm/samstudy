/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Salad;
import com.casabots.pantry.service.SaladService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.casabots.pantry.util.Constants.Roles.USER_ROLE;

/**
 * This Controller is to perform CRUD operations on global list of salads that are serviced by Sally machines.
 * Information of salads can be updated time to time using this controller.
 */
@RestController
@RequestMapping("salads")
public class SaladController {


    @Autowired
    private SaladService saladService;

    @RequestMapping(method = RequestMethod.POST)
    public Salad createSalad(@RequestBody Salad salad) {
        return saladService.create(salad);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{saladId}")
    public Salad getSalad(@PathVariable String saladId) {
        return saladService.get(saladId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public Salad updateSalad(@RequestBody Salad salad) {
        return saladService.update(salad);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{saladId}")
    public void deleteSalad(@PathVariable String saladId) {
        saladService.delete(saladId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAll")
    public List<Salad> getAllSalads() {
        return saladService.getByUser("admin");
    }

    @RequiresRoles(USER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/custom")
    public Salad createCustomSalad(@RequestBody Salad salad) {
        return saladService.createCustomSalad(salad);
    }

}
