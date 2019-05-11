package test.server.handler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ServerEventDecoder extends ByteToMessageDecoder {
	
	public static final Logger logger = LogManager.getLogger("EventDispatchHandler");
	
	
	public static final int SIZE_LENGTH = 4;
	
	
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1,
			List<Object> arg2) throws Exception {
				
		ByteBuf object = decode(arg0, arg1);
		
		if(null != object)
			arg2.add(object);

	}
	
	/**
	 * 
	 * @param ctx
	 * @param buf
	 * @return
	 * @throws Exception
	 * 
	 * Messsage format:
	 * 		    uint16 size;
	 * 			byte[] date;
	 * 
	 */
	
	private ByteBuf decode(ChannelHandlerContext ctx, ByteBuf buf) 
			throws Exception {

		if(buf.isReadable(SIZE_LENGTH)) {
			
			// 标记读位置
			buf.markReaderIndex();
			
			int size = buf.readInt();
			
			buf.resetReaderIndex();
			
			if(buf.isReadable(size)) 
				return buf.readRetainedSlice(size);
			
//			if(buf.isReadable(size)) {
//				
//				ByteBuf heapBuf = ctx.alloc().heapBuffer(size);
//				
//				buf.readBytes(heapBuf, size);
//				
//				return heapBuf;
//				
//				
//			}

		}
				
		return null;

	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        ctx.close();
    }

}
