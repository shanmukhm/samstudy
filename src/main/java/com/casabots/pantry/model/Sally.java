/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sally")
public class Sally {

    @Id
    private String sallyId;
    private String name;
    private GeoLocation location;
    @JsonIgnore
    @GeoSpatialIndexed
    private Point sallyLocation;
    private List<ItemStatus> salads;
    private List<ItemStatus> ingredients;
    private String ownerId;
    @Transient
    private String password;
    @JsonIgnore
    private byte[] hashedPwd;

    public String getSallyId() {
        return sallyId;
    }

    public void setSallyId(String sallyId) {
        this.sallyId = sallyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public Point getSallyLocation() {
        return sallyLocation;
    }

    public void setSallyLocation(Point sallyLocation) {
        this.sallyLocation = sallyLocation;
    }

    public List<ItemStatus> getSalads() {
        return salads;
    }

    public void setSalads(List<ItemStatus> salads) {
        this.salads = salads;
    }

    public List<ItemStatus> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ItemStatus> ingredients) {
        this.ingredients = ingredients;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getHashedPwd() {
        return hashedPwd;
    }

    public void setHashedPwd(byte[] hashedPwd) {
        this.hashedPwd = hashedPwd;
    }
}
