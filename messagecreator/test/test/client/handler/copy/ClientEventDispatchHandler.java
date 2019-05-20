package test.client.handler.copy;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import test.client.ClientMessageEvent;


public class ClientEventDispatchHandler extends ChannelInboundHandlerAdapter {
		
	public static final Logger logger = LogManager.getLogger("DQEventProcessor");
  
    
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    		
	    	ByteBuf buf = (ByteBuf)msg;
	    	
	    	ClientMessageEvent event = new ClientMessageEvent();
	    	
	    	event.parse(buf);
	    		    	
	    	buf.release();

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }

}
