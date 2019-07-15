package org.snake.client;

import org.snake.client.handler.ClientEventDispatchHandler;
import org.snake.client.handler.MessageDecoder;
import org.snake.client.handler.MessageEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;




public class ClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
				
//		pipeline.addLast("DecryptHandler", new DecryptHandler());
		
		pipeline.addLast("MessageDecoder", new MessageDecoder());
		pipeline.addLast("MessageEncoder", new MessageEncoder());
		
//		pipeline.addLast("MessageEncoder2", new MessageEncoder2());
		
		pipeline.addLast("ClientEventDispatchHandler", new ClientEventDispatchHandler());
		
		
	}
}
