package com.spring.dao.impl;

import com.spring.dao.interfaces.GenericDao;
import com.spring.util.AbstractPersistentObject;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;

public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

    public GenericDaoImpl(SessionFactory sessionFactory, Class<T> type) {
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    private SessionFactory sessionFactory;
    private Class<T> type;

    @Override
    public PK create(T o) {
        return (PK) getCurrentSession().save(o);
    }

    @Override
    public T get(PK id) {
        T value = (T) getCurrentSession().get(type, id);
        if (value == null) {
            return null;
        }

        if (value instanceof HibernateProxy) {
            Hibernate.initialize(value);
            value = (T) ((HibernateProxy) value).getHibernateLazyInitializer().getImplementation();
        }
        return value;
    }

    public List<T> getAll() {
        Criteria crit = getCurrentSession().createCriteria(type);
        return crit.list();
    }

    public void createOrUpdate(T o) throws Exception{
        if (o instanceof AbstractPersistentObject) {
            if (((AbstractPersistentObject) o).isCreation()) {
                //getCurrentSession().save(o);
                getCurrentSession().saveOrUpdate(o);
            } else {
                getCurrentSession().merge(o);
            }
        }

    }

    public void update(T o) {
        getCurrentSession().update(o);
    }

    public void delete(T o) {
        getCurrentSession().delete(o);
    }

    public void deleteById(PK id) {
        T entity = get(id);
        delete(entity);
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
