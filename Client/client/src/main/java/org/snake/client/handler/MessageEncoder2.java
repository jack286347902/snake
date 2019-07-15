package org.snake.client.handler;


import org.snake.message.Message;
import org.snake.message.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import test.client.handler.crypt.ByteValidate;

public class MessageEncoder2 extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		
		ByteBuf buf = ctx.alloc().heapBuffer();
		
		MessageEvent.messageToServer(msg, buf);
				
		out.writeBytes(buf);
	}

	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
       
        ctx.close();
    }
}
