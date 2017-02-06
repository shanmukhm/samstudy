/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;

import com.casabots.pantry.reporting.model.Portion;

import java.util.List;

public interface PortionDAO {

    void create(Portion portion);

    Portion get(long id);

    void update(Portion portion);

    void delete(long id);

    List<Portion> list();

    Portion getByGuid(String guid);
}
