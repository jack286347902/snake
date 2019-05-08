package test.server.handler;


import abc.bcd.event.MessageEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerEventEncoder extends MessageToByteEncoder<MessageEvent> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEvent msg, ByteBuf out) throws Exception {
		
		// default 256 bytes
		msg.array(out);
		
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }
}
