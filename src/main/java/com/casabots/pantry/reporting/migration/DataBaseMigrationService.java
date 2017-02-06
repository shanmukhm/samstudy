/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.migration;

import com.casabots.pantry.model.GeoLocation;
import com.casabots.pantry.reporting.dao.*;
import com.casabots.pantry.reporting.model.*;
import com.casabots.pantry.repository.IngredientRepository;
import com.casabots.pantry.repository.PortionRepository;
import com.casabots.pantry.repository.SaladRepository;
import com.casabots.pantry.repository.SizeRepository;
import com.casabots.pantry.service.OrderService;
import com.casabots.pantry.service.SallyService;
import com.casabots.pantry.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DataBaseMigrationService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SaladRepository saladRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private PortionRepository portionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SallyService sallyService;
    // Reporting services
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SallyDAO sallyDAO;
    @Autowired
    private SaladDAO saladDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private SizeDAO sizeDAO;
    @Autowired
    private IngredientDAO ingredientDAO;
    @Autowired
    private PortionDAO portionDAO;

    @Scheduled(cron = "0 0 * * * *")
    public void doMigrate() {

        for (com.casabots.pantry.model.Order order :
                orderService.getOrders()) {
            String userName = order.getUserId();
            String saladId = order.getSaladId();
            String sallyId = order.getSallyId();

            com.casabots.pantry.model.User user = userService.get(userName);
            User existingUser = userDAO.getByUserName(order.getUserId());
            User userReport = convertUser(user);
            if (existingUser == null) {
                userDAO.create(userReport);
            } else {
                userReport.setId(existingUser.getId());
                userDAO.update(userReport);
            }

            com.casabots.pantry.model.Salad salad = saladRepository.findOne(saladId);
            Salad saladReport = convertSalad(salad);
            saladReport.setPortions(importPortions(salad.getPortions()));
            Salad existingSalad = saladDAO.getByGuid(order.getSaladId());
            if (existingSalad == null) {
                saladDAO.create(saladReport);
            } else {
                saladReport.setId(existingSalad.getId());
                saladDAO.update(saladReport);
            }

            com.casabots.pantry.model.Sally sally = sallyService.get(sallyId);
            Sally sallyReport = convertSally(sally);
            Sally existingSally = sallyDAO.getByGuid(sallyId);
            if (existingSally == null) {
                sallyDAO.create(sallyReport);
            } else {
                sallyReport.setId(existingSally.getId());
                sallyDAO.update(sallyReport);
            }

            Order orderReport = new Order();
            orderReport.setPlacedTime(new Date(order.getPlacedTime().getTime()));
            orderReport.setUser(userReport);
            orderReport.setSalad(saladReport);
            orderReport.setSally(sallyReport);
            orderReport.setCustomSalad(salad.isCustomSalad());

            orderDAO.create(orderReport);
        }

        Logger logger = LoggerFactory.getLogger(DataBaseMigrationService.class);
        logger.debug("Done!!");

        // Code for clearing orders and custom salads from main db
    }

    private List<Portion> importPortions(List<String> portionIds) {

        List<Portion> portionReportsList = new ArrayList<>();
        for (String portionId :
                portionIds) {
            com.casabots.pantry.model.Portion portion = portionRepository.findOne(portionId);
            // Importing size
            com.casabots.pantry.model.Size size = sizeRepository.findOne(portion.getSizeId());
            Size sizeReport = convertSize(size);
            Size existingSize = sizeDAO.getByGuid(size.getId());
            if (existingSize == null) {
                sizeDAO.create(sizeReport);
            } else {
                sizeReport.setId(existingSize.getId());
                sizeDAO.update(sizeReport);
            }
            // Importing ingredient
            com.casabots.pantry.model.Ingredient ingredient = ingredientRepository.findOne(portion.getIngredientId());
            Ingredient ingredientReport = convertIngredient(ingredient);
            Ingredient existingIngredient = ingredientDAO.getByGuid(ingredient.getIngredientId());
            if (existingIngredient == null) {
                ingredientDAO.create(ingredientReport);
            } else {
                ingredientReport.setId(existingIngredient.getId());
                ingredientDAO.update(ingredientReport);
            }

            Portion portionReport = convertPortion(portion);
            portionReport.setSize(sizeReport);
            portionReport.setIngredient(ingredientReport);
            Portion existingPortion = portionDAO.getByGuid(portion.getId());
            if (portionDAO.getByGuid(portion.getId()) == null) {
                portionDAO.create(portionReport);
            } else {
                portionReport.setId(existingPortion.getId());
                portionDAO.update(portionReport);
            }

            portionReportsList.add(portionReport);
        }

        return portionReportsList;
    }

    private Salad convertSalad(com.casabots.pantry.model.Salad salad) {
        Salad saladReport = new Salad();
        saladReport.setName(salad.getName());
        saladReport.setGuid(salad.getSaladId());

        return saladReport;
    }

    private Portion convertPortion(com.casabots.pantry.model.Portion portion) {
        Portion portionReport = new Portion();
        portionReport.setCalories(portion.getCalories());
        portionReport.setGuid(portion.getId());

        return portionReport;
    }

    private Size convertSize(com.casabots.pantry.model.Size size) {
        Size sizeReport = new Size();
        sizeReport.setGuid(size.getId());
        sizeReport.setName(size.getSizeName());

        return sizeReport;
    }

    private Ingredient convertIngredient(com.casabots.pantry.model.Ingredient ingredient) {
        Ingredient ingredientReport = new Ingredient();
        ingredientReport.setGuid(ingredient.getIngredientId());
        ingredientReport.setName(ingredient.getName());
        ingredientReport.setCategory(ingredient.getCategory());

        return ingredientReport;
    }

    private User convertUser(com.casabots.pantry.model.User user) {
        User userReport = new User();
        userReport.setFirstName(user.getFirstName());
        userReport.setGender(user.getGender());
        userReport.setDob(new Date(user.getDob().getTime()));
        userReport.setLocation(convertLocation(user.getLocation()));
        userReport.setUserName(user.getUserId());

        return userReport;
    }

    private Sally convertSally(com.casabots.pantry.model.Sally sally) {
        Sally sallyReport = new Sally();
        sallyReport.setName(sally.getName());
        sallyReport.setGuid(sally.getSallyId());
        sallyReport.setLocation(convertLocation(sally.getLocation()));

        return sallyReport;
    }

    private Location convertLocation(GeoLocation location) {
        if (location == null) {
            return null;
        }
        Location locationReport = new Location(location.getName(), location.getLatitude(), location.getLongitude());

        return locationReport;
    }
}
