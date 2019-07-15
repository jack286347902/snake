package org.snake.login.command.client.handler;

import java.util.List;

import org.snake.login.command.client.SingleConnection;
import org.snake.message.event.CommandMessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class CommandEventDecoder extends ByteToMessageDecoder {
		
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		while (in.isReadable(CommandMessageEvent.SERVER_SIZE_LENGTH)) {
			
			// 标记读位置
			in.markReaderIndex();
			
			int size = in.readInt();
			
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
    	
    	
//    	ctx.channel().attr(SingleConnection.ATTR_KEY).get().reConnect();
    	
        cause.printStackTrace();
        ctx.close();
        
    }

}
