/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Size;
import com.casabots.pantry.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This Controller is to perform CRUD operations on global list of sizes.
 * Information of sizes can be updated time to time using this controller.
 */
@RestController
@RequestMapping("sizes")
public class SizeController {

    @Autowired
    private SizeRepository sizeRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Size createSize(@RequestBody Size size) {
        return sizeRepository.insert(size);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{sizeId}")
    public Size getSize(@PathVariable String sizeId) {
        return sizeRepository.findOne(sizeId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public Size updateSize(@RequestBody Size size) {
        if (sizeRepository.exists(size.getId())) {
            return sizeRepository.save(size);
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{sizeId}")
    public void deleteSize(@PathVariable String sizeId) {
        sizeRepository.delete(sizeId);
    }
}
