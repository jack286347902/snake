package org.snake.connectorlogincener.logincenter;

import org.snake.testmessage.Message;
import org.snake.testmessage.event.MessageEvent;
import org.snake.testmessage.login.ConnectorIPPort;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ProcessHandler extends ChannelInboundHandlerAdapter {


	private IpPortToChannel ipPortToChannel = IpPortToChannel.getInstance();

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			
		Message message = MessageEvent.parseMessageFromClient((ByteBuf)msg);
		
		if(ConnectorIPPort.CMD_INT == message.getCmdInt()) {
			
			ConnectorIPPort connectorIPPort = (ConnectorIPPort)message;
			
			String ipPort = new String(connectorIPPort.getConnectorIPPort().getBytes(), "UTF-8");
			
			ipPortToChannel.add(ipPort, ctx.channel());
		}
		
		

    }
    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        ctx.close();
    }
}
