/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "sally_id")
    private Sally sally;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "custom_salad")
    private boolean customSalad;
    @OneToOne
    @JoinColumn(name = "salad_id")
    private Salad salad;
    @Column(name = "placed_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date placedTime;

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Sally getSally() {
        return sally;
    }

    public void setSally(Sally sally) {
        this.sally = sally;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCustomSalad() {
        return customSalad;
    }

    public void setCustomSalad(boolean customSalad) {
        this.customSalad = customSalad;
    }

    public Salad getSalad() {
        return salad;
    }

    public void setSalad(Salad salad) {
        this.salad = salad;
    }

    public Date getPlacedTime() {
        return placedTime;
    }

    public void setPlacedTime(Date placedTime) {
        this.placedTime = placedTime;
    }
}
