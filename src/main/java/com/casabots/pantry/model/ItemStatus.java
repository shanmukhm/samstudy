/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;

public class ItemStatus {

    private String itemId;
    private boolean availability;
    private int itemsDispensed;

    public ItemStatus() {
    }

    public ItemStatus(String itemId) {
        this.itemId = itemId;
    }

    public ItemStatus(String itemId, boolean availability, int itemsDispensed) {
        this.itemId = itemId;
        this.availability = availability;
        this.itemsDispensed = itemsDispensed;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getItemsDispensed() {
        return itemsDispensed;
    }

    public void setItemsDispensed(int itemsDispensed) {
        this.itemsDispensed = itemsDispensed;
    }
}
