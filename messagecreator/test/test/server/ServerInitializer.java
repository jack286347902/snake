package test.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import test.server.handler.ServerEventDecoder;
import test.server.handler.EventDispatchHandler;
import test.server.handler.ServerEventEncoder;
import test.server.handler.HeartBeatServerHandler;


public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(1800, 0, 0));
		pipeline.addLast("HeartBeatServerHandler", new HeartBeatServerHandler());

		pipeline.addLast("EventDecoder", new ServerEventDecoder());
		pipeline.addLast("EventEncoder", new ServerEventEncoder());
		
		pipeline.addLast("EventDispatchHandler", new EventDispatchHandler());
		
	}

}
