/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.model.User;
import com.casabots.pantry.reporting.dao.UserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDAOImpl implements UserDAO{

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void create(User user) {
        Session session = getSession();
        session.persist(user);
        session.flush();
    }

    @Override
    @Transactional
    public List<User> list() {
        Session session = getSession();
        List<User> userList = session.createQuery("from users").list();
        return userList;
    }

    @Override
    @Transactional
    public void update(User user) {
        getSession().update(user);
    }

    @Override
    @Transactional
    public User get(long id) {
        Session session = getSession();
        User user = session.get(User.class, id);
        return user;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Session session = getSession();
        User user = session.load(User.class, id);
        session.delete(user);
    }

    @Override
    @Transactional
    public User getByUserName(String userName) {
        Session session = getSession();
        return (User) session.createCriteria(User.class)
                .add(Restrictions.eq("userName", userName)).uniqueResult();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
