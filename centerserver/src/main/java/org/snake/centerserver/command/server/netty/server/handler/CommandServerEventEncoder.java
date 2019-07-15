package org.snake.centerserver.command.server.netty.server.handler;


import org.snake.centerserver.command.server.manager.connection.CommandConnectionManager;
import org.snake.message.Message;
import org.snake.message.event.CommandMessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CommandServerEventEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		
		
		try {
			
			// default 256 bytes
			CommandMessageEvent.messageToClient(msg, out);
		
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ctx.close();
		}
	}
	
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	CommandConnectionManager.getInstance().removeConnection(ctx.channel());
    	
        cause.printStackTrace();
        ctx.close();
    }
}
