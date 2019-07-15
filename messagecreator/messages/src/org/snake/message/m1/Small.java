package org.snake.message.m1;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;



public final class Small extends Message {

	public static final short CMD_SHORT = 10003;
	public static final int CMD_INT = 10003;
	public static final Integer CMD_INTEGER = 10003;

	 // id
	private int id;
	 
/* count */
	private int count;
	 //	 double
	private double dd;
	 //
	private float ff;
	 //
	private byte i8;
	 //
	private short i16;
	 //
	private int i32;
	private long i64;


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

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getDd() {
		return dd;
	}
	public void setDd(double dd) {
		this.dd = dd;
	}
	public float getFf() {
		return ff;
	}
	public void setFf(float ff) {
		this.ff = ff;
	}
	public byte getI8() {
		return i8;
	}
	public void setI8(byte i8) {
		this.i8 = i8;
	}
	public short getI16() {
		return i16;
	}
	public void setI16(short i16) {
		this.i16 = i16;
	}
	public int getI32() {
		return i32;
	}
	public void setI32(int i32) {
		this.i32 = i32;
	}
	public long getI64() {
		return i64;
	}
	public void setI64(long i64) {
		this.i64 = i64;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		id = buf.readInt();
		count = buf.readInt();
		dd = buf.readDouble();
		ff = buf.readFloat();
		i8 = buf.readByte();
		i16 = buf.readShort();
		i32 = buf.readInt();
		i64 = buf.readLong();

	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {

		buf.writeInt(id);
		buf.writeInt(count);
		buf.writeDouble(dd);
		buf.writeFloat(ff);
		buf.writeByte(i8);
		buf.writeShort(i16);
		buf.writeInt(i32);
		buf.writeLong(i64);

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

	private static final int SIZE = 35;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" id = ")
			.append(id);

		builder.append(" count = ")
			.append(count);

		builder.append(" dd = ")
			.append(dd);

		builder.append(" ff = ")
			.append(ff);

		builder.append(" i8 = ")
			.append(i8);

		builder.append(" i16 = ")
			.append(i16);

		builder.append(" i32 = ")
			.append(i32);

		builder.append(" i64 = ")
			.append(i64);

		return builder.toString();

	}
}