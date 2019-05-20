package org.snake.transfer.client.handler;

import org.snake.transfer.server.handler.UserInfoHandler;
import org.snake.user.UserInfo;
import org.snake.user.map.TransferChannelToUserInfo;
import org.snake.user.netty.exception.channel.close.ReleaseChannel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientTransferHandler extends ChannelInboundHandlerAdapter {

	private TransferChannelToUserInfo transferChannelToUserInfo = TransferChannelToUserInfo.getInstance();
	
	private ReleaseChannel releaseChannel = ReleaseChannel.getInstance();
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
    	UserInfo userInfo = transferChannelToUserInfo.get(ctx.channel());
    	
    	UserInfoHandler.transferClientLogin(userInfo);
    	
        ctx.fireChannelActive();
    }

	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
					
		transferChannelToUserInfo.get(ctx.channel())
								 .getClientChannel()
								 .writeAndFlush(msg);

    }
    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        
        releaseChannel.transferChannelClose(ctx.channel());
    }
}
