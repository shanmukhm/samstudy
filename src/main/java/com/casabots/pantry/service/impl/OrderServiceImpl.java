/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.*;
import com.casabots.pantry.model.Order;
import com.casabots.pantry.repository.*;
import com.casabots.pantry.service.OrderService;
import com.casabots.pantry.service.SallyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SallyService sallyService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PendingOrderRepository pendingOrderRepository;
    @Autowired
    private UniqueCodeRepository uniqueCodeRepository;
    @Autowired
    private SaladRepository saladRepository;
    @Autowired
    private PortionRepository portionRepository;

    @Override
    public boolean checkOrder(PendingOrder order) {
        return sallyService.areIngredientsAvailable(order.getSallyId(),
                getIngredientsIds(order.getSaladId()));
    }

    @Override
    public PendingOrder placeOrder(PendingOrder order) {
        UniqueCode code = uniqueCodeRepository.findBySallyId(order.getSallyId());
        if (!(code != null && code.getUuid().equals(order.getUuid()))) {
            throw new RuntimeException("Invalid QR code!");
        } else if (!checkOrder(order)) {
            throw new RuntimeException("Ingredients are not available at this Sally!");
        } else {
            uniqueCodeRepository.delete(code.getId());
            return pendingOrderRepository.insert(order);
        }
    }

//    @Override
//    public Salad saveAsSalad(List<Ingredient> ingredients) {
//        Salad salad = new Salad();
//        return saladRepository.insert(salad);
//    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    private List<String> getIngredientsIds(String saladId) {
        List<String> ingredientIds = new ArrayList<>();
        for (int i = 0; i < getPortions(saladId).size(); i++) {
            String portionId = getPortions(saladId).get(i);
            ingredientIds.add(portionRepository.findOne(portionId).getIngredientId());
        }

        return ingredientIds;
    }

    private List<String> getPortions(String saladId) {
        return saladRepository.findOne(saladId).getPortions();
    }

}
