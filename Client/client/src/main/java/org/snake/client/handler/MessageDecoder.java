package org.snake.client.handler;

import java.util.List;

import org.snake.client.Client;
import org.snake.message.event.MessageEvent;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


public class MessageDecoder extends ByteToMessageDecoder {
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
				
		while (in.isReadable(MessageEvent.SERVER_SIZE_LENGTH)) {
			// 标记读位置
			in.markReaderIndex();
			
			Crypt crypt = ctx.channel()
			         .attr(Client.ATTR_KEY)
			         .get();
			
			ByteBuf sizeBuf = ctx.alloc().heapBuffer(MessageEvent.SERVER_SIZE_LENGTH);
			
			in.readBytes(sizeBuf);
			
			in.resetReaderIndex();
						
			crypt.saveRecvIndex();
			
			int sizeOffset = sizeBuf.arrayOffset() + sizeBuf.readerIndex();

			crypt.decryptRecv(sizeBuf.array(), sizeOffset, MessageEvent.SERVER_SIZE_LENGTH);
			
			crypt.resetRecvIndex();
			
			int size = sizeBuf.readInt();
			
			sizeBuf.release();
			
			if(in.isReadable(size)) {
				
				ByteBuf messsageBuf = ctx.alloc().heapBuffer(size);
				
				in.readBytes(messsageBuf);
				
			    int messageOffset = messsageBuf.arrayOffset() + messsageBuf.readerIndex();

				crypt.decryptRecv(messsageBuf.array(), messageOffset, size);
				
				if(ByteValidate.xor(messsageBuf.array(), messageOffset, size - 1) 
						!= messsageBuf.getByte(size - 1)) {
					
					ctx.close();
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
        
        ctx.close();
    }

}
