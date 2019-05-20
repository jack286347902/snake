package org.snake.testmessage.login;


import org.snake.testmessage.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// login center -> connector
// remove user
public final class ClientRemove extends Message {

	public static final short CMD_SHORT = 105;
	public static final int CMD_INT = 105;
	public static final Integer CMD_INTEGER = 105;

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

	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		uuid = buf.readLong();

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {

		buf.writeLong(uuid);

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {


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

	private static final int SIZE = 8;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" uuid = ")
			.append(uuid);

		return builder.toString();

	}
}