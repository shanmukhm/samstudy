/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Ingredient;
import com.casabots.pantry.repository.IngredientRepository;
import com.casabots.pantry.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This Controller is to perform CRUD operations on global list of ingredients that are serviced by Sally machines.
 * Information of ingredients can be updated time to time using this controller.
 */
@RestController
@RequestMapping("ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @RequestMapping(method = RequestMethod.POST)
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.create(ingredient);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{ingredientId}")
    public Ingredient getIngredient(@PathVariable String ingredientId) {
        return ingredientService.get(ingredientId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public Ingredient updateIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.update(ingredient);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{ingredientId}")
    public void deleteIngredient(@PathVariable String ingredientId) {
        ingredientService.delete(ingredientId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAll")
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getByCategory")
    public List<Ingredient> getIngredientsByCategory(@RequestParam String category) {
        return ingredientService.getIngredientsByCategory(category);
    }
}
