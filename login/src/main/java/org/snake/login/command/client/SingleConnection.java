package org.snake.login.command.client;



import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.snake.login.command.client.manager.connection.CommandConnectionManager;
import org.snake.zookeeper.manager.zookeeper.ServerData;
import org.snake.zookeeper.manager.zookeeper.ServerDataManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class SingleConnection {
	
	public static final AttributeKey<SingleConnection> ATTR_KEY 
							= AttributeKey.valueOf("SingleConnection");
	
	private Channel channel;
	private ServerData serverData;
	
	private Bootstrap bootstrap;
	
	public SingleConnection(ServerData serverData) {
		this.serverData = serverData;
	}

	private void init(EventLoopGroup eventLoopGroup) {
		
		bootstrap = new Bootstrap();

		bootstrap.group(eventLoopGroup)
			.channel(NioSocketChannel.class)
			.handler(new CommandClientInitializer())
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.remoteAddress(serverData.getIp(), serverData.getCommandPort());
	}
	
	public synchronized void doConnect(EventLoopGroup eventLoopGroup) {
		
//		ChannelFuture future = 
//		bootstrap.connect()
//				 .sync();
		
		if(!ServerDataManager.getInstance().contains(serverData))
			return;
		
		if(CommandConnectionManager.getInstance().hasActiveConnection(serverData))
			return;
		
		if(null == bootstrap)
			init(eventLoopGroup);
		
		ChannelFuture future = null;
		try {
			
			future = bootstrap.connect().sync();
			
			channel = future.channel();
			
			channel.attr(ATTR_KEY).set(this);
			
			CommandConnectionManager.getInstance().addConnection(this);
			
			log.info(new Date() + ": 客户端连接成功, 属性: {}", serverData);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
        	if(null != channel) {
        		
        		CommandConnectionManager.getInstance().removeConnection(channel);
        		
        		channel = null;
        	}
        	
        	if(serverData.isRemoved()) {
        		
        		ServerDataManager.getInstance().remove(serverData);
        		            		
        		return;
        	
        	}
        	
        	log.info("与服务端断开连接!在10s之后准备尝试重连!");
        	eventLoopGroup.schedule(() -> doConnect(eventLoopGroup), 10, TimeUnit.SECONDS);
			
		}
		
//		bootstrap.connect().addListener((ChannelFuture futureListener) -> {
//            
//			boolean initFalg = true;
//			final EventLoop eventLoop = futureListener.channel().eventLoop();
//			
//        	if(null != channel) {
//        		
//        		CommandConnectionManager.getInstance().removeConnection(channel);
//        		
//        		channel = null;
//        	}
//        	
//            if (!futureListener.isSuccess()) {
//            	
//            	if(serverData.isRemoved()) {
//            		
//            		ServerDataManager.getInstance().remove(serverData);
//            		            		
//            		return;
//            	
//            	}
//
//                log.info("与服务端断开连接!在10s之后准备尝试重连!");
//                eventLoop.schedule(() -> doConnect(eventLoop), 60, TimeUnit.SECONDS);
//
//                initFalg = false;
//            } else {
//                initFalg = true;
//            }
//            
//            if (initFalg) {
//            	
//                log.info(new Date() + ": 客户端连接成功, 属性: {}", serverData);
//                
//                channel = futureListener.channel();
//                
//                channel.attr(ATTR_KEY).set(this);
//                
//        		CommandConnectionManager.getInstance().addConnection(this);
//
//            }
//
//        });

	}
	
	public void reConnect() {
		
		CommandConnectionManager.getInstance().removeConnection(channel);
		
		final EventLoop eventLoop = channel.eventLoop();
		
		channel = null;
		
        log.info("与服务端断开连接!在10s之后准备尝试重连!");
        eventLoop.schedule(() -> doConnect(eventLoop), 10, TimeUnit.SECONDS);
	}
	
}
