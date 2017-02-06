/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;

import com.casabots.pantry.reporting.model.Salad;
import com.casabots.pantry.reporting.model.Sally;

import java.util.List;

public interface SallyDAO {

    void create(Sally sally);

    Sally get(long id);

    void update(Sally sally);

    void delete(long id);

    List<Sally> list();

    Sally getByGuid(String guid);
}
