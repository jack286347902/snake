package org.snake.connectorlogincener.logincenter;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class LoginCenterServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("MessageDecoder", new MessageDecoder());
		pipeline.addLast("MessageEncoder", new MessageEncoder());
		
		pipeline.addLast("ProcessHandler", new ProcessHandler());
	}

}
