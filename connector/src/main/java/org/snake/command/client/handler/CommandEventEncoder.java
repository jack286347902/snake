package org.snake.command.client.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snake.testmessage.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class CommandEventEncoder extends MessageToByteEncoder<MessageEvent> {

	public static final Logger logger = LogManager.getLogger("EventEncoder");
		
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEvent msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub

		try {
			
			// default 256 bytes
			msg.arrayToServer(out);
			
		
		} catch (Exception e) {
						
			e.printStackTrace();
			
			ctx.close();
		}
		
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }


}
