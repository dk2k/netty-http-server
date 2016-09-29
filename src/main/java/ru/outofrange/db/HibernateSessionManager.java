package ru.outofrange.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateSessionManager {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
	
    public static SessionFactory getSessionFactory() {
    	return sessionFactory;
    }
    	 
    public static Session beginTransaction() {
    	Session hibernateSession = HibernateSessionManager.getSession();
    	hibernateSession.beginTransaction();
    	return hibernateSession;
    }
    	 
    public static void commitTransaction() {
    	HibernateSessionManager.getSession().getTransaction().commit();
    }
    	 
    public static void rollbackTransaction() {
    	HibernateSessionManager.getSession().getTransaction().rollback();
    }
    	 
    public static void closeSession() {
    	HibernateSessionManager.getSession().close();
    }
    	 
    public static Session getSession() {
    	Session hibernateSession = sessionFactory.getCurrentSession();
    	return hibernateSession;
    }

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}
