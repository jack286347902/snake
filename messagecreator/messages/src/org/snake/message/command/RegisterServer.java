package org.snake.message.command;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;

import org.snake.message.publics.ServerType;



// other server -> center server
public final class RegisterServer extends Message {

	public static final short CMD_SHORT = 101;
	public static final int CMD_INT = 101;
	public static final Integer CMD_INTEGER = 101;

	private ServerType serverType = ServerType.START;
	 // 最大人数
	private int maxLoad;
	 // 启动时间
	private long time;
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

	public ServerType getServerType() {
		return serverType;
	}

	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}
	public int getMaxLoad() {
		return maxLoad;
	}
	public void setMaxLoad(int maxLoad) {
		this.maxLoad = maxLoad;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
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

		serverType = ServerType.get(buf.readInt());
		maxLoad = buf.readInt();
		time = buf.readLong();
		ip = readString(buf);
		port = buf.readInt();

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {

		buf.writeInt(serverType.getValue());
		buf.writeInt(maxLoad);
		buf.writeLong(time);

		writeString(ip, buf);
		buf.writeInt(port);

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {

			serverType = ServerType.START;
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

	private static final int SIZE = 22;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" serverType = ")
			.append(serverType);

		builder.append(" maxLoad = ")
			.append(maxLoad);

		builder.append(" time = ")
			.append(time);

		builder.append(" ip = ")
			.append(ip);

		builder.append(" port = ")
			.append(port);

		return builder.toString();

	}
}