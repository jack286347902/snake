package org.snake.client.handler;



import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snake.message.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


public class ClientEventDispatchHandler extends ChannelInboundHandlerAdapter {
		
	public static final Logger logger = LogManager.getLogger("DQEventProcessor");
  
	public static final AtomicInteger COUNT = new AtomicInteger(0);
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    		
	    	ByteBuf buf = (ByteBuf)msg;
	    	
	    	MessageEvent event = new MessageEvent();
	    	
	    	event.parseFromServer(buf);
	    	
	    	if(COUNT.getAndIncrement() > 99998)
	    		System.err.println(System.currentTimeMillis());

	    		    	

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }

}
