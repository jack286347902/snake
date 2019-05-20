package test.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import test.client.Client;
import test.client.handler.crypt.ByteValidate;
import test.client.handler.crypt.Crypt;

public class EncryptHandler extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		int messageOffset = msg.arrayOffset() + msg.readerIndex();

		byte validate = ByteValidate.xor(msg.array(), messageOffset, msg.readableBytes());
		
		msg.writeByte(validate);
		
		Crypt crypt = Client.getCrypt();
		crypt.encryptSend(msg.array(), messageOffset, msg.readableBytes());
			
		out.writeBytes(msg);
		
		
	}

	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        ctx.close();
    }
}
