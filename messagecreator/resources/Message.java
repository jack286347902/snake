
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class Message implements Cmd, ReferenceCount {
	
	protected static final KeyedMessagePool MESSAGE_POOL 
							= KeyedMessagePool.getInstance();
	
	public static final int ENUM_LENGTH = 4;
	
	public abstract int getSize();

	/*
	 * parse message from ByteBuf
	 * 
	 * @param ByteBuf PooledByteBufAllocator.directBuffer()
	 */
	public abstract void parse(ByteBuf buf) throws UnsupportedEncodingException;


	/*
	 * convert messsage to ByteBuf
	 * 
	 * @param ByteBuf PooledByteBufAllocator.directBuffer()
	 */
	public abstract void array(ByteBuf buf)  throws UnsupportedEncodingException;
	
	/*
	 * convert messsage to ByteBuf
	 * 
	 * @return ByteBuf Unpooled.directBuffer(getSize())
	 */
	public ByteBuf array() throws UnsupportedEncodingException {
		
		ByteBuf buf = Unpooled.directBuffer(getSize());
		
		array(buf);
		
		return buf;
		
	}
	
	
	private final AtomicInteger refCnt = new AtomicInteger(0);
	
	@Override
	public void retain() {

		refCnt.incrementAndGet();
		retainMessage();
	}

	/*
	 * refCnt >= 0
	 */
	@Override
	public void release() {
		
		if(refCnt.get() > 0) {
			
			refCnt.decrementAndGet();
			releaseMessage();
		}
	}
	
	protected int getRefCnt() {
		return refCnt.get();
	}

	/*
	 * return message to message pool
	 * 
	 * 1. if refCnt > 0, --refCnt, minus reference count of submessages
	 * 		if not return to pool, collect by gc
	 * 2. if refCnt == 0, return message to pool, 
	 * 		clear list messages,
	 * 		submessage = null
	 * 
	 */
	protected abstract void releaseMessage();
	
	/*
	 * add reference count of submessages
	 * if message == null, get message
	 */
	protected abstract void retainMessage();

	
	protected String readString(ByteBuf buf) throws UnsupportedEncodingException {
		
		short len = buf.readShort();
		
		if(0 == len)
			return null;
		
		byte[] array  = new byte[len];
		buf.readBytes(array);
		
		return new String(array, "UTF-8");
	}
	
	protected String[] readStringArray(ByteBuf buf) throws UnsupportedEncodingException {
		
		short len = buf.readShort();
		
		if(0 == len)
			return null;
		
		String[] result = new String[len];
		for(int i = 0; i < len; ++i) {
			result[i] = readString(buf);
		}
		
		return result;
	}
	
	protected void writeString(String s, ByteBuf buf) throws UnsupportedEncodingException {
		
		if(null == s) {
			
			buf.writeShort(0);
			return;
		}
		
		int len = s.length();
		buf.writeShort(len);
		if(len > 0)
			buf.writeBytes(s.getBytes("UTF-8"));
	}
	
	protected void writeStringArray(String[] array, ByteBuf buf) throws UnsupportedEncodingException {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		
		for(String s: array)
			writeString(s, buf);
		
	}
	

}
