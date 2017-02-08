package com.samstudy.service.impl;

import com.samstudy.model.User;
import com.samstudy.repository.UserRepository;
import com.samstudy.service.UserService;
import com.samstudy.util.PasswordHashingUtil;
import com.samstudy.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

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
    public User get(String userId) {
        User user = userRepository.findOne(userId);
        user.setHashedPwd(null);
        return user;
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
    public User getCompleteUser(String userId) {
        User user = userRepository.findOne(userId);

        return user;
    }

    @Override
    public User getCurrentUser() {
        return userRepository.findOne(SessionUtils.getCurrentUserId());
    }

    @Override
    public void deleteCurrentUser() {
        userRepository.delete(SessionUtils.getCurrentUserId());
    }
}
