package org.snake.transfer.server.handler;

import java.util.List;

import org.snake.testmessage.event.MessageEvent;
import org.snake.transfer.server.handler.crypt.ByteValidate;
import org.snake.transfer.server.handler.crypt.Crypt;
import org.snake.user.UserInfo;
import org.snake.user.map.ClientChannelToUserInfo;
import org.snake.user.netty.exception.channel.close.ReleaseChannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {
	
	private ClientChannelToUserInfo clientChannelToUserInfo = ClientChannelToUserInfo.getInstance();
	private ReleaseChannel releaseChannel = ReleaseChannel.getInstance();
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
				
		while (in.isReadable(MessageEvent.CLIENT_SIZE_LENGTH)) {
			// 标记读位置
			in.markReaderIndex();
			
			UserInfo userInfo = clientChannelToUserInfo.get(ctx.channel());
			
			ByteBuf sizeBuf = userInfo.getSizeBuf();
			
			in.readBytes(sizeBuf);
			
			in.resetReaderIndex();
			
			Crypt crypt = userInfo.getCrypt();
			
			crypt.saveRecvIndex();
			
			int sizeOffset = sizeBuf.arrayOffset() + sizeBuf.readerIndex();

			crypt.decryptRecv(sizeBuf.array(), sizeOffset, MessageEvent.CLIENT_SIZE_LENGTH);
			
			crypt.resetRecvIndex();
			
			int size = sizeBuf.readShort();
			
			if(in.isReadable(size)) {
				
				ByteBuf messsageBuf = ctx.alloc().heapBuffer(size);
				
				in.readBytes(messsageBuf);
				
			    int messageOffset = messsageBuf.arrayOffset() + messsageBuf.readerIndex();

				crypt.decryptRecv(messsageBuf.array(), messageOffset, size);
				
				if(ByteValidate.xor(messsageBuf.array(), messageOffset, size - 1) 
						!= messsageBuf.getByte(size - 1)) {
					
					ctx.channel().close();
				}
				
				out.add(messsageBuf);
				
			} else {
				break;
			}
			
		}

	}
	


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        
        releaseChannel.clientChannelClose(ctx.channel());
    }

}
