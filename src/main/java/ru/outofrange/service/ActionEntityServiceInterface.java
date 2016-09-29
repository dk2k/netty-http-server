package ru.outofrange.service;

import java.util.List;

import ru.outofrange.model.ActionEntity;

public interface ActionEntityServiceInterface {

	public String getResponseForAction(String action);
	public void persist(ActionEntity entity);
	public void update(ActionEntity entity);
	public ActionEntity findByID(Long id);
	public ActionEntity findByAction(String action);
	public void delete(Long id);
	public void delete(ActionEntity act);
	public List<ActionEntity> findAll();
	//public void deleteAll();
}
