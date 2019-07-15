package org.snake.transfer.user.server.netty.server;

import org.snake.transfer.user.server.netty.server.handler.TransferServerEventDecoder;
import org.snake.transfer.user.server.netty.server.handler.TransferServerEventDispatchHandler;
import org.snake.transfer.user.server.netty.server.handler.TransferServerEventEncoder;
import org.snake.transfer.user.server.netty.server.handler.TransferServerHeartBeatHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;


public class TransferServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(600, 0, 0));
		pipeline.addLast("TransferServerHeartBeatHandler", new TransferServerHeartBeatHandler());

		pipeline.addLast("TransferServerEventDecoder", new TransferServerEventDecoder());
		pipeline.addLast("TransferServerEventEncoder", new TransferServerEventEncoder());
		
		pipeline.addLast("TransferServerEventDispatchHandler", new TransferServerEventDispatchHandler());
		
	}

}
