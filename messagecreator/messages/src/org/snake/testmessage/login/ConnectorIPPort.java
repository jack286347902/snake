package org.snake.testmessage.login;


import org.snake.testmessage.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// connector -> login center 
// remove user
public final class ConnectorIPPort extends Message {

	public static final short CMD_SHORT = 106;
	public static final int CMD_INT = 106;
	public static final Integer CMD_INTEGER = 106;

	private String connectorIPPort;


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

	public String getConnectorIPPort() {
		return connectorIPPort;
	}
	public void setConnectorIPPort(String connectorIPPort) {
		this.connectorIPPort = connectorIPPort;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		connectorIPPort = readString(buf);

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {


		writeString(connectorIPPort, buf);

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {

			connectorIPPort = null;

			MESSAGE_POOL.returnMessage(this);

		}

	}

	@Override
	public void retainMessage() {


	}

	@Override
	public int getSize() {

		int size = SIZE;

		if(null !=connectorIPPort)
			size += connectorIPPort.length();


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
		builder.append(" connectorIPPort = ")
			.append(connectorIPPort);

		return builder.toString();

	}
}