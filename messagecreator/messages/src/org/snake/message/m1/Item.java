package org.snake.message.m1;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;




// item testing..........
public final class Item extends Message {

	public static final short CMD_SHORT = 10001;
	public static final int CMD_INT = 10001;
	public static final Integer CMD_INTEGER = 10001;

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
	 //
	private long i64;
	 //
	private boolean isArray;
	 //
	private String name;
	private String[] l6;


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
	public boolean getIsArray() {
		return isArray;
	}
	public void setIsArray(boolean isArray) {
		this.isArray = isArray;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getL6() {
		return l6;
	}
	public void setL6(String[] l6) {
		this.l6 = l6;
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
		isArray = buf.readBoolean();
		name = readString(buf);

		l6 = readStringArray(buf);

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
		buf.writeBoolean(isArray);

		writeString(name, buf);

		writeStringArray(l6, buf);

	}

	@Override
	public void releaseMessage() {


		if(getRefCnt() == 0) {

			name = null;

			l6 = null;

			MESSAGE_POOL.returnMessage(this);

		}

	}

	@Override
	public void retainMessage() {


	}

	@Override
	public int getSize() {

		int size = SIZE;

		if(null !=name)
			size += name.length();


		if(null != l6)
			for(String temp: l6)
				size += temp.length() + 2;


		return size;

	}

	private static final int SIZE = 40;


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

		builder.append(" isArray = ")
			.append(isArray);

		builder.append(" name = ")
			.append(name);

		if(null == l6)
			builder.append(" l6 = null ");
		else {

			builder.append(" l6 size = ")
				.append(l6.length);

			builder.append(" {");
			for(String temp: l6)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		return builder.toString();

	}
}