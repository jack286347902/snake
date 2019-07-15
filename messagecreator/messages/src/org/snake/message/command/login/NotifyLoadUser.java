package org.snake.message.command.login;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// logic server -> other server
public final class NotifyLoadUser extends Message {

	public static final short CMD_SHORT = 204;
	public static final int CMD_INT = 204;
	public static final Integer CMD_INTEGER = 204;

	private String token;
	private long uuid;


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
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		token = readString(buf);
		uuid = buf.readLong();

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {


		writeString(token, buf);
		buf.writeLong(uuid);

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

	private static final int SIZE = 10;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" token = ")
			.append(token);

		builder.append(" uuid = ")
			.append(uuid);

		return builder.toString();

	}
}