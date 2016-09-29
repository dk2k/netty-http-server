package ru.outofrange.dao;

import ru.outofrange.model.ActionEntity;

public interface ActionEntityDao {

    public ActionEntity findByAction(String action);
	
}
