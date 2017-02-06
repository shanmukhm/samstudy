/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.model.Ingredient;
import com.casabots.pantry.reporting.dao.IngredientDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientDAOImpl implements IngredientDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(Ingredient ingredient) {
        getSession().persist(ingredient);
        getSession().flush();
    }

    @Override
    @Transactional
    public Ingredient get(long id) {
        return getSession().load(Ingredient.class, id);
    }

    @Override
    @Transactional
    public void update(Ingredient ingredient) {
        getSession().update(ingredient);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Ingredient ingredient = getSession().load(Ingredient.class, id);
        getSession().delete(ingredient);
    }

    @Override
    @Transactional
    public List<Ingredient> list() {
        return getSession().createQuery("from ingredients").list();
    }

    @Override
    @Transactional
    public Ingredient getByGuid(String guid) {
        Session session = getSession();
        return (Ingredient) session.createCriteria(Ingredient.class)
                .add(Restrictions.eq("guid", guid)).uniqueResult();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
