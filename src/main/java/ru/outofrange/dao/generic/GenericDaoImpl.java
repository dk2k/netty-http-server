package ru.outofrange.dao.generic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ru.outofrange.db.HibernateSessionManager;

public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {
	 
    protected Session getSession() {
        return HibernateSessionManager.getSession();
    }
 
    /*public void save(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.saveOrUpdate(entity);
    }*/
 
    public void merge(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.merge(entity);
    }
    
    public void persist(T entity) {
		Session hibernateSession = this.getSession();
		Transaction tr = hibernateSession.beginTransaction();
        hibernateSession.save(entity);
        tr.commit();
    }
 
    public void update(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.update(entity);
    }
 
    public void delete(T entity) {
        Session hibernateSession = this.getSession();
        Transaction tr = hibernateSession.beginTransaction();
        hibernateSession.delete(entity);
        tr.commit();
    }
 
    public List<T> findMany(Query query) {
        List<T> t;
        t = (List<T>) query.list();
        return t;
    }
 
    public T findOne(Query query) {
        T t;
        t = (T) query.uniqueResult();
        return t;
    }
 
    public T findByID(Class clazz, Long id) {
        Session hibernateSession = this.getSession();
        T t = null;
        t = (T) hibernateSession.get(clazz, id);
        return t;
    }
 
    public List<T> findAll(Class clazz) {
        Session hibernateSession = this.getSession();
        List T = null;
        Query query = hibernateSession.createQuery("from " + clazz.getName());
        T = query.list();
        return T;
    }
    
    
}
