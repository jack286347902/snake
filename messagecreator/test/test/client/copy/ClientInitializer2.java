package test.client.copy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import test.client.handler.ClientEventDispatchHandler;
import test.client.handler.DecryptHandler;
import test.client.handler.EncryptHandler;
import test.client.handler.EventDecoder;
import test.client.handler.EventEncoder;
import test.client.handler.copy.DecryptHandler2;
import test.client.handler.copy.EventEncoder2;



public class ClientInitializer2 extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
				
		pipeline.addLast("DecryptHandler", new DecryptHandler2());
		
//		pipeline.addLast("EventDecoder", new EventDecoder());
		pipeline.addLast("EventEncoder", new EventEncoder2());
		
		pipeline.addLast("ClientEventDispatchHandler", new ClientEventDispatchHandler());
		
		
	}
}
