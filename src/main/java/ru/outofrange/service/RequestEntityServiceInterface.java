package ru.outofrange.service;

import java.util.Date;
import java.util.List;

import ru.outofrange.model.ActionEntity;
import ru.outofrange.model.RequestEntity;

public interface RequestEntityServiceInterface {
	
	public void persist(RequestEntity request);
	public void update(RequestEntity entity);
	public void delete(Long id);
	public void delete(RequestEntity request);
	public List<RequestEntity> findAll();
	public RequestEntity findByID(Long id);
	List<RequestEntity> findByExtractedContent(String content);
	//public void deleteAll();
	
	public void logRequest(Date currentDate, String remoteHost, String method, String requestBodyAsString, String action, String responseFromDB);
}
