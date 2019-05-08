package abc.bcd.efg;


import abc.bcd.Message;

import io.netty.buffer.ByteBuf;



public final class Empty extends Message {

	public static final short CMD_SHORT = 1002;
	public static final int CMD_INT = 1002;
	public static final Integer CMD_INTEGER = 1002;



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
	public void parse(ByteBuf buf) throws Exception {


	}

	@Override
	public void array(ByteBuf buf) throws Exception {


		release();
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
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		return builder.toString();

	}
}