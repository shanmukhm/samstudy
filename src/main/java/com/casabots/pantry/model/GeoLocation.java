/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;


public class GeoLocation {

    private String name;
    private double latitude;
    private double longitude;

    public GeoLocation() {
    }

    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // returns the latitude of this geo location
    public double getLatitude() {
        return latitude;
    }

    // returns the longitude of this geo location
    public double getLongitude() {
        return longitude;
    }
}
