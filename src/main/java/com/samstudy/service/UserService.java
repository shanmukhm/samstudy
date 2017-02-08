package com.samstudy.service;

import com.samstudy.model.User;

public interface UserService {
    User create(User user);

    User get(String userId);

    User update(User user);

    User getCompleteUser(String userId);

    User getCurrentUser();

    void deleteCurrentUser();
}
