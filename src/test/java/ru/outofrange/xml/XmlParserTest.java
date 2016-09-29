package ru.outofrange.xml;

import org.junit.Assert;
import org.junit.BeforeClass;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class XmlParserTest {
	
	static private final String path3333 = "/Employees/Employee[@emplid='3333']/email";
	static private final String expectedContent3333 = "jim@sh.com";
	
	static private final String xmlDoc = 
			"<?xml version=\"1.0\"?>" +
	"<Employees>" +
	    "<Employee emplid=\"1111\" type=\"admin\">" +
	        "<firstname>John</firstname>" +
	        "<lastname>Watson</lastname>" +
	        "<age>30</age>" +
	        "<email>johnwatson@sh.com</email>" +
	    "</Employee>" +
	    "<Employee emplid=\"2222\" type=\"admin\">" +
	        "<firstname>Sherlock</firstname>" +
	        "<lastname>Homes</lastname>" +
	        "<age>32</age>" +
	        "<email>sherlock@sh.com</email>" +
	    "</Employee>" +
	    "<Employee emplid=\"3333\" type=\"user\">" +
	        "<firstname>Jim</firstname>" +
	        "<lastname>Moriarty</lastname>" +
	        "<age>52</age>" +
	        "<email>jim@sh.com</email>" +
	    "</Employee>" +
	    "<Employee emplid=\"4444\" type=\"user\">" +
	        "<firstname>Mycroft</firstname>" +
	        "<lastname>Holmes</lastname>" +
	        "<age>41</age>" +
	        "<email>mycroft@sh.com</email>" +
	    "</Employee>" +
	"</Employees>";
	
	private static ApplicationContext context;
	private static XmlParser parser;
	
    @BeforeClass
    public static void setUp(){
        context = new ClassPathXmlApplicationContext("testApplicationContext.xml");
        parser = (XmlParser) context.getBean("parsingService");
    }
	
    
    @Test
    public void testExtraction1(){
    	
    	parser.setPath(path3333);
    	String content = parser.extractContentOfElement(xmlDoc);
    	//System.out.println(content);
    	Assert.assertTrue("Value 1 read OK", content.equals(expectedContent3333));
    }
	
}
