package test.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import test.client.handler.ClientEventDispatchHandler;
import test.client.handler.EventDecoder;
import test.client.handler.EventEncoder;



public class ClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("EventDecoder", new EventDecoder());
		pipeline.addLast("EventEncoder", new EventEncoder());
		
		pipeline.addLast("ClientEventDispatchHandler", new ClientEventDispatchHandler());
		
		
	}
}
