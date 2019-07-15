package org.snake.centerserver.command.server.netty.server.handler;


import org.snake.centerserver.command.server.manager.connection.CommandConnectionManager;
import org.snake.centerserver.command.server.netty.disruptor.CommandEventProcessor;
import org.snake.centerserver.command.zookeeper.ServersImpl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


public class CommandServerEventDispatchHandler extends ChannelInboundHandlerAdapter {
	

	private CommandEventProcessor commandEventProcessor 
					= ServersImpl.getInstance().getCommandEventProcessor();
	
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
//    	try {
	    	
    	commandEventProcessor.enqueue(ctx.channel(), (ByteBuf)msg);
	    		    		    	    	
//    	} finally {
//    		ReferenceCountUtil.release(msg);
//    	}  
    	
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	
    	CommandConnectionManager.getInstance().removeConnection(ctx.channel());
    	
    	ctx.fireChannelInactive();
    };
    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	CommandConnectionManager.getInstance().removeConnection(ctx.channel());
    	
        cause.printStackTrace();
        ctx.close();
    }

}
