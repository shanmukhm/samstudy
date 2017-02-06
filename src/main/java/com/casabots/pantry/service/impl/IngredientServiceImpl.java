/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.Ingredient;
import com.casabots.pantry.repository.IngredientRepository;
import com.casabots.pantry.service.IngredientService;
import com.casabots.pantry.util.PantryUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    
    @Autowired
    private IngredientRepository ingredientRepository;
    
    @Override
    public Ingredient create(Ingredient ingredient) {
        verifyIngredient(ingredient);
        verifyIfIngredientExists(ingredient.getIngredientId());
        verifyIfNameExists(ingredient.getName());
        ingredient = trimStrings(ingredient);

        return ingredientRepository.insert(ingredient);
    }

    @Override
    public Ingredient get(String ingredientId) {
        verifyIfIngredientDoesNotExist(ingredientId);
        return ingredientRepository.findOne(ingredientId);
    }

    @Override
    public Ingredient update(Ingredient ingredient) {
        verifyIngredient(ingredient);
        verifyIfIngredientDoesNotExist(ingredient.getIngredientId());
        ingredient = trimStrings(ingredient);
        
        return ingredientRepository.save(ingredient);
        
    }

    @Override
    public void delete(String ingredientId) {
        verifyIfIngredientDoesNotExist(ingredientId);
        
        ingredientRepository.delete(ingredientId);
    }

    @Override
    public List<Ingredient> getIngredientsByCategory(String category) {
        PantryUtil.verifyIngredientCategory(category);

        return ingredientRepository.findByCategory(category);
    }

    @Override
    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public List<Ingredient> getByIds(List<String> idList) {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (String id :
                idList) {
            ingredientList.add(get(id));
        }

        return ingredientList;
    }

    private void verifyIfIngredientExists(String ingredientId) {
        if (ingredientRepository.exists(ingredientId)) {
            throw new IllegalArgumentException("Ingredient with id : " + ingredientId + " already exists!");
        }
    }

    private void verifyIfNameExists(String name) {
        if (ingredientRepository.findByName(name) != null) {
            throw new IllegalArgumentException("Ingredient with name : " + name + "already exists!");
        }
    }

    private void verifyIfIngredientDoesNotExist(String ingredientId) {
        if (!ingredientRepository.exists(ingredientId)) {
            throw new IllegalArgumentException("Ingredient with id : " + ingredientId + " does not exist!");
        }
    }

    private void verifyIngredient(Ingredient ingredient) {
        if (StringUtils.isBlank(ingredient.getIngredientId())) {
            throw new IllegalArgumentException("Invalid ingredient id!");
        }

        if (StringUtils.isBlank(ingredient.getName())) {
            throw new IllegalArgumentException("Invalid ingredient name!");
        }

        if (StringUtils.isBlank(ingredient.getCategory())) {
            throw new IllegalArgumentException("Invalid ingredient category!");
        }
        PantryUtil.verifyIngredientCategory(ingredient.getCategory());
    }

    private Ingredient trimStrings(Ingredient ingredient) {

        ingredient.setIngredientId(ingredient.getIngredientId().trim());
        ingredient.setName(ingredient.getName().trim());
        String[] ingredientProperties = { ingredient.getIngredientId(), ingredient.getName()};
        PantryUtil.verifyStringsLength(ingredientProperties);

        if (StringUtils.isNotBlank(ingredient.getImageUrl())) {
            ingredient.setImageUrl(ingredient.getImageUrl().trim());
            PantryUtil.verifyImageUrl(ingredient.getImageUrl());
            PantryUtil.verifyStringLength(ingredient.getImageUrl());
        }

        return ingredient;
    }
}
