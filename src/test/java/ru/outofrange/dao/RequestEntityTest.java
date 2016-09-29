package ru.outofrange.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.outofrange.dao.RequestEntityDaoImpl;
import ru.outofrange.model.RequestEntity;
import ru.outofrange.service.RequestEntityServiceImpl;
import ru.outofrange.service.RequestEntityServiceInterface;

public class RequestEntityTest {

	public static final String TEST_ACTION1 = "test_action1";
	public static final String TEST_RESPONSE1 = "<test_response>111</test_response>";
	public static final String TEST_RESPONSE2 = "<test_response>222</test_response>";
	
	private static ApplicationContext context;
	
    @BeforeClass
    public static void setUp(){
        context = new ClassPathXmlApplicationContext("testApplicationContext.xml");
    }
	
    @Test
    public void testRequestsGetsAddedAndDeleted(){
    	
    	RequestEntityServiceInterface requestService = (RequestEntityServiceImpl) context
				.getBean("requestService");
    	 
    	 // creating test entries
    	 RequestEntity request1 = new RequestEntity();
    	 request1.setExtractedContent(TEST_ACTION1);
    	 request1.setRequestBody(TEST_RESPONSE1);
    	 request1.setDate(new Date());
    	 request1.setRemoteHost("100.100.100.100");
    	 request1.setMethod("HEAD");
    	 
    	 RequestEntity request2 = new RequestEntity();
    	 request2.setExtractedContent(TEST_ACTION1);
    	 request2.setRequestBody(TEST_RESPONSE2);
    	 request2.setDate(new Date());
    	 request2.setRemoteHost("101.101.101.101");
    	 request2.setMethod("POST");
    	
    	 
    	 // inserting the entries
    	 requestService.persist(request1);
    	 requestService.persist(request2);
    	 
    	 List<RequestEntity> requests = requestService.findByExtractedContent(TEST_ACTION1);
    	 
    	 
    	 // cleanup
    	 for (RequestEntity req: requests) {
    		 System.out.println(req);
    		 requestService.delete(req);
    	 }
    	 
    	 // nothing is to be found
    	 requests = requestService.findByExtractedContent(TEST_ACTION1);
    	 Assert.assertTrue("RequestEntities got deleted", (requests.size() == 0));
    	 
    }
}

