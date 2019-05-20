package test.client;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.snake.testmessage.Message;
import org.snake.testmessage.login.ClientLogin;
import org.snake.testmessage.m1.Item;
import org.snake.testmessage.m1.Small;
import org.snake.testmessage.m2.FirstRequest;
import org.snake.testmessage.m3.SecondRequest;
import org.snake.testmessage.pool.MessagePool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import test.client.handler.crypt.ByteValidate;
import test.client.handler.crypt.Crypt;

public class ClientMessageEvent {
	
	
	public static final int CLIENT_SIZE_LENGTH = 2;
	// SIZE 2 CMD 2
	public static final int CLIENT_HEADER_LENGTH = 4;
	
	public static final int SERVER_SIZE_LENGTH = 4;
	// SIZE 4 CMD 2
	public static final int SERVER_HEADER_LENGTH = 6;
	
	public static final int VALIDATE_LENGTH = 1;
	
	public static final int SERVER_EXTRA_LENGTH = SERVER_HEADER_LENGTH + VALIDATE_LENGTH;
	
	
	private Channel channel;
	private Message message;


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
	   


	// Exception: message recycled by gc
	public ByteBuf array(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		
		int totalLen = message.getSize() + CLIENT_HEADER_LENGTH + VALIDATE_LENGTH;
	
		ByteBuf buf = ctx.alloc().heapBuffer(totalLen);
	
		buf.writeShort(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);
		
		if(ClientLogin.CMD_INT == message.getCmdInt()) {
			
			buf.writeByte(0);
			
			return buf;
		}
		
		encrypt(buf);
		
		System.err.println("	c	a " + COUNTER2.getAndIncrement() 
		+ "	" + message.getSize() + " " + buf.readableBytes());
			
		
		message.release();
		
		return buf;
	}
	
	private void encrypt(ByteBuf msg) {
		
		int messageOffset = msg.arrayOffset() + msg.readerIndex();

		byte validate = ByteValidate.xor(msg.array(), messageOffset, msg.readableBytes());
		
		msg.writeByte(validate);
		
		System.err.println(validate);
		
		Crypt crypt = Client.getCrypt();
		crypt.encryptSend(msg.array(), messageOffset, msg.readableBytes());

	}
	
	
	public static final AtomicInteger COUNTER2 = new AtomicInteger(0);
	

	
}
