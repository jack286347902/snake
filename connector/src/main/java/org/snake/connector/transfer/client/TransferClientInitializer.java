package org.snake.connector.transfer.client;

import org.snake.connector.transfer.client.handler.TransferClientEventDecoder;
import org.snake.connector.transfer.client.handler.TransferClientHandler;
import org.snake.connector.transfer.client.handler.TransferClientHeartBeatHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;



public class TransferClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
//		pipeline.addLast("TransferClientEventDecoder", new TransferClientEventDecoder());
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(100, 0, 0));
		pipeline.addLast("TransferClientHeartBeatHandler", new TransferClientHeartBeatHandler());
		
		pipeline.addLast("TransferClientHandler", new TransferClientHandler());
		
		
	}
}
