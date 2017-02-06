/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.repository;

import com.casabots.pantry.model.Sally;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SallyRepository extends MongoRepository<Sally, String> {

    List<Sally> findByOwnerId(String ownerId);

    GeoResults<Sally> findBySallyLocationNear(Point location, Distance distance);

    @Query("{'salads.itemId' : ?0}")
    List<Sally> findBySalad(String saladId);

    @Query("{'ingredients.itemId' : ?0}")
    List<Sally> findByIngredient(String ingredientId);

    @Query("{'sallyLocation':{$near:{$geometry: {type:'Point', coordinates:[?0 , ?1]}, $maxDistance:?2}}}, '                                                                                                     salads.itemId' : ?3}")
    List<Sally> findByLocationAndSalad(double x, double y, double distance, String saladId);
}
