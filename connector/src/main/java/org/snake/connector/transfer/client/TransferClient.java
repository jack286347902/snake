package org.snake.connector.transfer.client;


import java.util.Date;

import org.snake.connector.transfer.user.UserInfo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.log4j.Log4j2;


@Data
@Log4j2
public class TransferClient {

	
	public static void doConnect(UserInfo userInfo, EventLoopGroup eventLoopGroup) {
		
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(eventLoopGroup)
			.channel(NioSocketChannel.class)
			.handler(new TransferClientInitializer())
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.remoteAddress(userInfo.getTransferIp(), userInfo.getTransferPort());
		
//		Channel channel = null;
		try {
			
			ChannelFuture future = bootstrap.connect();
			
			future.addListener(new TransferClientChannelFutureListener(userInfo));
			
//			channel = future.channel();
//			
//			channel.attr(UserInfo.ATTR_KEY).set(userInfo);
//			
//			userInfo.setTransferChannel(channel);
//			
//			log.info(new Date() + ": 客户端连接成功, 属性: {}", userInfo);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
//        	if(null != channel) {	
//        	
//        		channel.close();
//        		channel = null;
//        		
//        	}
//        	        	
//            Channel transferChannel = userInfo.getTransferChannel();
//            if(null != transferChannel 
//            		&& transferChannel.isActive())
//            	transferChannel.close();
//            
//            userInfo.setClientChannel(null);
//            userInfo.setTransferChannel(null);

		}
		
	}
	

	
}
