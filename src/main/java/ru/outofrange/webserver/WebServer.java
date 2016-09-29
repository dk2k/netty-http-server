package ru.outofrange.webserver;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import org.jboss.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import ru.outofrange.config.Constant;
import ru.outofrange.config.PropReader;
import ru.outofrange.service.ActionEntityServiceImpl;
import ru.outofrange.service.ActionEntityServiceInterface;
import ru.outofrange.service.RequestEntityServiceImpl;
import ru.outofrange.service.RequestEntityServiceInterface;
import ru.outofrange.xml.XmlParser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class WebServer {
	
	private static final int defaultPort = 8100;

	private static Logger log = Logger.getLogger(WebServer.class);
	
	public static void main(String[] args) {
		
		// this call causes lots of noise in the console
		//BasicConfigurator.configure();
		
		// loading the definitions from the given XML file
		ApplicationContext context = new ClassPathXmlApplicationContext(
						"applicationContext.xml");
		
		
		PropReader propReader = (PropReader) context
				.getBean("configService");
		propReader.parse();
		
		String portFromConfig = propReader.getProperty(Constant.PORT);
		int localPort;
		try {
			localPort = Integer.valueOf(portFromConfig);
		} catch (NumberFormatException exc) {
			localPort = defaultPort;
		}
		
		//Executor threadPool = Executors.newCachedThreadPool();
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(), 
				Executors.newCachedThreadPool())
		);
		
		String uri = propReader.getProperty(Constant.URI);
		
		// already configured in the applicationContext.xml
		XmlParser parser = (XmlParser) context
				.getBean("parsingService");
		

		// configuring access to DB
		ActionEntityServiceInterface actionService = (ActionEntityServiceImpl) context
				.getBean("actionService");
		RequestEntityServiceInterface requestService = (RequestEntityServiceImpl) context
				.getBean("requestService");
		
        // creating a pipelineFactory 
        bootstrap.setPipelineFactory(new WebServerPipelineFactory(uri, parser, actionService, requestService));
        
		// starting web server
		bootstrap.bind(new InetSocketAddress(localPort));
		
		// first dummy request in order to invoke lazy init and
		// to have no delay for the first business request
		actionService.getResponseForAction("dummy_string");
		
		System.out.println("XmlParser's cache is enabled: " + parser.isXmlCacheEnabled());
		System.out.println("Listening on port " + localPort);
		//log.warn("Listening on port " + localPort);
		
	}
	
}
