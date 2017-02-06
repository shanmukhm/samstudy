/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;

import java.util.List;

public class SaladList {

    private List<String> preDefinedSalads;
    private List<CustomSalad> customSalads;

    public SaladList() {}

    public SaladList(List<String> existingSalads, List<CustomSalad> customSalads) {
        this.preDefinedSalads = existingSalads;
        this.customSalads = customSalads;
    }

    public List<String> getPreDefinedSalads() {
        return preDefinedSalads;
    }

    public void setPreDefinedSalads(List<String> preDefinedSalads) {
        this.preDefinedSalads = preDefinedSalads;
    }

    public List<CustomSalad> getCustomSalads() {
        return customSalads;
    }

    public void setCustomSalads(List<CustomSalad> customSalads) {
        this.customSalads = customSalads;
    }
}
