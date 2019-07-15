package test.client.handler;


import org.snake.message.Message;
import org.snake.message.event.MessageEvent;
import org.snake.message.login.ClientLogin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import test.client.Client;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		
//		ByteBuf buf = ctx.alloc().heapBuffer(msg.readableBytes());
		
		ByteBuf buf = ctx.alloc().heapBuffer();
		
		MessageEvent.messageToServer(msg, buf);
		
		if(msg.getCmdInt() != ClientLogin.CMD_INT) {
			
			int messageOffset = buf.arrayOffset() + buf.readerIndex();
			
			Crypt crypt = ctx.channel()
					         .attr(Client.ATTR_KEY)
					         .get();
			
			crypt.encryptSend(buf.array(), messageOffset, buf.readableBytes());
			
		}
		
		
		out.writeBytes(buf);
		
		buf.release();
		
	}

	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
       
        ctx.close();
    }
}
