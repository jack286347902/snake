package org.snake.centerserver.command.server.netty.server.handler;

import java.util.List;

import org.snake.centerserver.command.server.manager.connection.CommandConnectionManager;
import org.snake.message.event.CommandMessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class CommandServerEventDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		while (in.isReadable(CommandMessageEvent.CLIENT_SIZE_LENGTH)) {
			
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
    	
    	CommandConnectionManager.getInstance().removeConnection(ctx.channel());
    	
        cause.printStackTrace();
        ctx.close();
    }

}
