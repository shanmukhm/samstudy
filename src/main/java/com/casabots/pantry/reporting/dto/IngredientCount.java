/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dto;

import com.casabots.pantry.reporting.model.Ingredient;

public class IngredientCount {

    private Long ingredientId;
    private String ingredient;
    private Integer count;

    public IngredientCount() {
    }

    public IngredientCount(Long ingredientId, String ingredient, Integer count) {
        this.ingredientId = ingredientId;
        this.ingredient = ingredient;
        this.count = count;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
