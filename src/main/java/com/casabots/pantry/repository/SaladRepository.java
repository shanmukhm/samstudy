/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.repository;

import com.casabots.pantry.model.Salad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SaladRepository extends MongoRepository<Salad, String> {

    Salad findByName(String salad);

    List<Salad> findByCreatedBy(String createdBy);

    @Query("{'name': ?0, 'createdBy': ?1}")
    List<Salad> findByNameAndCreatedBy(String name, String userName);
}
