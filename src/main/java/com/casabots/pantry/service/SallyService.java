/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import com.casabots.pantry.model.Ingredient;
import com.casabots.pantry.model.Order;
import com.casabots.pantry.model.Salad;
import com.casabots.pantry.model.Sally;
import org.springframework.data.geo.Point;

import java.util.List;

public interface SallyService {

    boolean exists(String sallyId);

    Sally create(Sally sally);

    Sally update(Sally sally);

    Sally get(String sallyId);

    void remove(String sallyId);

    Sally getCompleteSally(String sallyId);

    List<Ingredient> getIngredients(String sallyId);

    List<Salad> getSalads(String sallyId);

    Order getPendingOrder(String sallyId);

    List<Order> getOrders(String sallyId);

    List<Sally> getSallyDevicesByLocation(Point location);

    /**
     * Checks if a salad with the given id is available at Sally with the given id
     *
     * @param sallyId the id of Sally
     * @param saladId the id of the salad
     * @return availability of the salad
     */
    boolean isSaladAvailable(String sallyId, String saladId);

    /**
     * Checks if an ingredient with the given id is available at Sally with the given id
     *
     * @param sallyId      the id of Sally
     * @param ingredientId the id of the ingredient
     * @return availability of the ingredient
     */
    boolean isIngredientAvailable(String sallyId, String ingredientId);

    /**
     * Checks if a list of ingredients with the given ids are available at Sally with the given id
     *
     * @param sallyId      the id of Sally
     * @param ingredientIds the list of ids of the ingredients
     * @return availability of the ingredient
     */
    boolean areIngredientsAvailable(String sallyId, List<String> ingredientIds);

    boolean isOwnedBy(String sallyId, String ownerId);

    List<Sally> getSallyDevicesBySalad(String salad);

    List<Sally> getSallyDevicesByIngredient(String ingredient);

    String generateUniqueCode(String sallyId);

    List<Sally> getAll();

    List<Salad> getPopularSalads(String sallyId);

    List<Ingredient> getPopularIngredients(String sallyId);

    List<Sally> getSallyDevicesByLocationNSalad(Point point, String saladId);
}
