/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.model.Portion;
import com.casabots.pantry.reporting.dao.PortionDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PortionDAOImpl implements PortionDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(Portion portion) {
        getSession().persist(portion);
        getSession().flush();
    }

    @Override
    @Transactional
    public Portion get(long id) {
        return getSession().load(Portion.class, id);
    }

    @Override
    @Transactional
    public void update(Portion portion) {
        getSession().update(portion);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Portion portion = getSession().load(Portion.class, id);
        getSession().delete(portion);
    }

    @Override
    @Transactional
    public List<Portion> list() {
        return getSession().createQuery("from portions").list();
    }

    @Override
    @Transactional
    public Portion getByGuid(String guid) {
        Session session = getSession();
        return (Portion) session.createCriteria(Portion.class)
                .add(Restrictions.eq("guid", guid)).uniqueResult();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
