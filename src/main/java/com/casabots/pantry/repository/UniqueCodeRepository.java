/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.repository;

import com.casabots.pantry.model.UniqueCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UniqueCodeRepository extends MongoRepository<UniqueCode, String> {

    UniqueCode findBySallyId(String sallyId);
}
