package ru.outofrange.dao;

import java.util.List;

import org.hibernate.Query;
import ru.outofrange.dao.generic.GenericDaoImpl;
import ru.outofrange.db.HibernateSessionManager;
import ru.outofrange.model.RequestEntity;

public class RequestEntityDaoImpl 
extends GenericDaoImpl<RequestEntity, Long>  
implements RequestEntityDao {
	
	public static final String QUERY_BY_CONTENT = "From RequestEntity as e where e.extractedContent=:extractedContent";

	public RequestEntityDaoImpl() {
		
	}

	public List<RequestEntity> findByExtractedContent(String content) {
		
   	 	Query q = HibernateSessionManager.beginTransaction().createQuery(QUERY_BY_CONTENT);
   	 	q.setString("extractedContent", content);

		List<RequestEntity> requests = findMany(q);
		HibernateSessionManager.commitTransaction();
		return requests;

	}

}
