package org.snake.message.command.login;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// login web -> center server
public final class CreateUser extends Message {

	public static final short CMD_SHORT = 201;
	public static final int CMD_INT = 201;
	public static final Integer CMD_INTEGER = 201;

	private String token;
	private long uuid;
	private long sign;


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
	public long getSign() {
		return sign;
	}
	public void setSign(long sign) {
		this.sign = sign;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		token = readString(buf);
		uuid = buf.readLong();
		sign = buf.readLong();

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {


		writeString(token, buf);
		buf.writeLong(uuid);
		buf.writeLong(sign);

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

	private static final int SIZE = 18;


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

		builder.append(" sign = ")
			.append(sign);

		return builder.toString();

	}
}