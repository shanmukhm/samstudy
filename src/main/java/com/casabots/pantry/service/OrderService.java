/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import com.casabots.pantry.model.Order;
import com.casabots.pantry.model.PendingOrder;

import java.util.List;

public interface OrderService {
    /**
     * Checks whether the ordered item is available at Sally. If it is available, order is placed.
     * If it is not available order won't be placed.
     * @param order the order object
     * @return  {@Code Order}
     */
    boolean checkOrder(PendingOrder order);

    PendingOrder placeOrder(PendingOrder order);

    List<Order> getOrders();
}
