package ru.outofrange.xml;

import java.io.ByteArrayInputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/*
 * several singletons for XML processing are accessed via double checked locking
 * 
 * */

@Service("parsingService")
public class XmlParser {

	private String path = null;
	private Boolean xmlCacheEnabled = false;
	
	private static XPath xPath = null;
	static XPathFactory xPathFactory;
	static DocumentBuilderFactory builderFactory;
	static DocumentBuilder builder;
	
	
	private static final String CACHE_NAME = "cache1";
	
	// Implementing cache
	// Key - XML file from request
	// Value - extracted action
	
	//1. Create a cache manager
	static CacheManager cm = CacheManager.getInstance();
	static {
		cm.addCache(CACHE_NAME);
	}
	
	static void maybeShutdownCache(){
		cm.shutdown();
	}
	
	// Cache is thread safe. 
	// http://www.ehcache.org/apidocs/2.9/net/sf/ehcache/Cache.html
	
	
	public XmlParser(){
	}
	
	public String getFromCache(String key){
		Cache cache = cm.getCache(CACHE_NAME);
		Element el = cache.get(key);
		if (el != null) {
			return el.getObjectValue().toString();
		} else {
			return null;
		}
	}
	
	public void setXmlCacheEnabled (String enabled){
		this.xmlCacheEnabled = Boolean.valueOf(enabled);
	}
	
	public Boolean isXmlCacheEnabled (){
		return this.xmlCacheEnabled;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	private static XPath getXPath(){
		if (xPath == null){
			synchronized (XPath.class){
				if (xPath == null){
					createXPath();
				}
			}
		} 
		return xPath;
	}
	
	private static void createXPath(){
		xPath = getXPathFactory().newXPath();
	}
	
	public String getPath (){
		return path;
	}
	
	private static DocumentBuilder getDocumentBuilder(){
		if (builder == null){
			synchronized (DocumentBuilder.class){
				if (builder == null){
					createDocumentBuilder();
				}
			}
		} 
		return builder;
	}
 	
	private static void createDocumentBuilder(){
		try {
			builder = getDocumentBuilderFactory().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static DocumentBuilderFactory getDocumentBuilderFactory(){
		if (builderFactory == null){
			synchronized (DocumentBuilderFactory.class){
				if (builderFactory == null){
					createDocumentBuilderFactory();
				}
			}
		}
		return builderFactory;
	}
 	
	private static void createDocumentBuilderFactory(){
		builderFactory = DocumentBuilderFactory.newInstance();
	}
	
	private static XPathFactory getXPathFactory(){
		if (xPathFactory == null){
			synchronized (XPathFactory.class){
				if (xPathFactory == null){
					createXPathFactory();
				}
			}
		}
		return xPathFactory;
	}
	
	private static void createXPathFactory(){
		xPathFactory = XPathFactory.newInstance();
	}
	
	// this method requires thread safety
	public String extractContentOfElement(String xmlDoc){
		String contentOfElement = null;
		
		Boolean cacheEnabled = isXmlCacheEnabled();
		
		/*An XPath expression is not thread-safe and not reentrant. 
		 * In other words, it is the application's responsibility to make sure that 
		 * one XPathExpression object is not used from more than one thread at any given time, 
		 * and while the evaluate method is invoked, applications may not recursively call the 
		 * evaluate method. */
		
		String valueFromCache = null;
		if (cacheEnabled) {
			valueFromCache = getFromCache(xmlDoc);
		}
		
		if (valueFromCache != null) {
			System.out.println("cache hit " + valueFromCache);
			return valueFromCache;
		}
				
		try {
			Document xmlDocument = null;
			try {
				synchronized(DocumentBuilder.class) {
					xmlDocument = getDocumentBuilder().parse(new ByteArrayInputStream(xmlDoc.getBytes()));
				}
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			synchronized (XPath.class) {
				contentOfElement = getXPath().compile(path).evaluate(xmlDocument);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			contentOfElement = null;
		}
		
		//saving in cache
		if (cacheEnabled) {
			if (contentOfElement != null) {
				//System.out.println("cache put " + contentOfElement);
				cm.getCache(CACHE_NAME).put(new Element(xmlDoc, contentOfElement));
			}
		}
		return contentOfElement;
	}
	
}
