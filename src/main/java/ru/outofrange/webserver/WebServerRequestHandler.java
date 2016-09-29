package ru.outofrange.webserver;

import java.net.*;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.*;

import java.nio.charset.Charset;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpResponseDecoder;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;

import ru.outofrange.service.ActionEntityServiceInterface;
import ru.outofrange.service.RequestEntityServiceInterface;
import ru.outofrange.xml.XmlParser;

@Sharable
public class WebServerRequestHandler extends SimpleChannelUpstreamHandler {

	private static AtomicLong requestCount = new AtomicLong(0);
	
	private volatile Channel _proxyServerChanel;
	
	private String uri = null;
	private XmlParser parser;
	private ActionEntityServiceInterface actionService;
	private RequestEntityServiceInterface requestService;
	
	public WebServerRequestHandler(String uri, XmlParser parser, 
			ActionEntityServiceInterface actionService, RequestEntityServiceInterface requestService){
		this.uri = uri;
		this.parser = parser;
		this.actionService = actionService;
		this.requestService = requestService;
	}
	
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	
    	Date currentDate = new Date();
    	
        HttpRequest request = (HttpRequest) e.getMessage();
        
        this._proxyServerChanel = e.getChannel();
 
        long currRequestValue = requestCount.incrementAndGet();
        if (currRequestValue % 100 == 0) {
            // reporting every 100th request
            System.out.println("Num Of HTTP Requests: " + currRequestValue);
        }
 
        if (request.getProtocolVersion() == HttpVersion.HTTP_1_0) {
        	request = convertRequestTo1_1(request);
        }
 
        String remoteHost = parseSenderIP(e.getChannel().getRemoteAddress().toString());
 
        HttpMethod method =	request.getMethod();
        
        String errorResp = "";
        String responseFromDB = null;
        
        if (method != HttpMethod.POST) {
        	errorResp += " Only POST requests supported! You sent [" + method.toString() + "]";
        	// can't exit early here since we need to log the request
        }
        
        String requestedURI = request.getUri();
        String action = null;
        
        String requestBodyAsString = "";

        // if we have correct URL and there were no errors previously
        if (requestedURI.equals(uri) && errorResp.equals("")) {
        	ChannelBuffer requestBody = request.getContent();
        	requestBodyAsString = requestBody.toString(Charset.forName("UTF-8"));
			
			if (requestBodyAsString != null && !requestBodyAsString.equals("")) {
				// pass this string into xml parser and get requested action
				action = parser.extractContentOfElement(requestBodyAsString);
			}

	        if (action == null || action.equals("")){
	        	errorResp += " Couldn't extract action from this XML! Is it malformed?";
	        } else {
	        	// TODO
	        	responseFromDB = actionService.getResponseForAction(action);
	        	if (responseFromDB == null) {
	        		// no response found in DB for this extracted content
	        		errorResp += " No response found in DB for this action: [" + action + "]";
	        	}
	        }
        } else if (!requestedURI.equals(uri)) {
        	errorResp += " Unsupported URI! You requested [" + requestedURI + "], the server listens [" + uri + "]";
        }
        
        // saving request's data in DB
        requestService.logRequest(currentDate, remoteHost, method.toString(), requestBodyAsString, action, responseFromDB);
        
        if (errorResp != null && !errorResp.equals("")){
        	// sending error message
        	writeResponseAndClose(createErrorResponse(errorResp));
        } else {
        	// sending a corresponding message from DB
        	 writeResponseAndClose(createXMLResponse(responseFromDB));   	
        }
    }
    
    private void writeResponseAndClose(HttpResponse response) {
		if (response != null) {
           response.setHeader("Connection", "close");
           this._proxyServerChanel.write(response).addListener(ChannelFutureListener.CLOSE);
		} 
		
		else {
	        this._proxyServerChanel.close();
	    }
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.err.println("Unexpected exception from proxy server handler: " + e.getCause());
        e.getCause().printStackTrace();
        
        e.getChannel().close();
    }
	
    // converts HTTP 1.0 requests into HTTP 1.1
    private HttpRequest convertRequestTo1_1(HttpRequest request) throws URISyntaxException {
        DefaultHttpRequest newReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, request.getMethod(), request.getUri());
        if (!request.getHeaderNames().isEmpty()) {
            for (String name : request.getHeaderNames()) {
                newReq.setHeader(name, request.getHeaders(name));
            }
        }
        if (!newReq.containsHeader(HttpHeaders.Names.HOST)) {
            URI url = new URI(newReq.getUri());
            String host = url.getHost();
            if (url.getPort() != -1) {
                host += ":" + url.getPort();
            }
            newReq.setHeader(HttpHeaders.Names.HOST, host);
        }
        newReq.setContent(request.getContent());
        return newReq;
    }
    
 	/**
 	 * 
 	 * This method extracts the sender IP from remote address
 	 * @param senderIP remote address
 	 * @return String representation in form *.*.*.*
 	 * 
 	 */
 	private String parseSenderIP(String senderIP){
 		
 		String tAnswer = ""; 
 		String tDigits = "0123456789";
 		byte [] tSenderIP = senderIP.getBytes(Charset.defaultCharset());
 		int tSenderIPLength = tSenderIP.length;
 		
 		for(int j = 0; j < tSenderIPLength; j++){
 			if((char)tSenderIP[j]== ':')
 				break;
 			
 			if((char)tSenderIP[j]== '.'  | tDigits.indexOf((char)tSenderIP[j]) != -1 )
 				tAnswer = tAnswer.concat(String.valueOf((char)tSenderIP[j]));
 		}
 		return tAnswer;
 	}
 
 	// we know for sure that responses are well formed XML, hence can reflect that in the header
    public static HttpResponse createXMLResponse(String body) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/xhtml+xml; charset=utf-8");
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(body, Charset.forName("UTF-8"));
        response.setContent(buf);
        response.setHeader("Content-Length", String.valueOf(buf.readableBytes()));
        return response;
    }
 	
    // creates an http response with a specified text 
    public static HttpResponse createErrorResponse(String errorText) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");
        ChannelBuffer buf = ChannelBuffers.copiedBuffer("<html><body><h3>" + errorText + "</h3></body></html>", Charset.forName("UTF-8"));
        response.setContent(buf);
        response.setHeader("Content-Length", String.valueOf(buf.readableBytes()));
        return response;
    }
}
