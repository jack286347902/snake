package test.client.handler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import abc.bcd.efg.Item;
import abc.bcd.efgh.AReq;
import abc.bcd.event.MessageEvent;
import abc.bcd.pool.MessagePool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


public class ClientEventDispatchHandler extends ChannelInboundHandlerAdapter {
		
	public static final Logger logger = LogManager.getLogger("DQEventProcessor");
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    		
//    	writeMessage(ctx);

    }
    

    
    public static int COUNTER = 0;
    
    
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	try {
    		
	    	ByteBuf buf = (ByteBuf)msg;
	    	
	    	MessageEvent event = new MessageEvent();
	    	
	    	event.parse(buf);
	    	
//	    	logger.error("receive:" + COUNTER++);
	    	    	
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
