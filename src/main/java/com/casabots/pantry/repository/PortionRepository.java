/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.repository;

import com.casabots.pantry.model.Portion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortionRepository extends MongoRepository<Portion, String> {
}
