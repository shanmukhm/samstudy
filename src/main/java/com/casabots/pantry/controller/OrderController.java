/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.PendingOrder;
import com.casabots.pantry.repository.PendingOrderRepository;
import com.casabots.pantry.repository.SaladRepository;
import com.casabots.pantry.repository.SallyRepository;
import com.casabots.pantry.service.OrderService;
import com.casabots.pantry.util.Constants.Roles;
import com.casabots.pantry.util.SessionUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SallyRepository sallyRepository;
    @Autowired
    private SaladRepository saladRepository;
    @Autowired
    private PendingOrderRepository pendingOrderRepository;

    @RequiresRoles(Roles.USER_ROLE)
    @RequestMapping(method = RequestMethod.POST, path = "/place-order")
    public PendingOrder placeOrder(@RequestBody PendingOrder pendingOrder) {
        pendingOrder.setPlacedTime(new Date());
        pendingOrder.setUserId(SessionUtils.getCurrentUserId());
        if (!checkOrder(pendingOrder)) {
            throw new RuntimeException("Incorrect order details!!");
        } else if (pendingOrderRepository.findByUserId(SessionUtils.getCurrentUserId()) != null) {
            throw new RuntimeException("There's already a pending order! Please wait until it's fulfilled!!");
        } else {
            return orderService.placeOrder(pendingOrder);
        }
    }

    private boolean checkOrder(PendingOrder order) {
        return sallyRepository.exists(order.getSallyId()) && saladRepository.exists(order.getSaladId());
    }
}
