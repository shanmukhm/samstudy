/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.repository;

import com.casabots.pantry.model.Size;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SizeRepository extends MongoRepository<Size, String> {
}
