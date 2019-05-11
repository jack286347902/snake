package test.server.handler;


import java.util.concurrent.atomic.AtomicInteger;

import org.snake.testmessage.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerEventEncoder extends MessageToByteEncoder<MessageEvent> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEvent msg, ByteBuf out) throws Exception {
		
		
		try {
			
			// default 256 bytes
			msg.array(out);
		
//			msg.getMessage().release();
		
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ctx.close();
		}
	}
	
	public static final AtomicInteger COUNTER = new AtomicInteger(0);

	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }
}
