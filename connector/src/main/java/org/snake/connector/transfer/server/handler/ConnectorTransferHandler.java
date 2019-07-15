package org.snake.connector.transfer.server.handler;


import java.util.concurrent.atomic.AtomicInteger;

import org.snake.connector.transfer.user.UserInfo;
import org.snake.message.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConnectorTransferHandler extends ChannelInboundHandlerAdapter {

	public static final AtomicInteger COUNT = new AtomicInteger();
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ctx.channel()
	       .attr(UserInfo.ATTR_KEY)
	       .get()
	       .getTransferChannel()
	       .writeAndFlush(msg);
		
		
//    	ByteBuf buf = (ByteBuf)msg;
//    	
//    	MessageEvent event = new MessageEvent();
//    	
//    	event.parseFromClient(buf);
//    	
////    	System.err.println(COUNT.getAndIncrement());
//    	
//    	ByteBuf cbuf = ctx.alloc().heapBuffer();
//    	event.messageToClient(cbuf);
//		
//		ctx.writeAndFlush(cbuf);
		
    }
	

    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();

        ctx.close();
    }
}
