package ru.outofrange.model;

import java.io.Serializable;

/*import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;*/

public class ActionEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 993269108415593695L;

	private Long id;
    
    private String action;
    
    private String response;
    
    public void setId(Long id){
    	this.id = id;
    }
	
    public void setAction(String action){
    	this.action = action;
    }
    
    public void setResponse(String response){
    	this.response = response;
    }
    
    public String getAction(){
    	return this.action;
    }
    
    public String getResponse(){
    	return this.response;
    }
    
    public Long getId(){
    	return this.id;
    }
    
    @Override
    public String toString(){
    	return "Action id: "+ id + " action: " + action + " response: " + response;
    }
}
