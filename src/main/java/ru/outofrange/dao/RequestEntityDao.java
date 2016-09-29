package ru.outofrange.dao;

import java.util.List;

import ru.outofrange.dao.generic.GenericDao;
import ru.outofrange.model.RequestEntity;

public interface RequestEntityDao extends GenericDao<RequestEntity, Long> {
	
    public List<RequestEntity> findByExtractedContent(String action);

}
