package org.snake.transfer.server;

import org.snake.transfer.server.handler.HeartBeatServerHandler;
import org.snake.transfer.server.handler.UserInfoHandler;
import org.snake.transfer.server.handler.MessageDecoder;
import org.snake.transfer.server.handler.MessageEncoder;
import org.snake.transfer.server.handler.ConnectorTransferHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;


public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(1800, 0, 0));
		pipeline.addLast("HeartBeatServerHandler", new HeartBeatServerHandler());

		pipeline.addLast("UserInfoHandler", new UserInfoHandler());
		
		pipeline.addLast("MessageDecoder", new MessageDecoder());
		pipeline.addLast("MessageEncoder", new MessageEncoder());
		
		pipeline.addLast("ConnectorTransferHandler", new ConnectorTransferHandler());
		
	}

}
