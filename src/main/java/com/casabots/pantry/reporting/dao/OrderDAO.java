/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;


import com.casabots.pantry.reporting.dto.IngredientCount;
import com.casabots.pantry.reporting.dto.SaladCount;
import com.casabots.pantry.reporting.model.Location;
import com.casabots.pantry.reporting.model.Order;

import java.util.List;

public interface OrderDAO {

    void create(Order order);

    Order get(long id);

    void update(Order order);

    void delete(long id);

    List<Order> list();

    List<SaladCount> getPopularSaladsByGender(Character gender, int limit);

    List<SaladCount> getPopularSaladsByLocation(Location location, int limit);

    List<SaladCount> getPopularSaladsByTime(String startTime, String endTime, int limit);

    List<SaladCount> getPopularSaladsByAge(Integer startAge, Integer endAge, int limit);

    List<IngredientCount> getPopularIngredientsByGender(Character gender, int limit);

    List<IngredientCount> getPopularIngredientsByAge(Integer startAge, Integer endAge, int limit);

    List<IngredientCount> getPopularIngredientsByTime(String startTime, String endTime, int limit);

    List<IngredientCount> getPopularIngredientsByLocation(Location location, int limit);
}
