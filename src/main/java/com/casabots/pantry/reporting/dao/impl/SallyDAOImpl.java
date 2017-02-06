/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.model.Sally;
import com.casabots.pantry.reporting.dao.SallyDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SallyDAOImpl implements SallyDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(Sally sally) {
        getSession().persist(sally);
        getSession().flush();
    }

    @Override
    @Transactional
    public Sally get(long id) {
        return getSession().load(Sally.class, id);
    }

    @Override
    @Transactional
    public void update(Sally sally) {
        getSession().update(sally);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Sally sally = getSession().load(Sally.class, id);
        getSession().delete(sally);
    }

    @Override
    @Transactional
    public List<Sally> list() {
        return getSession().createQuery("from sally").list();
    }

    @Override
    @Transactional
    public Sally getByGuid(String guid) {
        Session session = getSession();
        return (Sally) session.createCriteria(Sally.class)
                .add(Restrictions.eq("guid", guid)).uniqueResult();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
