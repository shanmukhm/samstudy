/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;

import com.casabots.pantry.reporting.model.Ingredient;

import java.util.List;

public interface IngredientDAO {

    void create(Ingredient ingredient);

    Ingredient get(long id);

    void update(Ingredient ingredient);

    void delete(long id);

    List<Ingredient> list();

    Ingredient getByGuid(String guid);
}
