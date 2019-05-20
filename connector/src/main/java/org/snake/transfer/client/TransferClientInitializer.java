package org.snake.transfer.client;

import org.snake.transfer.client.handler.ClientTransferHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;



public class TransferClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("ClientTransferHandler", new ClientTransferHandler());
		
		
	}
}
