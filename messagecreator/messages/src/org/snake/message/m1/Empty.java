package org.snake.message.m1;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;



public final class Empty extends Message {

	public static final short CMD_SHORT = 10002;
	public static final int CMD_INT = 10002;
	public static final Integer CMD_INTEGER = 10002;



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


	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {


	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {


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

	private static final int SIZE = 0;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		return builder.toString();

	}
}