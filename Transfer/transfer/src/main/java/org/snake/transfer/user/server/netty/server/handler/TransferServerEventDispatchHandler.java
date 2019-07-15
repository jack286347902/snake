package org.snake.transfer.user.server.netty.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snake.message.event.MessageEvent;
import org.snake.transfer.user.server.netty.disruptor.DQEventProcessor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class TransferServerEventDispatchHandler extends ChannelInboundHandlerAdapter {
	
	public static final Logger logger = LogManager.getLogger("EventDispatchHandler");
	
	public static final DQEventProcessor EVENT_PROCESSOR = DQEventProcessor.getInstance();
	
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
//    	try {
	    	
//	    	EVENT_PROCESSOR.enqueue(ctx.channel(), (ByteBuf)msg);
    		
    		
        	ByteBuf buf = (ByteBuf)msg;
        	
        	MessageEvent event = new MessageEvent();
        	
        	event.parseFromClient(buf);
        	
//        	System.err.println(COUNT.getAndIncrement());
        	
        	ByteBuf cbuf = ctx.alloc().heapBuffer();
        	event.messageToClient(cbuf);
    		
    		ctx.writeAndFlush(cbuf);
	    		    		    	    	
//    	} finally {
//    		ReferenceCountUtil.release(msg);
//    	}  
    	
    }
    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }

}
