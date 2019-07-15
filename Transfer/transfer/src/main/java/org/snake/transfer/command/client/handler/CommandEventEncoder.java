package org.snake.transfer.command.client.handler;

import org.snake.message.Message;
import org.snake.message.event.CommandMessageEvent;
import org.snake.transfer.command.client.SingleConnection;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class CommandEventEncoder extends MessageToByteEncoder<Message> {
		
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub

		try {
			
			// default 256 bytes
			CommandMessageEvent.messageToServer(msg, out);
			
		
		} catch (Exception e) {
						
			e.printStackTrace();
			
			ctx.close();
		}
		
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
//    	ctx.channel().attr(SingleConnection.ATTR_KEY).get().reConnect();
    	
        cause.printStackTrace();
        ctx.close();
    }


}
