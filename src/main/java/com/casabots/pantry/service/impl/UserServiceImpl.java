/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.service.impl;

import com.casabots.pantry.model.User;
import com.casabots.pantry.repository.UserRepository;
import com.casabots.pantry.service.UserService;
import com.casabots.pantry.util.PasswordHashingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean exists(String userId) {
        return userRepository.exists(userId);
    }

    @Override
    public User create(User user) {
        byte[] hash = PasswordHashingUtil.hash(user.getPassword());
        user.setPassword(null);
        user.setHashedPwd(hash);
        User createdUser = userRepository.insert(user);
        createdUser.setHashedPwd(null);
        return createdUser;
    }

    @Override
    public User update(User user) {
        if (userRepository.exists(user.getUserId())) {
            if (user.getPassword() != null) {
                byte[] hash = PasswordHashingUtil.hash(user.getPassword());
                user.setPassword(null);
                user.setHashedPwd(hash);
            }
            User updatedUser = userRepository.save(user);
            updatedUser.setHashedPwd(null);
            return updatedUser;
        }

        return null;
    }

    @Override
    public User get(String userId) {
        User user = userRepository.findOne(userId);
        user.setHashedPwd(null);
        return user;
    }

    @Override
    public void remove(String userId) {
        userRepository.delete(userId);
    }

    @Override
    public User getCompleteUser(String userId) {
        User user = userRepository.findOne(userId);

        return user;
    }

    @Override
    public boolean existsWithEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
