package org.snake.connectorlogincener.logincenter;

import java.util.List;

import org.snake.testmessage.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {
	
	
		@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		while (in.isReadable(MessageEvent.CLIENT_SIZE_LENGTH)) {
			
			// 标记读位置
			in.markReaderIndex();
			
			int size = in.readShort();
			
			in.resetReaderIndex();
			
			if(in.isReadable(size)) 
				out.add(in.readRetainedSlice(size));
			else 
				break;
		}

	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        ctx.close();
    }

}
