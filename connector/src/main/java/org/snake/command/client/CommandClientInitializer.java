package org.snake.command.client;

import org.snake.command.client.handler.CommandClientDecoder;
import org.snake.command.client.handler.CommandClientHandler;
import org.snake.command.client.handler.CommandEventEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;



public class CommandClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		
		pipeline.addLast("CommandClientDecoder", new CommandClientDecoder());
		pipeline.addLast("CommandEventEncoder", new CommandEventEncoder());
		
		pipeline.addLast("CommandClientHandler", new CommandClientHandler());
		
		
	}
}
