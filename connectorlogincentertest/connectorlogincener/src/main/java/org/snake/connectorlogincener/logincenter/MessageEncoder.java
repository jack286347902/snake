package org.snake.connectorlogincener.logincenter;


import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snake.testmessage.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class MessageEncoder extends MessageToByteEncoder<MessageEvent> {

	public static final Logger logger = LogManager.getLogger("EventEncoder");
		
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEvent msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub

		try {
			
			msg.arrayToClient(ctx, out);
			
//			msg.getMessage().release();
		
		} catch (Exception e) {
						
			e.printStackTrace();
			
			ctx.close();
		}
		
//		System.err.println("	c 	e: " + COUNTER.getAndIncrement() + "	" + out.readableBytes());
	}
	
	public static final AtomicInteger COUNTER = new AtomicInteger(0);
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }


}
