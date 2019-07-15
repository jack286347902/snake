package org.snake.connector.transfer.client.handler;

import org.snake.connector.transfer.user.UserInfo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TransferClientHandler extends ChannelInboundHandlerAdapter {
		
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	ctx.channel()
	       .attr(UserInfo.ATTR_KEY)
	       .get()
	       .getClientChannel()
	       .writeAndFlush(msg);

    }
    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        
        UserInfo userInfo = ctx.channel()
				    	       .attr(UserInfo.ATTR_KEY)
				    	       .get();

        
        Channel transferChannel = userInfo.getTransferChannel();
        if(null != transferChannel 
        		&& transferChannel.isActive())
        	transferChannel.close();
        
        // send error to client and center server
        ctx.close();
        
        userInfo.setClientChannel(null);
        userInfo.setTransferChannel(null);
    }
}
