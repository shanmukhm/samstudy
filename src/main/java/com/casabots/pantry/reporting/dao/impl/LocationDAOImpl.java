/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.model.Location;
import com.casabots.pantry.reporting.dao.LocationDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationDAOImpl implements LocationDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(Location location) {
        getSession().persist(location);
    }

    @Override
    @Transactional
    public Location get(long id) {
        return getSession().load(Location.class, id);
    }

    @Override
    @Transactional
    public void update(Location location) {
        getSession().update(location);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Location location = getSession().load(Location.class, id);
        getSession().delete(location);
    }

    @Override
    @Transactional
    public List<Location> list() {
        return getSession().createQuery("from locations").list();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
