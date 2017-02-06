/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import com.casabots.pantry.model.ItemStatus;
import com.casabots.pantry.model.Owner;
import com.casabots.pantry.model.Sally;

import java.util.List;

public interface OwnerService {

    boolean exists(String ownerId);

    Owner create(Owner owner);

    Owner update(Owner owner);

    Owner get(String ownerId);

    void remove(String ownerId);

    List<Sally> getSallyDevices(String ownerId);
    
    List<ItemStatus> addSaladsToSally(String sallyId, String[] saladIds);
    
    List<ItemStatus> addIngredientsToSally(String sallyId, String[] ingredientIds);

    Owner getCompleteOwner(String ownerId);

    List<ItemStatus> removeSaladsFromSally(String sallyId, String[] saladIds);

    List<ItemStatus> removeIngredientsFromSally(String sallyId, String[] ingredientIds);
}
