package org.snake.testmessage.event;


import org.snake.testmessage.Message;
import org.snake.testmessage.pool.MessagePool;


import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class MessageEvent {

	
	public static final int CMD_OFFSET = 4;
	
	public static final int HEADER_LENGTH = 6;
	public static final int UUID_LENGTH = 8;
	
	private Channel channel;
	private Message message;
	private long uuid;

	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	
	/*
	 *  client send and receive data with connector:
	 *  parse message from directbuf
	 *  message structure:
	 * 			short size
	 * 			short cmd
	 * 			byte[] data -> message
	 *  
	 *  @param buf directbuf from netty
	 *  
	 */
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(CMD_OFFSET);
		short cmd = buf.readShort();

		message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
	}

	
	/*
	 *  connecter send and receive data from other server:
	 *  parse message from directbuf
	 *  message structure:
	 * 			short size
	 * 			short cmd
	 * 			byte[] data -> message
	 *  
	 *  @param buf directbuf from netty
	 *  
	 */
//	public void parseWithUuid(ByteBuf buf) throws Exception {
//		
//		try {
//		
//			buf.skipBytes(CMD_OFFSET);
//			short cmd = buf.readShort();
//			buf.skipBytes(HASH_LENGTH);
//			
//			message = MessagePool.borrowMessage(cmd);
//			
//			message.parse(buf);
//			
//			uuid = buf.readLong();
//			
//		} finally {
//			buf.release();
//		}
//	}

	
	private byte dataHash(int dataIndex, int dataSize, ByteBuf buf) {
		
		byte hash = 0;
		
		for(int i = 0; i < dataSize; ++i)
			hash ^= buf.getByte(dataIndex++);
		
		return hash;
		
	}

	// Exception: message recycled by gc
	public void array(ByteBuf buf) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		
		// this line to next 5 unempty line must here, 
		// can't move to message.array(buf)
		// why??????????????????????????????????????
		// 
		// cacl total length if in message.array(buf), 
		// every inner message recacl it's length
		// length changes again and again
		int totalLen = message.getSize() + HEADER_LENGTH;
		
		if(!buf.isWritable(totalLen))
			buf.ensureWritable(totalLen);
		
		buf.writeInt(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);

		// next unempty line must here, 
		// can't move to message.array(buf)
		// why??????????????????????????????????????
		
		
		// if calls int message.array(buf)
		// every inner message calls it's release
		message.release();
		

	}
	
	
	
	// Exception: message recycled by gc
//	public void arrayWithUuid(ByteBuf buf) throws Exception {
//
//		int headIndex = buf.writerIndex();
//		
//		buf.writeBytes(ZERO_HEADER);
//		
//		int dataIndex = buf.writerIndex();
//		message.array(buf);
//		int dataSize = buf.writerIndex() - dataIndex;
//		
//		buf.writeLong(uuid);
//		
//		int size = buf.writerIndex() - headIndex;
//		
//		buf.setShort(headIndex, size);
//		buf.setShort(headIndex + CMD_OFFSET, message.getCmdShort());
//		buf.setByte(headIndex + DATA_HASH_OFFSET, dataHash(dataIndex, dataSize, buf));
//		
//		message.release();
//	}

	
	
}
