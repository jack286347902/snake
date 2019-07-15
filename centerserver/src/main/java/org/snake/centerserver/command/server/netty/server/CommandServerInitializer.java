package org.snake.centerserver.command.server.netty.server;

import org.snake.centerserver.command.server.netty.server.handler.CommandServerEventDecoder;
import org.snake.centerserver.command.server.netty.server.handler.CommandServerEventDispatchHandler;
import org.snake.centerserver.command.server.netty.server.handler.CommandServerEventEncoder;
import org.snake.centerserver.command.server.netty.server.handler.CommandServerHeartBeatHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;


public class CommandServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(10, 0, 0));
		pipeline.addLast("CommandServerHeartBeatHandler", new CommandServerHeartBeatHandler());

		pipeline.addLast("CommandServerEventDecoder", new CommandServerEventDecoder());
		pipeline.addLast("CommandServerEventEncoder", new CommandServerEventEncoder());
		
		pipeline.addLast("CommandServerEventDispatchHandler", new CommandServerEventDispatchHandler());
		
	}

}
