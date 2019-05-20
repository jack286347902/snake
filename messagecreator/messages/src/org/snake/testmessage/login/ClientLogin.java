package org.snake.testmessage.login;


import org.snake.testmessage.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// client -> connector
public final class ClientLogin extends Message {

	public static final short CMD_SHORT = 101;
	public static final int CMD_INT = 101;
	public static final Integer CMD_INTEGER = 101;

	private String token;


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

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		token = readString(buf);

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {


		writeString(token, buf);

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {

			token = null;

			MESSAGE_POOL.returnMessage(this);

		}

	}

	@Override
	public void retainMessage() {


	}

	@Override
	public int getSize() {

		int size = SIZE;

		if(null !=token)
			size += token.length();


		return size;

	}

	private static final int SIZE = 2;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" token = ")
			.append(token);

		return builder.toString();

	}
}