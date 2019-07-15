package org.snake.message.command.login;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// other server -> logic server
public final class UserLoadedPart extends Message {

	public static final short CMD_SHORT = 205;
	public static final int CMD_INT = 205;
	public static final Integer CMD_INTEGER = 205;

	private long uuid;
	private LoadedPart sign = LoadedPart.START;


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

	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public LoadedPart getSign() {
		return sign;
	}

	public void setSign(LoadedPart sign) {
		this.sign = sign;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		uuid = buf.readLong();
		sign = LoadedPart.get(buf.readInt());

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {

		buf.writeLong(uuid);
		buf.writeInt(sign.getValue());

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {

			sign = LoadedPart.START;

			MESSAGE_POOL.returnMessage(this);

		}

	}

	@Override
	public void retainMessage() {


	}

	@Override
	public int getSize() {

		int size = SIZE;


		return size;

	}

	private static final int SIZE = 12;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" uuid = ")
			.append(uuid);

		builder.append(" sign = ")
			.append(sign);

		return builder.toString();

	}
}