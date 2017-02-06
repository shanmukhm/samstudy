/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.model;

import javax.persistence.*;

@Entity
@Table(name = "portions")
public class Portion {

    @Id
    @GeneratedValue
    private long id;
    @OneToOne
    @JoinColumn(name = "size_id")
    private Size size;
    @OneToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
    @Column(nullable = false)
    private int calories;
    private String guid;

    public Portion() {
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public long getId() {
        return id;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
