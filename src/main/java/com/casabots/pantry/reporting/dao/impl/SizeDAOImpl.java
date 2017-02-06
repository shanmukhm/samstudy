/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.model.Size;
import com.casabots.pantry.reporting.dao.SizeDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SizeDAOImpl implements SizeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(Size size) {
        getSession().persist(size);
        getSession().flush();
    }

    @Override
    @Transactional
    public Size get(long id) {
        return getSession().load(Size.class, id);
    }

    @Override
    @Transactional
    public void update(Size size) {
        getSession().update(size);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Size size = getSession().load(Size.class, id);
        getSession().delete(size);
    }

    @Override
    @Transactional
    public List<Size> list() {
        return getSession().createQuery("from sizes").list();
    }

    @Override
    @Transactional
    public Size getByGuid(String guid) {
        Session session = getSession();
        return (Size) session.createCriteria(Size.class)
                .add(Restrictions.eq("guid", guid)).uniqueResult();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
