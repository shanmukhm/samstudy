/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.controller;

import com.casabots.pantry.reporting.dao.OrderDAO;
import com.casabots.pantry.reporting.dto.IngredientCount;
import com.casabots.pantry.reporting.dto.SaladCount;
import com.casabots.pantry.reporting.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("reports")
public class ReportsController {

    @Autowired
    private OrderDAO orderDAO;

    @RequestMapping(path = "/popularSaladsByGender", method = RequestMethod.GET)
    public List<SaladCount> getPopularSaladsByGender(@RequestParam char gender, @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularSaladsByGender(gender, limit);
    }

    @RequestMapping(path = "/popularSaladsByAge", method = RequestMethod.GET)
    public List<SaladCount> getPopularSaladsByAge(@RequestParam Integer startAge, @RequestParam Integer endAge,
                                                  @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularSaladsByAge(startAge, endAge, limit);
    }

    @RequestMapping(path = "/popularSaladsByLocation", method = RequestMethod.GET)
    public List<SaladCount> getPopularSaladsByLocation(@RequestParam double latitude, @RequestParam double longitude,
                                                       @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularSaladsByLocation(new Location(latitude, longitude), limit);
    }

    @RequestMapping(path = "/popularSaladsByTime", method = RequestMethod.GET)
    public List<SaladCount> getPopularSaladsByTime(@RequestParam String startTime, @RequestParam String endTime,
                                                   @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularSaladsByTime(startTime, endTime, limit);
    }

    @RequestMapping(path = "/popularIngredientsByGender", method = RequestMethod.GET)
    public List<IngredientCount> getPopularIngredientsByGender(@RequestParam char gender, @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularIngredientsByGender(gender, limit);
    }

    @RequestMapping(path = "/popularIngredientsByAge", method = RequestMethod.GET)
    public List<IngredientCount> getPopularIngredientsByAge(@RequestParam Integer startAge, @RequestParam Integer endAge,
                                                  @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularIngredientsByAge(startAge, endAge, limit);
    }

    @RequestMapping(path = "/popularIngredientsByLocation", method = RequestMethod.GET)
    public List<IngredientCount> getPopularIngredientsByLocation(@RequestParam double latitude, @RequestParam double longitude,
                                                       @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularIngredientsByLocation(new Location(latitude, longitude), limit);
    }

    @RequestMapping(path = "/popularIngredientsByTime", method = RequestMethod.GET)
    public List<IngredientCount> getPopularIngredientsByTime(@RequestParam String startTime, @RequestParam String endTime,
                                                   @RequestParam(defaultValue = "10") int limit) {
        return orderDAO.getPopularIngredientsByTime(startTime, endTime, limit);
    }
}
