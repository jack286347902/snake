package test.client.handler;


import abc.bcd.event.MessageEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class EventEncoder extends MessageToByteEncoder<MessageEvent> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEvent msg, ByteBuf out) throws Exception {

		msg.array(out);
		
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
        cause.printStackTrace();
        ctx.close();
    }
}
