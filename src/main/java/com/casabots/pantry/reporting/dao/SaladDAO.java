/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;

import com.casabots.pantry.reporting.model.Salad;

import java.util.List;

public interface SaladDAO {

    void create(Salad salad);

    Salad get(long id);

    void update(Salad salad);

    void delete(long id);

    List<Salad> list();

    Salad getByGuid(String guid);
}
