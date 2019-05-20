package test.client.handler.copy;


import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import test.client.ClientMessageEvent;
import test.client.copy.ClientMessageEvent2;

public class EventEncoder2 extends MessageToByteEncoder<ClientMessageEvent2> {

	public static final Logger logger = LogManager.getLogger("EventEncoder");
		
	@Override
	protected void encode(ChannelHandlerContext ctx, ClientMessageEvent2 msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub

		try {
			
			ByteBuf buf = msg.array(ctx);
			
			out.writeBytes(buf);
			
			buf.release();
			
		
		} catch (Exception e) {
						
			e.printStackTrace();
			
			ctx.close();
		}
		
//		System.err.println("	c 	e: " + COUNTER.getAndIncrement() + "	" + out.readableBytes());
	}
	
	public static final AtomicInteger COUNTER = new AtomicInteger(0);
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }


}
