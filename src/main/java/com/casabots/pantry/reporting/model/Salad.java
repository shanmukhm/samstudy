/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "salads")
public class Salad {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String guid;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "salads_portions",
            joinColumns = {
                    @JoinColumn(name = "salad_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "portion_id")
            })
    private List<Portion> portions = new ArrayList<>();

    public Salad() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<Portion> getPortions() {
        return portions;
    }

    public void setPortions(List<Portion> portions) {
        this.portions = portions;
    }
}
