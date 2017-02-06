/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service;

import com.casabots.pantry.model.User;

import java.util.List;

public interface UserService {

    boolean exists(String userId);

    User create(User user);

    User update(User user);

    User get(String userId);

    void remove(String userId);

    User getCompleteUser(String userId);

    boolean existsWithEmail(String email);

    List<User> getAll();
}
