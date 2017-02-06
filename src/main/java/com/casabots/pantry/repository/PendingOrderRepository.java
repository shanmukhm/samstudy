/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.repository;


import com.casabots.pantry.model.PendingOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PendingOrderRepository extends MongoRepository<PendingOrder, String>{

    List<PendingOrder> findBySallyId(String sallyId, Sort sort);

    PendingOrder findByUserId(String userId);
}
