package org.snake.command.client;


import org.snake.testmessage.event.MessageEvent;
import org.snake.testmessage.login.ConnectorIPPort;
import org.snake.testmessage.pool.MessagePool;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


@Component
public class CommandClient {
	
	public static final int SINGLE_EVENT_LOOP_GROUP_THREADS = 1;
	
	private EventLoopGroup singleEventLoopGroup = new NioEventLoopGroup(SINGLE_EVENT_LOOP_GROUP_THREADS);
	
	public void connect(String ip, int port) {
		
		Bootstrap bootstrap = new Bootstrap();
		
    	try {

    		
    		bootstrap.group(singleEventLoopGroup)
    			.channel(NioSocketChannel.class)
    			.handler(new CommandClientInitializer())
    			.option(ChannelOption.TCP_NODELAY, true);
    		
    		bootstrap.connect(ip, port).sync();
    		    		
        	
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
        	
        } finally {

        }
		
	}
	

	

	public void shutdown() {
		
		singleEventLoopGroup.shutdownGracefully();
		
	}

     
    
    
	
}
