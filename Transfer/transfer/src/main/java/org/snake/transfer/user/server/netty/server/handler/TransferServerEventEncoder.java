package org.snake.transfer.user.server.netty.server.handler;



import org.snake.message.Message;
import org.snake.message.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TransferServerEventEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		
		
		try {
			
			// default 256 bytes
			MessageEvent.messageToClient(msg, out);
		
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
