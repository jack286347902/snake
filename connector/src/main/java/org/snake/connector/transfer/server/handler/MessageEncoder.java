package org.snake.connector.transfer.server.handler;


import org.snake.connector.transfer.server.handler.crypt.Crypt;
import org.snake.connector.transfer.user.UserInfo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		
		ByteBuf buf = ctx.alloc().heapBuffer(msg.readableBytes());
		
		msg.readBytes(buf);
		
		int messageOffset = buf.arrayOffset() + buf.readerIndex();
		
		Crypt crypt = ctx.channel()
				         .attr(UserInfo.ATTR_KEY)
				         .get()
				         .getCrypt();
		
		crypt.encryptSend(buf.array(), messageOffset, buf.readableBytes());
		
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
