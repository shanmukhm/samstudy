/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.*;
import com.casabots.pantry.repository.*;
import com.casabots.pantry.service.IngredientService;
import com.casabots.pantry.service.OrderService;
import com.casabots.pantry.service.SaladService;
import com.casabots.pantry.service.SallyService;
import com.casabots.pantry.util.PasswordHashingUtil;
import com.casabots.pantry.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SallyServiceImpl implements SallyService {

    private static final Double MAX_DISTANCE = 3.0;

    @Autowired
    private SallyRepository sallyRepository;
    @Autowired
    private SaladRepository saladRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PendingOrderRepository pendingOrderRepository;
    @Autowired
    private UniqueCodeRepository uniqueCodeRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private SaladService saladService;

    @Override
    public boolean exists(String sallyId) {
        return sallyRepository.exists(sallyId);
    }

    @Override
    public Sally create(Sally sally) {
        return sallyRepository.insert(prepareSally(sally));
    }

    @Override
    public Sally update(Sally sally) {
        if (sallyRepository.exists(sally.getSallyId())) {
            return sallyRepository.save(prepareSally(sally));
        }
        return null;
    }

    @Override
    public Sally get(String sallyId) {
        Sally sally = sallyRepository.findOne(sallyId);
        sally.setHashedPwd(null);
        return sally;
    }

    @Override
    public void remove(String sallyId) {
        sallyRepository.delete(sallyId);
    }

    @Override
    public Sally getCompleteSally(String sallyId) {
        return sallyRepository.findOne(sallyId);
    }

    @Override
    public List<Ingredient> getIngredients(String sallyId) {
        verifyIfSallyDoesNotExist(sallyId);
            List<Ingredient> ingredients = new ArrayList<>();
            List<ItemStatus> ingredientItems = sallyRepository.findOne(sallyId).getIngredients();
            for (ItemStatus item :
                    ingredientItems) {
                ingredients.add(ingredientService.get(item.getItemId()));
            }
            return ingredients;

    }

    @Override
    public List<Salad> getSalads(String sallyId) {
        verifyIfSallyDoesNotExist(sallyId);
        List<Salad> salads = new ArrayList<>();
            List<ItemStatus> saladItems = sallyRepository.findOne(sallyId).getSalads();
        for (ItemStatus item:
             saladItems) {
            salads.add(saladService.get(item.getItemId()));
        }
            return salads;
    }

    @Override
    public Order getPendingOrder(String sallyId) {
        List<PendingOrder> pendingOrders = pendingOrderRepository.findBySallyId(sallyId, new Sort(Sort.Direction.ASC,
                "placedTime"));
        for (PendingOrder pendingOrder :
                pendingOrders) {
            orderRepository.insert(pendingOrderToOrder(pendingOrder));
            return pendingOrderToOrder(pendingOrder);
        }

        return null;
    }

    @Override
    public List<Order> getOrders(String sallyId) {
        if (sallyRepository.exists(sallyId)) {
            return orderRepository.findBySallyId(sallyId);
        }
        return null;
    }

    @Override
    public List<Sally> getSallyDevicesByLocation(Point location) {
        // returns Sally devices within 5 kms of the customer's location
        GeoResults<Sally> sallyGeoResults = sallyRepository.findBySallyLocationNear(location,
                new Distance(MAX_DISTANCE, Metrics.KILOMETERS));
        List<Sally> sallyList = new ArrayList<>();
        for (GeoResult<Sally> sally :
                sallyGeoResults.getContent()) {
            sallyList.add(sally.getContent());
        }

        return sallyList;
    }

    @Override
    public boolean isSaladAvailable(String sallyId, String saladId) {
        if (sallyRepository.exists(sallyId)) {
            List<ItemStatus> availableSalads = sallyRepository.findOne(sallyId).getSalads();
            if (availableSalads != null) {
                return availableSalads.contains(saladId);
            }
        }

        return false;
    }

    @Override
    public boolean isIngredientAvailable(String sallyId, String ingredientId) {
        if (sallyRepository.exists(sallyId)) {
            List<ItemStatus> availableIngredients = sallyRepository.findOne(sallyId).getIngredients();
            if (availableIngredients != null) {
                return availableIngredients.contains(ingredientId);
            }
        }

        return false;
    }

    @Override
    public boolean areIngredientsAvailable(String sallyId, List<String> ingredientIds) {
        if (sallyRepository.exists(sallyId)) {
            List<ItemStatus> availableIngredients = sallyRepository.findOne(sallyId).getIngredients();
            if (availableIngredients != null && ingredientIds != null) {
                return availableIngredients.containsAll(ingredientIds);
            }
        }

        return false;
    }

    @Override
    public boolean isOwnedBy(String sallyId, String ownerId) {
        Sally sally = sallyRepository.findOne(sallyId);
        return sally.getOwnerId().equals(ownerId);
    }

    @Override
    public List<Sally> getSallyDevicesBySalad(String saladId) {
        return sallyRepository.findBySalad(saladId);
    }

    @Override
    public List<Sally> getSallyDevicesByIngredient(String ingredientId) {
        return sallyRepository.findByIngredient(ingredientId);
    }

    @Override
    public String generateUniqueCode(String sallyId) {
        UniqueCode existingCode = uniqueCodeRepository.findBySallyId(sallyId);
        if (existingCode != null) {
            uniqueCodeRepository.delete(existingCode);
        }

        UniqueCode code = new UniqueCode(sallyId, UUID.randomUUID().toString());
        uniqueCodeRepository.insert(code);
        return code.getUuid();
    }

    @Override
    public List<Sally> getAll() {
        return sallyRepository.findAll();
    }

    @Override
    public List<Salad> getPopularSalads(String sallyId) {
        verifyIfSallyDoesNotExist(sallyId);
        verifyIfUserOwnsSally(sallyId);
        List<ItemStatus> items = get(sallyId).getSalads();

        Collections.sort(items, new Comparator<ItemStatus>() {
            @Override
            public int compare(ItemStatus o1, ItemStatus o2) {
                if(o1.getItemsDispensed()==o2.getItemsDispensed())
                    return 0;
                else if(o1.getItemsDispensed()>o2.getItemsDispensed())
                    return -1;
                else
                    return 1;
            }
        });

        return getSaladsFromItems(items);
    }

    @Override
    public List<Ingredient> getPopularIngredients(String sallyId) {
        verifyIfSallyDoesNotExist(sallyId);
        verifyIfUserOwnsSally(sallyId);
        List<ItemStatus> items = get(sallyId).getIngredients();

        Collections.sort(items, new Comparator<ItemStatus>() {
            @Override
            public int compare(ItemStatus o1, ItemStatus o2) {
                if(o1.getItemsDispensed()==o2.getItemsDispensed())
                    return 0;
                else if(o1.getItemsDispensed()>o2.getItemsDispensed())
                    return -1;
                else
                    return 1;
            }
        });

        return getIngredientsFromItems(items);
    }

    @Override
    public List<Sally> getSallyDevicesByLocationNSalad(Point point, String saladId) {
        return sallyRepository.findByLocationAndSalad(point.getX(), point.getY(), MAX_DISTANCE*1000, saladId);
    }

    private List<Ingredient> getIngredientsFromItems(List<ItemStatus> items) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (ItemStatus item :
                items) {
            ingredients.add(ingredientService.get(item.getItemId()));
        }

        return ingredients;
    }

    private List<Salad> getSaladsFromItems(List<ItemStatus> items) {
        List<Salad> salads = new ArrayList<>();
        for (ItemStatus item :
                items) {
            salads.add(saladService.get(item.getItemId()));
        }

        return salads;
    }

    private void verifyIfUserOwnsSally(String sallyId) {
        if (!isOwnedBy(sallyId, SessionUtils.getCurrentUserId())) {
            throw new IllegalArgumentException("User with id : " + SessionUtils.getCurrentUserId() + " does not own Sally " +
                    "with id : " + sallyId);
        }
    }

    private void verifyIfSallyExists(String sallyId) {
        if (sallyRepository.exists(sallyId)) {
            throw new IllegalArgumentException("Sally with id : " + sallyId + "already exists!!");
        }
    }

    private void verifyIfSallyDoesNotExist(String sallyId) {
        if (!sallyRepository.exists(sallyId)) {
            throw new IllegalArgumentException("Sally with id : " + sallyId + "does not exist!!");
        }
    }

    private Order pendingOrderToOrder(PendingOrder pendingOrder) {
        Order order = new Order();
        order.setOrderId(pendingOrder.getOrderId());
        order.setSallyId(pendingOrder.getSallyId());
        order.setUserId(pendingOrder.getUserId());
        order.setSaladId(pendingOrder.getSaladId());
        order.setPlacedTime(pendingOrder.getPlacedTime());

        return order;
    }

    private Sally prepareSally(Sally sally) {
        sally.setSallyLocation(new Point(sally.getLocation().getLatitude(), sally.getLocation().getLongitude()));
        if (sally.getPassword() != null) {
            sally.setHashedPwd(PasswordHashingUtil.hash(sally.getPassword()));
            sally.setPassword(null);
        }

        return sally;
    }

}
