/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Portion;
import com.casabots.pantry.repository.IngredientRepository;
import com.casabots.pantry.repository.PortionRepository;
import com.casabots.pantry.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This Controller is to perform CRUD operations on global list of portions.
 * Information of portions can be updated time to time using this controller.
 */
@RestController
@RequestMapping("portions")
public class PortionController {

    @Autowired
    private PortionRepository portionRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Portion createPortion(@RequestBody Portion portion) {
        if (!portionRepository.exists(portion.getId()) && isValidPortion(portion)) {
            return portionRepository.insert(portion);
        } else {
            throw new RuntimeException("Not a valid portion! Some details are not currect!");
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{portionId}")
    public Portion getPortion(@PathVariable String portionId) {
        return portionRepository.findOne(portionId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public Portion updatePortion(@RequestBody Portion portion) {
        if (portionRepository.exists(portion.getId()) && isValidPortion(portion)) {
            return portionRepository.save(portion);
        } else {
            throw new RuntimeException("Not a valid portion! Some details are not currect!");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{portionId}")
    public void deletePortion(@PathVariable String portionId) {
        if (portionRepository.exists(portionId)) {
            portionRepository.delete(portionId);
        } else {
            throw new RuntimeException("Portion with id : " + portionId + " does not exist!!");
        }

    }

    private boolean isValidPortion(Portion portion) {
        return sizeRepository.exists(portion.getSizeId())
                && ingredientRepository.exists(portion.getIngredientId());
    }
}
