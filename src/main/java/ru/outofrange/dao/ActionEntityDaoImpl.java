package ru.outofrange.dao;

import org.hibernate.Query;
import ru.outofrange.dao.generic.GenericDaoImpl;
import ru.outofrange.db.HibernateSessionManager;
import ru.outofrange.model.ActionEntity;

public class ActionEntityDaoImpl 
extends GenericDaoImpl<ActionEntity, Long>  
implements ActionEntityDao{

	public static final String QUERY_BY_ACTION = "From ActionEntity as e where e.action=:action";

	public ActionEntityDaoImpl() {
		
	}

	// we expect that action is unique within the DB table
	// so we can find one entry maximum
	public ActionEntity findByAction(String action) {

   	 	Query q = HibernateSessionManager.beginTransaction().createQuery(QUERY_BY_ACTION);
   	 	q.setString("action", action);
   	 	
   	 	q.setCacheable(true);
   	 	
   	 	ActionEntity act = findOne(q);
   	    HibernateSessionManager.commitTransaction();
   	    
   	 	return act;
	}
}
