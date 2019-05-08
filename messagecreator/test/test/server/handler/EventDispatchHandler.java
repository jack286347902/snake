package test.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import test.disruptor.DQEventProcessor;

public class EventDispatchHandler extends ChannelInboundHandlerAdapter {
	
	public static final Logger logger = LogManager.getLogger("EventDispatchHandler");
	
	public static final DQEventProcessor EVENT_PROCESSOR = DQEventProcessor.getInstance();
	
	
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	try {
	    	
	    	EVENT_PROCESSOR.enqueue(ctx.channel(), 0, (ByteBuf)msg);
	    		    	    	
    	} finally {
    		ReferenceCountUtil.release(msg);
    	}  
    	
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }

}
