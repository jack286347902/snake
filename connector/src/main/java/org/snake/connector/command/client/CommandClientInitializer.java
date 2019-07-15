package org.snake.connector.command.client;

import org.snake.connector.command.client.handler.CommandEventDecoder;
import org.snake.connector.command.client.handler.CommandEventDispatchHandler;
import org.snake.connector.command.client.handler.CommandEventEncoder;
import org.snake.connector.command.client.handler.CommandEventHeartBeatHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;



public class CommandClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(1, 0, 0));
		pipeline.addLast("CommandEventHeartBeatHandler", new CommandEventHeartBeatHandler());
		
		pipeline.addLast("CommandEventDecoder", new CommandEventDecoder());
		pipeline.addLast("CommandEventEncoder", new CommandEventEncoder());
		
		pipeline.addLast("CommandEventDispatchHandler", new CommandEventDispatchHandler());
		
		
	}
}
