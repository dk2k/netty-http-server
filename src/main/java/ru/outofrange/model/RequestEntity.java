package ru.outofrange.model;

import java.io.Serializable;
import java.util.Date;

public class RequestEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4994566110829176839L;

	private Long id;
    
    private String requestBody;
    
    private Date date;
    
    private String remoteHost;
    
    private String method;
    
    public RequestEntity(){
    	
    }
    
    public RequestEntity(Date date, 
    					String remoteHost,
    					String method,
    					String requestBody, 
    					String extractedContent, 
    					String responseFromDB){
    	this.date = date;
    	this.extractedContent = extractedContent;
    	this.remoteHost = remoteHost;
    	this.requestBody = requestBody;
    	this.method = method;
    }
    
    // we have this redundancy intentionally: the app is for testing purposes
    // and it's good to see what was extracted since we have already carried out this action
    private String extractedContent;
    
    public void setId(Long id){
    	this.id = id;
    }
    
    public Long getId(){
    	return this.id;
    }
    
    public void setRequestBody(String requestBody){
    	this.requestBody = requestBody;
    }
    
    public void setDate(Date date){
    	this.date = date;
    }
    
    public void setRemoteHost(String remoteHost){
    	this.remoteHost = remoteHost;
    }
    
    public void setMethod(String method){
    	this.method = method;
    }
    
    public void setExtractedContent(String extractedContent){
    	this.extractedContent = extractedContent;
    }
    
    public String getRequestBody(){
    	return this.requestBody;
    }
    
    public Date getDate(){
    	return this.date;
    }
    
    public String getRemoteHost(){
    	return this.remoteHost;
    }
    
    public String getMethod(){
    	return this.method;
    }
    
    public String getExtractedContent(){
    	return this.extractedContent;
    }
    
    @Override
    public String toString(){
    	return "Request id: "+ id + " date: " + date + " method: " + method + " requestBody: " + requestBody + " extractedContent: " + extractedContent + " remoteHost: " + remoteHost;
    }
	
}
