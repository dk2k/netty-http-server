package ru.outofrange.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import ru.outofrange.dao.RequestEntityDaoImpl;
import ru.outofrange.model.ActionEntity;
import ru.outofrange.model.RequestEntity;

@Service("actionService")
public class RequestEntityServiceImpl implements RequestEntityServiceInterface{
	private RequestEntityDaoImpl requestEntityDao;
	
	public RequestEntityServiceImpl(){
		this.requestEntityDao = new RequestEntityDaoImpl();
	}

	public void persist(RequestEntity request) {

	requestEntityDao.persist(request);

	}

	public void update(RequestEntity entity) {
		requestEntityDao.update(entity);
	}

	public RequestEntity findById(Long id) {
		RequestEntity request = requestEntityDao.findByID(RequestEntity.class, id);
		return request;
	}

	public void delete(Long id) {
		RequestEntity request = requestEntityDao.findByID(RequestEntity.class, id);
		requestEntityDao.delete(request);
	}

	public void delete(RequestEntity request) {

		requestEntityDao.delete(request);
	}
	
	public List<RequestEntity> findAll() {
		List<RequestEntity> requests = requestEntityDao.findAll(RequestEntity.class);
		return requests;
	}

	/*public void deleteAll() {
		requestEntityDao.deleteAll();
	}*/
	
	public RequestEntity findByID(Long id) {
		RequestEntity req = requestEntityDao.findByID(RequestEntity.class, id);
		return req;
	}

	public void logRequest(Date date, String remoteHost, String method,
		String requestBody, String extractedContent, String responseFromDB) {
	
		RequestEntity request = new RequestEntity(date, remoteHost, method,
			requestBody, extractedContent, responseFromDB);
		persist(request);
	}

	public RequestEntityDaoImpl getRequestEntityDao() {
		return requestEntityDao;
	}

	@Override
	public List<RequestEntity> findByExtractedContent(String content) {
		
		return requestEntityDao.findByExtractedContent(content);
	}
	
}
