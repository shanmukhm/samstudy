/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import com.casabots.pantry.model.Ingredient;

import java.util.List;

public interface IngredientService {

    Ingredient create(Ingredient ingredient);

    Ingredient get(String ingredientId);

    Ingredient update(Ingredient ingredient);

    void delete(String ingredientId);

    List<Ingredient> getIngredientsByCategory(String category);

    List<Ingredient> getAll();

    List<Ingredient> getByIds(List<String> idList);
}
