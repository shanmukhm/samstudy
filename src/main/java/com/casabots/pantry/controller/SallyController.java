/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Ingredient;
import com.casabots.pantry.model.Order;
import com.casabots.pantry.model.Salad;
import com.casabots.pantry.model.Sally;
import com.casabots.pantry.security.LoginUser;
import com.casabots.pantry.service.SallyService;
import com.casabots.pantry.util.Constants;
import com.casabots.pantry.util.SessionUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.casabots.pantry.util.Constants.Roles.SALLY_ROLE;

@RestController
@RequestMapping("sally")
public class SallyController {

    @Autowired
    private SallyService sallyService;

    @RequiresRoles(Constants.Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.POST)
    public Sally createSally(@RequestBody Sally sally) {
        return sallyService.create(sally);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public void login(@RequestBody LoginUser sally, HttpServletResponse response) {
        SessionUtils.loginUser(sally.getUserId(), sally.getPassword(), SALLY_ROLE, null, response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{sallyId}")
    public Sally getSally(@PathVariable String sallyId) {
        return sallyService.get(sallyId);
    }

    @RequiresRoles({Constants.Roles.OWNER_ROLE, Constants.Roles.SALLY_ROLE})
    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public Sally updateSally(@RequestBody Sally sally) {
        return sallyService.update(sally);
    }

    @RequiresRoles({Constants.Roles.OWNER_ROLE, Constants.Roles.SALLY_ROLE})
    @RequestMapping(method = RequestMethod.DELETE, path = "/{sallyId}")
    public void deleteSally(@PathVariable String sallyId) {
        sallyService.remove(sallyId);
    }

    // search for sally based on location
    @RequestMapping(method = RequestMethod.GET, path = "/getByLocation")
    public List<Sally> getSallyDevicesByLocation(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
        return sallyService.getSallyDevicesByLocation(new Point(latitude, longitude));
    }

    // search for sally based on salad
    @RequestMapping(method = RequestMethod.GET, path = "/getBySalad")
    public List<Sally> getSallyDevicesBySalad(@RequestParam String salad) {
        return sallyService.getSallyDevicesBySalad(salad);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getBySaladAndLocation")
    public List<Sally> getSallyDevicesByLocationNSalad(@RequestParam(value = "latitude") Double latitude,
                                                       @RequestParam("longitude") Double longitude,
                                                       @RequestParam String saladId) {
        return sallyService.getSallyDevicesByLocationNSalad(new Point(latitude, longitude), saladId);
    }

    // search for sally based on salad
    @RequestMapping(method = RequestMethod.GET, path = "/by-ingredient")
    public List<Sally> getSallyDevicesByIngredient(@RequestParam String ingredient) {
        return sallyService.getSallyDevicesByIngredient(ingredient);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{sallyId}/salads")
    public List<Salad> getSalads(@PathVariable String sallyId) {
        return sallyService.getSalads(sallyId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{sallyId}/ingredients")
    public List<Ingredient> getIngredients(@PathVariable String sallyId) {
        return sallyService.getIngredients(sallyId);
    }

    // Gets QR code for an order placed on a Sally
    @RequiresRoles(SALLY_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/generate-code")
    public String generateUniqueCode() {
        return sallyService.generateUniqueCode(SessionUtils.getCurrentUserId());
    }

    // returns pending order
    @RequiresRoles(SALLY_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/pending-order")
    public Order getPendingOrder(HttpServletResponse response) throws IOException {
        if (sallyService.getPendingOrder(SessionUtils.getCurrentUserId()) == null) {
            response.sendError(500, "No pending orders for the machine!");
        } else {
            return sallyService.getPendingOrder(SessionUtils.getCurrentUserId());
        }

        return null;
    }

    // Get popular salads at a Sally
    @RequiresRoles(value = Constants.Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/{sallyId}/getPopularSalads")
    public List<Salad> getPopularSalads(@PathVariable String sallyId) {
        return sallyService.getPopularSalads(sallyId);
    }

    // Get popular ingredients at a Sally
    @RequiresRoles(value = Constants.Roles.OWNER_ROLE)
    @RequestMapping(method = RequestMethod.GET, path = "/{sallyId}/getPopularIngredients")
    public List<Ingredient> getPopularIngredients(@PathVariable String sallyId) {
        return sallyService.getPopularIngredients(sallyId);
    }
}
