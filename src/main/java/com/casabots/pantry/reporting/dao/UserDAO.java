/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao;

import com.casabots.pantry.reporting.model.User;

import java.util.List;

public interface UserDAO {

    void create(User user);

    List<User> list();

    void update(User user);

    User get(long id);

    void delete(long id);

    User getByUserName(String userName);
}
