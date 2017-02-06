/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "pendingOrders")
public class PendingOrder {
    @Id
    private String orderId;
    private String sallyId;
    private String userId;
    private String uuid;
    private String saladId;
    private String status;
    private boolean isOrderPlaced;
    @Indexed(expireAfterSeconds = 60)
    private Date placedTime;

    public PendingOrder() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSaladId() {
        return saladId;
    }

    public void setSaladId(String saladId) {
        this.saladId = saladId;
    }

    public String getSallyId() {

        return sallyId;
    }

    public void setSallyId(String sallyId) {
        this.sallyId = sallyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOrderPlaced() {
        return isOrderPlaced;
    }

    public void setOrderPlaced(boolean orderPlaced) {
        isOrderPlaced = orderPlaced;
    }

    public Date getPlacedTime() {
        return placedTime;
    }

    public void setPlacedTime(Date placedTime) {
        this.placedTime = placedTime;
    }
}
