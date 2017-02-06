/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.Salad;
import com.casabots.pantry.repository.PortionRepository;
import com.casabots.pantry.repository.SaladRepository;
import com.casabots.pantry.service.SaladService;
import com.casabots.pantry.util.PantryUtil;
import com.casabots.pantry.util.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class SaladServiceImpl implements SaladService{

    @Autowired
    private SaladRepository saladRepository;
    @Autowired
    private PortionRepository portionRepository;

    @Override
    public Salad create(Salad salad) {
        verifyIfSaladExists(salad.getSaladId());
        verifySalad(salad);
        verifyPortions(salad);
        salad = trimStrings(salad);
        salad.setCreatedBy("admin");
        verifyCustomSaladName(salad);

        return saladRepository.insert(salad);
    }

    @Override
    public Salad get(String saladId) {
        verifyIfSaladDoesNotExist(saladId);

        return saladRepository.findOne(saladId);
    }

    @Override
    public Salad update(Salad salad) {
        verifyIfSaladDoesNotExist(salad.getSaladId());
        verifySalad(salad);
        verifyPortions(salad);
        salad = trimStrings(salad);

        return saladRepository.save(salad);
    }

    @Override
    public void delete(String saladId) {
        verifyIfSaladDoesNotExist(saladId);

        saladRepository.delete(saladId);
    }

    @Override
    public Salad createCustomSalad(Salad salad) {
        verifyIfSaladExists(salad.getSaladId());
        verifySalad(salad);
        verifyPortions(salad);
        salad.setCustomSalad(true);
        salad.setCreatedBy(SessionUtils.getCurrentUserId());
        salad = trimStrings(salad);
        verifyCustomSaladName(salad);

        return saladRepository.insert(salad);
    }

    @Override
    public List<Salad> getByUser(String userName) {
        return saladRepository.findByCreatedBy(userName);
    }

    @Override
    public List<Salad> getAll() {
        return saladRepository.findAll();
    }

    private void verifyIfSaladExists(String saladId) {
        if (saladRepository.exists(saladId)) {
            throw new IllegalArgumentException("Salad with id : " + saladId + " already exists!");
        }
    }

    private void verifyIfSaladDoesNotExist(String saladId) {
        if (!saladRepository.exists(saladId)) {
            throw new IllegalArgumentException("Salad with id : " + saladId + " does not exist!");
        }
    }

    private void verifyCustomSaladName(Salad salad) {
        if (saladRepository.findByNameAndCreatedBy(salad.getName(), salad.getCreatedBy()) != null) {
            throw new IllegalArgumentException("There is already a salad with the name : " + salad.getName());
        }
    }

    private void verifySalad(Salad salad) {
        if (StringUtils.isBlank(salad.getSaladId())) {
            throw new IllegalArgumentException("Invalid salad id!");
        }

        if (StringUtils.isBlank(salad.getName())) {
            throw new IllegalArgumentException("Invalid salad name!");
        }

        Assert.notEmpty(salad.getPortions(), "Portions should not be empty!!");
    }

    private void verifyPortions(Salad salad) {
        for (String portionId :
                salad.getPortions()) {
            if (!portionRepository.exists(portionId)) {
                throw new IllegalArgumentException("The portion with id : " + portionId + " does not exist!!");
            }
        }
    }

    private Salad trimStrings(Salad salad) {

        salad.setSaladId(salad.getSaladId().trim());
        salad.setName(salad.getName().trim());
        String[] saladProperties = { salad.getSaladId(), salad.getName()};
        PantryUtil.verifyStringsLength(saladProperties);

        if (StringUtils.isNotBlank(salad.getImageUrl())) {
            salad.setImageUrl(salad.getImageUrl().trim());
            PantryUtil.verifyImageUrl(salad.getImageUrl());
            PantryUtil.verifyStringLength(salad.getImageUrl());
        }
        if (StringUtils.isNotBlank(salad.getCreatedBy())) {
            salad.setCreatedBy(salad.getCreatedBy().trim());
            PantryUtil.verifyStringLength(salad.getCreatedBy());
        }

        return salad;
    }
}
