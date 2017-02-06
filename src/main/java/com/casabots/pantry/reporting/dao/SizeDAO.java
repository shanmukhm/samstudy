/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;

import com.casabots.pantry.reporting.model.Size;

import java.util.List;

public interface SizeDAO {

    void create(Size size);

    Size get(long id);

    void update(Size size);

    void delete(long id);

    List<Size> list();

    Size getByGuid(String guid);
}
