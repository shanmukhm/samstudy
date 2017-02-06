/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dto;

public class SaladCount {

    private Long saladId;
    private String saladName;
    private int count;

    public SaladCount() {

    }

    public SaladCount(Long saladId, String saladName, int count) {
        this.saladId = saladId;
        this.saladName = saladName;
        this.count = count;
    }

    public Long getSaladId() {
        return saladId;
    }

    public void setSaladId(Long saladId) {
        this.saladId = saladId;
    }

    public String getSaladName() {
        return saladName;
    }

    public void setSaladName(String saladName) {
        this.saladName = saladName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
