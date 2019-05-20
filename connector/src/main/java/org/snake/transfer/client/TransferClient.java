package org.snake.transfer.client;


import org.snake.user.UserInfo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;



public class TransferClient {
	
	
	public static boolean connect(UserInfo userInfo) {
		// TODO Auto-generated method stub
		
    	try {

    		Bootstrap bootstrap = new Bootstrap();
    		bootstrap.group(userInfo.getEventLoop())
    			.channel(NioSocketChannel.class)
    			.handler(new TransferClientInitializer())
    			.option(ChannelOption.TCP_NODELAY, true);
    		
    		String ip = userInfo.getTransferIp();
    		int port = userInfo.getTransferPort();
    		
    		ChannelFuture f = bootstrap.connect(ip, port);

			userInfo.setTransferChannel(f.channel());
    		
    		return true;
	
        	
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
        } 
    	
    	return false;
		
	}
	
	
}
