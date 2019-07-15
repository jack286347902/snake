package org.snake.message.event;


import org.snake.message.Message;
import org.snake.message.pool.MessagePool;


import java.io.UnsupportedEncodingException;

import org.snake.message.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class CommandMessageEvent {

	
	public static final int CLIENT_SIZE_LENGTH = 2;
	// SIZE 2 CMD 2
	public static final int CLIENT_HEADER_LENGTH = 4;
	
	public static final int SERVER_SIZE_LENGTH = 4;
	// SIZE 4 CMD 2
	public static final int SERVER_HEADER_LENGTH = 6;

	
	// HEADER + VALIDATE
	public static final int SERVER_EXTRA_LENGTH = SERVER_HEADER_LENGTH;
	// HEADER + VALIDATE
	public static final int CLIENT_EXTRA_LENGTH = CLIENT_HEADER_LENGTH;
	
	public static final int VALIDATE = 0;
	
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
	 *  connector send to logic server:
	 *  parse message from directbuf
	 *  message structure:
	 * 			short size
	 * 			short cmd
	 * 			byte[] data -> message
	 * 			byte validate
	 *  
	 *  @param buf directbuf from netty
	 *  
	 */
	public void parseFromClient(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(CLIENT_SIZE_LENGTH);
		short cmd = buf.readShort();

		message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
		buf.release();
		
	}
	
	public static Message parseMessageFromClient(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(CLIENT_SIZE_LENGTH);
		short cmd = buf.readShort();

		Message message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
		buf.release();
		
		return message;
	}
	
	public void parseFromServer(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(SERVER_SIZE_LENGTH);
		short cmd = buf.readShort();

		message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
		buf.release();
	}

	public static Message parseMessageFromServer(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(SERVER_SIZE_LENGTH);
		short cmd = buf.readShort();

		Message message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
		buf.release();
		
		return message;
	}


	/*
	 *  logic server send to connector:
	 *  write message to directorbuf
	 *  message structure:
	 * 			int size
	 * 			short cmd
	 * 			byte[] data -> message
	 * 			byte validate
	 *  
	 *  @param buf directbuf from netty
	 *  
	 */
	public void messageToClient(ByteBuf out) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		messageToClient(message, out);
		
	}
	
	public static void messageToClient(Message message, ByteBuf buf) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		int totalLen = message.getSize() + SERVER_EXTRA_LENGTH;
		
		if(!buf.isWritable(totalLen))
			buf.ensureWritable(totalLen);
		
		buf.writeInt(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);
		
		message.release();
		
		
	}
	
	/*
	 *  logic server send to connector:
	 *  write message to directorbuf
	 *  message structure:
	 * 			int size
	 * 			short cmd
	 * 			byte[] data -> message
	 * 			byte validate
	 *  
	 *  @param buf directbuf from netty
	 *  
	 */
	public void messageToServer(ByteBuf buf) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		messageToServer(message, buf);
		
	}
	
	public static void messageToServer(Message message, ByteBuf buf) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		int totalLen = message.getSize() + CLIENT_EXTRA_LENGTH;
		
		if(!buf.isWritable(totalLen))
			buf.ensureWritable(totalLen);
		
		buf.writeShort(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);
		
		// if calls int message.array(buf)
		// every inner message calls it's release
		message.release();
		
	}
	
}