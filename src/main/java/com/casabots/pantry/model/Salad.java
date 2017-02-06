/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "salads")
public class Salad {

    @Id
    private String saladId;
    private String name;
    private String imageUrl;
    private boolean available;
    private boolean customSalad;
    private String createdBy;
    private List<String> portions;

    public List<String> getPortions() {
        return portions;
    }

    public void setPortions(List<String> portions) {
        this.portions = portions;
    }

    public String getSaladId() {
        return saladId;
    }

    public void setSaladId(String saladId) {
        this.saladId = saladId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public boolean isCustomSalad() {
        return customSalad;
    }

    public void setCustomSalad(boolean customSalad) {
        this.customSalad = customSalad;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;

    }
}
