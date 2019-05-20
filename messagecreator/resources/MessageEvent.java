
import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import test.client.handler.crypt.ByteValidate;

public class MessageEvent {

	
	public static final int CLIENT_SIZE_LENGTH = 2;
	// SIZE 2 CMD 2
	public static final int CLIENT_HEADER_LENGTH = 4;
	
	public static final int SERVER_SIZE_LENGTH = 4;
	// SIZE 4 CMD 2
	public static final int SERVER_HEADER_LENGTH = 6;
	public static final int VALIDATE_LENGTH = 1;
	
	// HEADER + VALIDATE
	public static final int SERVER_EXTRA_LENGTH = SERVER_HEADER_LENGTH + VALIDATE_LENGTH;
	// HEADER + VALIDATE
	public static final int CLIENT_EXTRA_LENGTH = CLIENT_HEADER_LENGTH + VALIDATE_LENGTH;
	
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
		
	}
	
	public static Message parseMessageFromClient(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(CLIENT_SIZE_LENGTH);
		short cmd = buf.readShort();

		Message message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
		return message;
	}
	
	public void parseFromServer(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(SERVER_SIZE_LENGTH);
		short cmd = buf.readShort();

		message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
	}

	public static Message parseMessageFromServer(ByteBuf buf) throws UnsupportedEncodingException {
		
		buf.skipBytes(SERVER_SIZE_LENGTH);
		short cmd = buf.readShort();

		Message message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
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
	public void arrayToClient(ChannelHandlerContext ctx, ByteBuf out) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		arrayToClient(message, ctx, out);
		
	}
	
	public static void arrayToClient(Message message, ChannelHandlerContext ctx, ByteBuf out) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		int totalLen = message.getSize() + SERVER_EXTRA_LENGTH;
		
		ByteBuf buf = ctx.alloc().heapBuffer(totalLen);
		
		buf.writeInt(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);
		
		int offset = buf.arrayOffset() + buf.readerIndex();
		
		// do validate in connector
		buf.writeByte(ByteValidate.xor(buf.array(), offset, buf.readableBytes()));
		
		out.writeBytes(buf);
		
		// if calls int message.array(buf)
		// every inner message calls it's release
		message.release();
		buf.release();
		
		
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
	public void arrayToServer(ByteBuf buf) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		arrayToServer(message, buf);
		
	}
	
	public static void arrayToServer(Message message, ByteBuf buf) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		int totalLen = message.getSize() + CLIENT_EXTRA_LENGTH;
		
		if(!buf.isWritable(totalLen))
			buf.ensureWritable(totalLen);
		
		buf.writeShort(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);
		
		// do validate in connector
		buf.writeByte(VALIDATE);
		
		// if calls int message.array(buf)
		// every inner message calls it's release
		message.release();
		
	}
	
}