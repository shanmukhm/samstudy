/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.model.Salad;
import com.casabots.pantry.reporting.dao.SaladDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SaladDAOImpl implements SaladDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(Salad salad) {
        getSession().persist(salad);
        getSession().flush();
    }

    @Override
    @Transactional
    public Salad get(long id) {
        return getSession().load(Salad.class, id);
    }

    @Override
    @Transactional
    public void update(Salad salad) {
        getSession().update(salad);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Salad salad = getSession().load(Salad.class, id);
        getSession().delete(salad);
    }

    @Override
    @Transactional
    public List<Salad> list() {
        return getSession().createQuery("from salads").list();
    }

    @Override
    @Transactional
    public Salad getByGuid(String guid) {
        Session session = getSession();
        return (Salad) session.createCriteria(Salad.class)
                .add(Restrictions.eq("guid", guid)).uniqueResult();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
