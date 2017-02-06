/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;

import com.casabots.pantry.reporting.model.Location;

import java.util.List;

public interface LocationDAO {

    void create(Location location);

    Location get(long id);

    void update(Location location);

    void delete(long id);

    List<Location> list();
}
