package ru.outofrange.service;

import java.util.List;

import ru.outofrange.dao.ActionEntityDaoImpl;
import ru.outofrange.model.ActionEntity;

//import org.hibernate.Session;
//import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

@Service("actionService")
public class ActionEntityServiceImpl implements ActionEntityServiceInterface {
	
	private ActionEntityDaoImpl actionEntityDao;
	
	public ActionEntityServiceImpl(){
		this.actionEntityDao = new ActionEntityDaoImpl();
	}
	
	public void persist(ActionEntity entity) {
		actionEntityDao.persist(entity);
	}

	public void update(ActionEntity entity) {
		actionEntityDao.update(entity);
	}

	public ActionEntity findByID(Long id) {
		ActionEntity action = actionEntityDao.findByID(ActionEntity.class, id);
		return action;
	}

	public void delete(Long id) {
		ActionEntity action = actionEntityDao.findByID(ActionEntity.class, id);
		actionEntityDao.delete(action);
	}
	
	public void delete(ActionEntity action) {
		actionEntityDao.delete(action);
	}

	public List<ActionEntity> findAll() {

		List<ActionEntity> actions = actionEntityDao.findAll(ActionEntity.class);
		return actions;
	}

	/*public void deleteAll() {
		actionEntityDao.deleteAll();
	}*/

	public String getResponseForAction(String actionString) {
		
		ActionEntity action = actionEntityDao.findByAction(actionString);
		
		if (action != null) {
			return action.getResponse(); 
		} else {
			// null means no action entry was found in DB for this string
			return null;
		}
	}

	@Override
	public ActionEntity findByAction(String actionString) {
		// TODO Auto-generated method stub
		return actionEntityDao.findByAction(actionString);
	}
	
}
