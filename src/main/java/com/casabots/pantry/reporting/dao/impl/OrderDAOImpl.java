/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dao.impl;

import com.casabots.pantry.reporting.dao.OrderDAO;
import com.casabots.pantry.reporting.dto.IngredientCount;
import com.casabots.pantry.reporting.dto.SaladCount;
import com.casabots.pantry.reporting.model.Location;
import com.casabots.pantry.reporting.model.Order;
import com.casabots.pantry.reporting.util.LocationUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDAOImpl implements OrderDAO {

    private static final int MAX_DISTANCE = 10;

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional
    public void create(Order order) {
        getSession().persist(order);
        getSession().flush();
    }

    @Override
    @Transactional
    public Order get(long id) {
        return getSession().get(Order.class, id);
    }

    @Override
    @Transactional
    public void update(Order order) {
        getSession().update(order);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Order order = getSession().load(Order.class, id);
        getSession().delete(order);
    }

    @Override
    @Transactional
    public List<Order> list() {
        return getSession().createQuery("from orders").list();
    }

    @Override
    @Transactional
    public List<SaladCount> getPopularSaladsByGender(Character gender, int limit) {

        Query query = getSession().createNativeQuery("select s.id, s.name, count(*) from orders \n" +
                "join salads s on orders.salad_id = s.id\n" +
                "join users u on orders.user_id = u.id\n" +
                "where u.gender= ?0\n" +
                "group by s.id\n" +
                "limit ?1")
                .setParameter(0, gender)
                .setParameter(1, limit);
        List<Object[]> results = query.list();

        return getSaladCounts(results);
    }

    @Override
    @Transactional
    public List<SaladCount> getPopularSaladsByAge(Integer startAge, Integer endAge, int limit) {

        Query query = getSession().createNativeQuery("select s.id, s.name, count(*) from orders \n" +
                "join salads s on orders.salad_id = s.id\n" +
                "join users u on orders.user_id = u.id\n" +
                "where year(now())-year(u.dob) between ?0 and ?1\n" +
                "group by s.id\n" +
                "limit ?2")
                .setParameter(0, startAge)
                .setParameter(1, endAge)
                .setParameter(2, limit);
        List<Object[]> results = query.list();

        return getSaladCounts(results);
    }

    @Override
    @Transactional
    public List<SaladCount> getPopularSaladsByLocation(Location location, int limit) {
        Location[] boundingCoords = LocationUtil.boundingCoordinates(location, MAX_DISTANCE);
        Query query = getSession().createNativeQuery("select s.id, s.name, count(*) from orders o\n" +
                "join salads s on o.salad_id = s.id\n" +
                "join sally_devices sd on o.sally_id = sd.id\n" +
                "join locations l on sd.location_id = l.id\n" +
                "where (l.latitude>= ?0 and l.latitude <= ?1) " +
                (boundingCoords[0].getLongitude() > boundingCoords[1].getLongitude() ? "or" : "and") +
                " (l.longitude >= ?2 and l.longitude <= ?3 and acos(sin(?4) * sin(radians(l.latitude)) + cos(?5) " +
                "* cos(radians(l.latitude)) * cos(radians(l.longitude) - ?6)) <= ?7)\n" +
                "group by s.id\n" +
                "limit ?8")
                .setParameter(0, boundingCoords[0].getLatitude())
                .setParameter(1, boundingCoords[1].getLatitude())
                .setParameter(2, boundingCoords[0].getLongitude())
                .setParameter(3, boundingCoords[1].getLongitude())
                .setParameter(4, Math.toRadians(location.getLatitude()))
                .setParameter(5, Math.toRadians(location.getLatitude()))
                .setParameter(6, Math.toRadians(location.getLongitude()))
                .setParameter(7, LocationUtil.getDeltaLatitude(MAX_DISTANCE))
                .setParameter(8, limit);

        return getSaladCounts(query.list());
    }

    @Override
    @Transactional
    public List<SaladCount> getPopularSaladsByTime(String startTime, String endTime, int limit) {

        Query query = getSession().createNativeQuery("select s.id, s.name, count(*) from orders \n" +
                "join salads s on orders.salad_id = s.id\n" +
                "where time_to_sec(orders.placed_time) between ?0 and ?1\n" +
                "group by s.id\n" +
                "limit ?2")
                .setParameter(0, getTimeFromString(startTime))
                .setParameter(1, getTimeFromString(endTime))
                .setParameter(2, limit);

        return getSaladCounts(query.list());
    }

    @Override
    @Transactional
    public List<IngredientCount> getPopularIngredientsByGender(Character gender, int limit) {
        Query query = getSession().createNativeQuery("select i.id, i.name, count(*) from orders \n" +
                "join salads s on orders.salad_id = s.id\n" +
                "join users u on orders.user_id = u.id\n" +
                "join salads_portions sp on sp.salad_id = s.id\n" +
                "join portions p on sp.portion_id = p.id\n" +
                "join ingredients i on p.ingredient_id = i.id\n" +
                "where u.gender=?0\n" +
                "group by i.id\n" +
                "limit ?1")
                .setParameter(0, gender)
                .setParameter(1, limit);
        List<Object[]> results = query.list();

        return getIngredientCounts(results);
    }

    @Override
    @Transactional
    public List<IngredientCount> getPopularIngredientsByAge(Integer startAge, Integer endAge, int limit) {

        Query query = getSession().createNativeQuery("select i.id, i.name, count(*) from orders " +
                "join salads s on orders.salad_id = s.id\n" +
                "join users u on orders.user_id = u.id\n" +
                "join salads_portions sp on sp.salad_id = s.id\n" +
                "join portions p on sp.portion_id = p.id\n" +
                "join ingredients i on p.ingredient_id = i.id\n" +
                "where year(now())-year(u.dob) between ?0 and ?1\n" +
                "group by i.id\n" +
                "limit ?2")
                .setParameter(0, startAge)
                .setParameter(1, endAge)
                .setParameter(2, limit);
        List<Object[]> results = query.list();

        return getIngredientCounts(results);
    }

    @Override
    @Transactional
    public List<IngredientCount> getPopularIngredientsByTime(String startTime, String endTime, int limit) {

        Query query = getSession().createNativeQuery("select i.id, i.name, count(*) from orders \n" +
                "join salads s on orders.salad_id = s.id\n" +
                "join salads_portions sp on sp.salad_id = s.id\n" +
                "join portions p on sp.portion_id = p.id\n" +
                "join ingredients i on p.ingredient_id = i.id\n" +
                "where time_to_sec(orders.placed_time) between ?0 and ?1\n" +
                "group by i.id\n" +
                "limit ?2")
                .setParameter(0, getTimeFromString(startTime))
                .setParameter(1, getTimeFromString(endTime))
                .setParameter(2, limit);

        return getIngredientCounts(query.list());
    }

    @Override
    @Transactional
    public List<IngredientCount> getPopularIngredientsByLocation(Location location, int limit) {
        Location[] boundingCoords = LocationUtil.boundingCoordinates(location, MAX_DISTANCE);
        Query query = getSession().createNativeQuery("select i.id, i.name, count(*) from orders \n" +
                "join salads s on orders.salad_id = s.id\n" +
                "join salads_portions sp on sp.salad_id = s.id\n" +
                "join portions p on sp.portion_id = p.id\n" +
                "join ingredients i on p.ingredient_id = i.id\n" +
                "join sally_devices sd on orders.sally_id = sd.id\n" +
                "join locations l on sd.location_id = l.id\n" +
                "where (l.latitude>= ?0 and l.latitude <= ?1) " +
                (boundingCoords[0].getLongitude() > boundingCoords[1].getLongitude() ? "or" : "and") +
                " (l.longitude >= ?2 and l.longitude <= ?3 and acos(sin(?4) * sin(radians(l.latitude)) + cos(?5) " +
                "* cos(radians(l.latitude)) * cos(radians(l.longitude) - ?6)) <= ?7)\n" +
                "group by i.id\n" +
                "limit ?8")
                .setParameter(0, boundingCoords[0].getLatitude())
                .setParameter(1, boundingCoords[1].getLatitude())
                .setParameter(2, boundingCoords[0].getLongitude())
                .setParameter(3, boundingCoords[1].getLongitude())
                .setParameter(4, Math.toRadians(location.getLatitude()))
                .setParameter(5, Math.toRadians(location.getLatitude()))
                .setParameter(6, Math.toRadians(location.getLongitude()))
                .setParameter(7, LocationUtil.getDeltaLatitude(MAX_DISTANCE))
                .setParameter(8, limit);

        return getSaladCounts(query.list());
    }

    private String getDateFromAge(Integer age) {
        LocalDate dob = LocalDate.now().minusYears(age);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dob.format(formatter);
    }

    private String getDateTimeFromTime(Date time) {
        return "";
    }

    private List<SaladCount> getSaladCounts(List<Object[]> results) {
        List<SaladCount> salads = new ArrayList<>();

        for (Object[] result :
                results) {
            SaladCount saladCount = new SaladCount();
            saladCount.setSaladId(((BigInteger) result[0]).longValue());
            saladCount.setSaladName((String) result[1]);
            saladCount.setCount(((BigInteger) result[2]).intValue());

            salads.add(saladCount);
        }

        return salads;
    }

    private List<IngredientCount> getIngredientCounts(List<Object[]> results) {
        List<IngredientCount> ingredients = new ArrayList<>();

        for (Object[] result :
                results) {
            IngredientCount ingredientCount = new IngredientCount();
            ingredientCount.setIngredientId(((BigInteger) result[0]).longValue());
            ingredientCount.setIngredient((String) result[1]);
            ingredientCount.setCount(((BigInteger) result[2]).intValue());

            ingredients.add(ingredientCount);
        }

        return ingredients;
    }

    private Long getTimeFromString(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return Long.valueOf(LocalTime.parse(time, formatter).toSecondOfDay());
    }
}
