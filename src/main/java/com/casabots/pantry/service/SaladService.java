/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import com.casabots.pantry.model.Salad;

import java.util.List;

public interface SaladService {

    Salad create(Salad salad);

    Salad get(String saladId);

    Salad update(Salad salad);

    void delete(String saladId);

    Salad createCustomSalad(Salad salad);

    List<Salad> getByUser(String userName);

    List<Salad> getAll();
}
