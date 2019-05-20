package org.snake.testmessage.login;


import org.snake.testmessage.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// user move to another server success
public final class UserMovingSuccess extends Message {

	public static final short CMD_SHORT = 104;
	public static final int CMD_INT = 104;
	public static final Integer CMD_INTEGER = 104;

	private long uuid;
	private String transferIp;
	private int transferPort;


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
	public String getTransferIp() {
		return transferIp;
	}
	public void setTransferIp(String transferIp) {
		this.transferIp = transferIp;
	}
	public int getTransferPort() {
		return transferPort;
	}
	public void setTransferPort(int transferPort) {
		this.transferPort = transferPort;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		uuid = buf.readLong();
		transferIp = readString(buf);
		transferPort = buf.readInt();

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {

		buf.writeLong(uuid);

		writeString(transferIp, buf);
		buf.writeInt(transferPort);

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {

			transferIp = null;

			MESSAGE_POOL.returnMessage(this);

		}

	}

	@Override
	public void retainMessage() {


	}

	@Override
	public int getSize() {

		int size = SIZE;

		if(null !=transferIp)
			size += transferIp.length();


		return size;

	}

	private static final int SIZE = 14;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" uuid = ")
			.append(uuid);

		builder.append(" transferIp = ")
			.append(transferIp);

		builder.append(" transferPort = ")
			.append(transferPort);

		return builder.toString();

	}
}