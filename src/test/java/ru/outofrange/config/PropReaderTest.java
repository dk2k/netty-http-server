package ru.outofrange.config;

import org.junit.Assert;

import org.junit.BeforeClass;
//import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PropReaderTest {

	private static final String TEST_CONFIG = "test.config";
	private static final String KEY1 = "value1";
	private static final String KEY2 = "value2";
	private static final String VALUE1 = "111 222";
	private static final String VALUE2 = "/home/arty/";
	
	private static ApplicationContext context;
	
    @BeforeClass
    public static void setUp(){
        context = new ClassPathXmlApplicationContext("testApplicationContext.xml");
    }
	
    @Test
    public void testConfigReading(){
    	PropReader reader = (PropReader) context.getBean("configService");
    	
    	reader.setFilename(TEST_CONFIG);
    	reader.parse();
    	
    	String value = reader.getProperty(KEY1);
    	Assert.assertTrue("First value read", value.equals(VALUE1));
    	
    	value = reader.getProperty(KEY2);
    	Assert.assertTrue("Second value read", value.equals(VALUE2));
    }
	
}
