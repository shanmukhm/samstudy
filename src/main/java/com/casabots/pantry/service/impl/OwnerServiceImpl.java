/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.ItemStatus;
import com.casabots.pantry.model.Owner;
import com.casabots.pantry.model.Sally;
import com.casabots.pantry.repository.OwnerRepository;
import com.casabots.pantry.repository.SallyRepository;
import com.casabots.pantry.service.OwnerService;
import com.casabots.pantry.service.SallyService;
import com.casabots.pantry.util.PasswordHashingUtil;
import com.casabots.pantry.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService{

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private SallyRepository sallyRepository;
    @Autowired
    private SallyService sallyService;

    @Override
    public boolean exists(String ownerId) {
        return ownerRepository.exists(ownerId);
    }

    @Override
    public Owner create(Owner owner) {
        byte[] hash = PasswordHashingUtil.hash(owner.getPassword());
        owner.setPassword(null);
        owner.setHashedPwd(hash);
        Owner createdOwner = ownerRepository.insert(owner);
        createdOwner.setHashedPwd(null);
        return createdOwner;
    }

    @Override
    public Owner update(Owner owner) {
        if (ownerRepository.exists(owner.getOwnerId())) {
            if (owner.getPassword() != null) {
                byte[] hash = PasswordHashingUtil.hash(owner.getPassword());
                owner.setPassword(null);
                owner.setHashedPwd(hash);
            }
            Owner updatedOwner = ownerRepository.save(owner);
            updatedOwner.setHashedPwd(null);
            return updatedOwner;
        }

        return null;
    }

    @Override
    public Owner get(String ownerId) {
        Owner owner = ownerRepository.findOne(ownerId);
        owner.setHashedPwd(null);
        return owner;
    }

    @Override
    public void remove(String ownerId) {
        ownerRepository.delete(ownerId);
    }

    @Override
    public List<Sally> getSallyDevices(String ownerId) {
        if (ownerRepository.exists(ownerId)) {
            return sallyRepository.findByOwnerId(ownerId);
        }
        return null;
    }

    @Override
    public List<ItemStatus> addSaladsToSally(String sallyId, String[] saladIds) {
        if (sallyService.isOwnedBy(sallyId, SessionUtils.getCurrentUserId())) {
            Sally sally = sallyService.get(sallyId);
            List<ItemStatus> currentSalads = sally.getSalads();
            List<ItemStatus> saladsToAdd = new ArrayList<>();
            for (String saladId :
                    saladIds) {
                saladsToAdd.add(new ItemStatus(saladId, true, 0));
            }
            currentSalads.addAll(saladsToAdd);
            sally.setSalads(currentSalads);
            Sally updatedSally = sallyService.update(sally);
            return updatedSally.getSalads();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public List<ItemStatus> addIngredientsToSally(String sallyId, String[] ingredientIds) {
        if (sallyService.isOwnedBy(sallyId, SessionUtils.getCurrentUserId())) {
            Sally sally = sallyService.get(sallyId);
            List<ItemStatus> currentIngredients = sally.getIngredients();
            List<ItemStatus> ingredientsToAdd = new ArrayList<>();
            for (String ingredientId :
                    ingredientIds) {
                ingredientsToAdd.add(new ItemStatus(ingredientId, true, 0));
            }
            currentIngredients.addAll(ingredientsToAdd);
            sally.setIngredients(currentIngredients);
            Sally updatedSally = sallyService.update(sally);
            return updatedSally.getIngredients();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public Owner getCompleteOwner(String ownerId) {
        Owner owner = ownerRepository.findOne(ownerId);

        return owner;
    }

    @Override
    public List<ItemStatus> removeSaladsFromSally(String sallyId, String[] saladIds) {
        if (sallyService.isOwnedBy(sallyId, SessionUtils.getCurrentUserId())) {
            Sally sally = sallyService.get(sallyId);
            List<ItemStatus> currentSalads = sally.getSalads();
            currentSalads.removeAll(Arrays.asList(saladIds));
            sally.setSalads(currentSalads);
            Sally updatedSally = sallyService.update(sally);
            return updatedSally.getSalads();
        }
        return null;
    }

    @Override
    public List<ItemStatus> removeIngredientsFromSally(String sallyId, String[] ingredientIds) {
        if (sallyService.isOwnedBy(sallyId, SessionUtils.getCurrentUserId())) {
            Sally sally = sallyService.get(sallyId);
            List<ItemStatus> currentIngredients = sally.getIngredients();
            currentIngredients.removeAll(Arrays.asList(ingredientIds));
            sally.setIngredients(currentIngredients);
            Sally updatedSally = sallyService.update(sally);
            return updatedSally.getIngredients();
        } else {
            throw new RuntimeException();
        }
    }
}
