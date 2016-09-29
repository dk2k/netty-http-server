package ru.outofrange.config;

import java.io.InputStream;  
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service("configService")
public class PropReader {

	private Properties prop;
	private String filename;
	
    public PropReader()  
    {  
        prop = new Properties();
    }
    
    /*public PropReader(String filename)  
    {  
        this();
        this.filename = filename;
    }*/
    
    public void setFilename (String filename){
    	this.filename = filename;
    }
    
    /*public Properties getProperties(){
    	return prop;
    }*/
    
    
    public void parse(){
    	
    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
    	try {
    	    if (inputStream != null) {
    		    prop.load(inputStream);
    	    } else {
    	    	throw new FileNotFoundException("property file '" + filename + "' not found in the classpath");
    	    }
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    public Set<String> stringPropertyNames(){
    	return prop.stringPropertyNames();
    }
    
    public String getProperty(String key){
    	return prop.getProperty(key);
    }
    
}

