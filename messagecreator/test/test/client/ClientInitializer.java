package test.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import test.client.handler.ClientEventDispatchHandler;
import test.client.handler.MessageDecoder;
import test.client.handler.MessageEncoder;
import test.client.handler.MessageEncoder2;



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
