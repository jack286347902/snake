package org.snake.message.command.login;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// logic server -> connector/login web
// user load all data success
public final class ConnectorShutdown extends Message {

	public static final short CMD_SHORT = 209;
	public static final int CMD_INT = 209;
	public static final Integer CMD_INTEGER = 209;

	private String ip;
	private int port;


	@Override
	public Integer getCmdInteger() {
		return CMD_INTEGER;
	}
	@Override
	public int getCmdInt() {
		return CMD_INT;
	}
	@Override
	public short getCmdShort() {
		return CMD_SHORT;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		ip = readString(buf);
		port = buf.readInt();

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {


		writeString(ip, buf);
		buf.writeInt(port);

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {

			ip = null;

			MESSAGE_POOL.returnMessage(this);

		}

	}

	@Override
	public void retainMessage() {


	}

	@Override
	public int getSize() {

		int size = SIZE;

		if(null !=ip)
			size += ip.length();


		return size;

	}

	private static final int SIZE = 6;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" ip = ")
			.append(ip);

		builder.append(" port = ")
			.append(port);

		return builder.toString();

	}
}