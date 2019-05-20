package org.snake.transfer.server.handler;


import org.snake.user.map.ClientChannelToUserInfo;
import org.snake.user.netty.exception.channel.close.ReleaseChannel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConnectorTransferHandler extends ChannelInboundHandlerAdapter {


	private ClientChannelToUserInfo clientChannelToUserInfo = ClientChannelToUserInfo.getInstance();


	private ReleaseChannel releaseChannel = ReleaseChannel.getInstance();
	
	
	

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		

		clientChannelToUserInfo.get(ctx.channel())
				   			   .getTransferChannel()
				   			   .writeAndFlush(msg);


    }
	

    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();

        releaseChannel.clientChannelClose(ctx.channel());
    }
}
