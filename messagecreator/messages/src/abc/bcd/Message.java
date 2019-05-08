package abc.bcd;


import abc.bcd.pool.KeyedMessagePool;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class Message implements Cmd, ReferenceCount {

	protected static final KeyedMessagePool MESSAGE_POOL 
							= KeyedMessagePool.getInstance();
	


	/*
	 * parse message from ByteBuf
	 * 
	 * @param ByteBuf PooledByteBufAllocator.directBuffer()
	 */
	public abstract void parse(ByteBuf buf) throws Exception;


	/*
	 * convert messsage to ByteBuf, auto release() message
	 * 
	 * @param ByteBuf PooledByteBufAllocator.directBuffer()
	 */
	public abstract void array(ByteBuf buf)  throws Exception;
	
	/*
	 * convert message to ByteBuf
	 * 
	 * @return Unpooled.directBuffer()
	 */
	public ByteBuf array() throws Exception {
		
		ByteBuf buf = Unpooled.directBuffer();
		
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
	public synchronized void release() {
		
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

	
	protected String readString(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();
		byte[] array  = new byte[len];
		buf.readBytes(array);
		
		return new String(array, "UTF-8");
	}
	
	protected String[] readStringArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();
		
		String[] result = new String[len];
		for(int i = 0; i < len; ++i) {
			result[i] = readString(buf);
		}
		
		return result;
	}
	
	protected void writeString(String s, ByteBuf buf) throws Exception {
		
		int len = s.length();
		buf.writeShort(len);
		if(len > 0)
			buf.writeBytes(s.getBytes("UTF-8"));
	}
	
	protected void writeStringArray(String[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		
		for(String s: array)
			writeString(s, buf);
		
	}
	
	protected boolean[] readBooleanArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();
		
		boolean[] result = new boolean[len];
		for(int i = 0; i < len; ++i) {
			result[i] = buf.readBoolean();
		}
		
		return result;
	}
	
	protected void writeBooleanArray(boolean[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		for(boolean value: array)
			buf.writeBoolean(value);
		
	}
	
	protected byte[] readByteArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();
		
		byte[] result = new byte[len];
		
		if(len > 0)
			buf.readBytes(result);
		
		return result;
	}
	
	protected void writeByteArray(byte[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		
		if(array.length > 0)
			buf.writeBytes(array);
		
	}
	
	protected short[] readShortArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();
		
		short[] result = new short[len];
		for(int i = 0; i < len; ++i) {
			result[i] = buf.readShort();
		}
		
		return result;
	}
	
	protected void writeShortArray(short[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		for(short value: array)
			buf.writeShort(value);
		
	}
	
	protected int[] readIntArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();
		
		int[] result = new int[len];
		for(int i = 0; i < len; ++i) {
			result[i] = buf.readInt();
		}
		
		return result;
	}
	
	protected void writeIntArray(int[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		for(int value: array)
			buf.writeInt(value);
		
	}
	
	protected long[] readLongArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();
		
		long[] result = new long[len];
		for(int i = 0; i < len; ++i) {
			result[i] = buf.readLong();
		}
		
		return result;
	}
	
	protected void writeLongArray(long[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		for(long value: array)
			buf.writeLong(value);
		
	}
	
	protected float[] readFloatArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();

		float[] result = new float[len];
		for(int i = 0; i < len; ++i) {
			result[i] = buf.readFloat();
		}
		
		return result;
	}
	
	protected void writeFloatArray(float[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
		
		buf.writeShort(array.length);
		for(float value: array)
			buf.writeFloat(value);
		
	}
	
	protected double[] readDoubleArray(ByteBuf buf) throws Exception {
		
		short len = buf.readShort();

		double[] result = new double[len];
		for(int i = 0; i < len; ++i) {
			result[i] = buf.readDouble();
		}
		
		return result;
	}
	
	protected void writeDoubleArray(double[] array, ByteBuf buf) throws Exception {
		
		if(null == array) {
			
			buf.writeShort(0);
			return;
		}
			
		
		buf.writeShort(array.length);
		for(double value: array)
			buf.writeDouble(value);
		
	}
}
