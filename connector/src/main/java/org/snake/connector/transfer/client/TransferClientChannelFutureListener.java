package org.snake.connector.transfer.client;

import org.snake.connector.transfer.user.UserInfo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class TransferClientChannelFutureListener implements ChannelFutureListener {
	
	private UserInfo userInfo;
	
	public TransferClientChannelFutureListener(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		// TODO Auto-generated method stub

		Channel channel = null;
		if(future.isSuccess()) {
			
			channel = future.channel();
			
			channel.attr(UserInfo.ATTR_KEY).set(userInfo);
			
			userInfo.setTransferChannel(channel);
			
		} else {
			
			Throwable cause = future.cause();
            if (cause != null) 
                throw new Exception(cause);
        	        	
            Channel clientChannel = userInfo.getClientChannel();
            if(null != clientChannel 
            		&& clientChannel.isActive())
            	clientChannel.close();
            
            userInfo.setClientChannel(null);
            userInfo.setTransferChannel(null);
		}
		
	}

}
