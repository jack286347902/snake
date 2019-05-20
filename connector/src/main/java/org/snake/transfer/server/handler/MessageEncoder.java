package org.snake.transfer.server.handler;


import org.snake.transfer.server.handler.crypt.Crypt;
import org.snake.user.UserInfo;
import org.snake.user.map.ClientChannelToUserInfo;
import org.snake.user.netty.exception.channel.close.ReleaseChannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import test.client.handler.crypt.ByteValidate;

public class MessageEncoder extends MessageToByteEncoder<ByteBuf> {

	private ClientChannelToUserInfo clientChannelToUserInfo = ClientChannelToUserInfo.getInstance();
	
	private ReleaseChannel releaseChannel = ReleaseChannel.getInstance();
	
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		
		ByteBuf buf = ctx.alloc().heapBuffer(msg.readableBytes());
		
		msg.readBytes(buf);
		
		int messageOffset = buf.arrayOffset() + buf.readerIndex();
//
//		byte validate = ByteValidate.xor(buf.array(), messageOffset, buf.readableBytes() - 1);
//		
//		buf.setByte(buf.readableBytes() - 1, validate);
		
		UserInfo userInfo = clientChannelToUserInfo.get(ctx.channel());
		Crypt crypt = userInfo.getCrypt();
		
		crypt.encryptSend(buf.array(), messageOffset, buf.readableBytes());
		
		out.writeBytes(buf);
		
		buf.release();
		
	}

	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
       
        releaseChannel.clientChannelClose(ctx.channel());
    }
}
