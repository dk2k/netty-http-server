package ru.outofrange.webserver;

import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;

import ru.outofrange.service.ActionEntityServiceInterface;
import ru.outofrange.service.RequestEntityServiceInterface;
import ru.outofrange.xml.XmlParser;

//this class handles requests from the client
public class WebServerPipelineFactory implements ChannelPipelineFactory {

	private static String uri = null;
	private static XmlParser parser;
	private static ActionEntityServiceInterface actionService;
	private static RequestEntityServiceInterface requestService;
	
	public WebServerPipelineFactory(String uri, XmlParser parser, 
			ActionEntityServiceInterface actionService, RequestEntityServiceInterface requestService){
		this.uri = uri;
		this.parser = parser;
		this.actionService = actionService;
		this.requestService = requestService;
	}
	
    //@Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = Channels.pipeline();
        p.addLast("decoder", new HttpRequestDecoder());
        p.addLast("aggregator", new HttpChunkAggregator(1048576));
        p.addLast("encoder", new HttpResponseEncoder());
        // that's our custom handler
        p.addLast("handler", new WebServerRequestHandler(uri, parser, actionService, requestService));
        return p;
    }
	
}
