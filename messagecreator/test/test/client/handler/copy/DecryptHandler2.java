package test.client.handler.copy;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import test.client.Client;
import test.client.ClientMessageEvent;
import test.client.copy.Client2;
import test.client.handler.crypt.ByteValidate;
import test.client.handler.crypt.Crypt;

public class DecryptHandler2 extends ByteToMessageDecoder {
	

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		
		while (in.isReadable(ClientMessageEvent.SERVER_SIZE_LENGTH)) {
			// 标记读位置
			in.markReaderIndex();
			
			ByteBuf sizeBuf = ctx.alloc().heapBuffer(ClientMessageEvent.SERVER_SIZE_LENGTH);
			
			in.readBytes(sizeBuf);
			
			in.resetReaderIndex();
			
			Crypt crypt = Client2.getCrypt();
			
			crypt.saveRecvIndex();
			           
		    int sizeOffset = sizeBuf.arrayOffset() + sizeBuf.readerIndex();

			crypt.decryptRecv(sizeBuf.array(), sizeOffset, ClientMessageEvent.SERVER_SIZE_LENGTH);
			
			crypt.resetRecvIndex();
			
			int size = sizeBuf.readInt();
			
			sizeBuf.release();
			
			if(in.isReadable(size)) {
				
				ByteBuf messsageBuf = ctx.alloc().heapBuffer(size);
				
				in.readBytes(messsageBuf);
				
			    int messageOffset = messsageBuf.arrayOffset() + messsageBuf.readerIndex();

				crypt.decryptRecv(messsageBuf.array(), messageOffset, size);
				
				byte validate = ByteValidate.xor(messsageBuf.array(), messageOffset, size - 1);
				
				if(validate != messsageBuf.getByte(size - 1)) {
					
					ctx.channel().close();
				}
				
				out.add(messsageBuf);
				
			} else {
				break;
			}
			
		}
		
	}
	


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        ctx.close();
    }
}
