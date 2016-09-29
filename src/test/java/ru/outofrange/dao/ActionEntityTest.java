package ru.outofrange.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.outofrange.dao.ActionEntityDaoImpl;
import ru.outofrange.model.ActionEntity;
import ru.outofrange.service.ActionEntityServiceImpl;
import ru.outofrange.service.ActionEntityServiceInterface;

public class ActionEntityTest {

	public static final String TEST_ACTION = "test_action_1111";
	public static final String TEST_RESPONSE = "<test_response>111</test_response>";
	
	//private ActionEntityDaoImpl actionEntityDao = new ActionEntityDaoImpl();
	
	private static ApplicationContext context;
	
    @BeforeClass
    public static void setUp(){
        context = new ClassPathXmlApplicationContext("testApplicationContext.xml");
    }
	
    @Test
    public void testActionGetsAddedAnDeleted(){
    	 
    	ActionEntityServiceInterface actionService = 
    			(ActionEntityServiceImpl) context.getBean("actionService");
    	
    	 // creating a test entry
    	 ActionEntity action = new ActionEntity();
    	 action.setAction(TEST_ACTION);
    	 action.setResponse(TEST_RESPONSE);
    	 
    	 // inserting the entry
    	 actionService.persist(action);
    	 
    	 ActionEntity act = actionService.findByAction(TEST_ACTION);
    	 
    	 Assert.assertTrue("Property action stored", act.getAction().equals(TEST_ACTION));
    	 Assert.assertTrue("Property response stored", act.getResponse().equals(TEST_RESPONSE));
    	 
    	 // cleanup
    	 actionService.delete(act);
    	 
    	 // nothing is to be found
    	 act = actionService.findByAction(TEST_ACTION);
    	 Assert.assertTrue("ActionEntity got deleted", act == null);
    	 
    }
}
