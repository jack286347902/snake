package test.client.handler;


import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.snake.testmessage.Message;
import org.snake.testmessage.m1.Item;
import org.snake.testmessage.m1.Small;
import org.snake.testmessage.m2.FirstRequest;
import org.snake.testmessage.m3.SecondRequest;
import org.snake.testmessage.pool.MessagePool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class ClientMessageEvent {

	
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
	public void parse(ByteBuf buf) throws Exception {
		
		int size = buf.readInt();
		
//		buf.skipBytes(CMD_OFFSET);
		short cmd = buf.readShort();

		message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
			
		System.err.println("	c	p 	" + COUNTER.getAndIncrement()
				+ "	" + message.getSize() + " " + size);


		print();
	}
	
	
	
	private void print() {
		
		StringBuilder result = new StringBuilder();
		
		switch (message.getCmdInt()) {
		case Item.CMD_INT:
			
			Item item = (Item)message;
			
			result.append(" id: ")
				  .append(item.getId())
				  .append(" name: ")
				  .append(item.getName().substring(0, 10));
			
			break;
			
		case Small.CMD_INT:
			
			Small small = (Small)message;
					
			result.append(" id: ")
			  		.append(small.getId());
			break;
					
		case FirstRequest.CMD_INT:
			
			FirstRequest first = (FirstRequest)message;
			result.append(" FirstRequest id: ")
			  .append(first.getAi32())
			  .append(" aitem name: ")
			  .append(first.getAitem().getName().substring(0, 10));
			
			break;
			
		case SecondRequest.CMD_INT:
			
			SecondRequest second = (SecondRequest)message;
			
			result.append(" id: ")
			  .append(second.getAi32())
			  .append(" name: ")
			  .append(second.getAname().substring(0, 10))
			  .append(" aitem name: ")
			  .append(second.getAitem().getName().substring(0, 10));
			  
			List<FirstRequest> fRequest  = second.getFRequest();
			
			for(FirstRequest r: fRequest) {
				
				
				result.append(" FirstRequest id: ")
					  .append(r.getAi32())
					  .append(" aitem name: ")
					  .append(r.getAitem().getName().substring(0, 10));
				
			}

			break;

		default:
			break;
		}
		
		System.err.println(result.toString());

	}
	
	public static AtomicInteger COUNTER = new AtomicInteger(0);
	   
	
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
	public void array(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub

		int totalLen = message.getSize() + HEADER_LENGTH;

		if(!buf.isWritable(totalLen))
			buf.ensureWritable(totalLen);
		
		buf.writeInt(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);
		
//		System.err.println("	c	a " + COUNTER2.getAndIncrement() 
//		+ "	" + message.getSize() + " " + buf.readableBytes());
//			
		
		message.release();
	}
	
	
	public ByteBuf array() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		

		int totalLen = message.getSize() + HEADER_LENGTH;
		
		ByteBuf buf = Unpooled.directBuffer(totalLen);
		
		buf.writeInt(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);

		message.release();
		
		return buf;
	}
	
	
	public static final AtomicInteger COUNTER2 = new AtomicInteger(0);
	
	
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
