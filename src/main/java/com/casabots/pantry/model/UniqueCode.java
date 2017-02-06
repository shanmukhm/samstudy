/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "uniqueCode")
public class UniqueCode {

    @Id
    private String id;
    private String sallyId;
    private String uuid;

    public UniqueCode() {
    }

    public UniqueCode(String sallyId, String uuid) {
        this.sallyId = sallyId;
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSallyId() {
        return sallyId;
    }

    public void setSallyId(String sallyId) {
        this.sallyId = sallyId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
