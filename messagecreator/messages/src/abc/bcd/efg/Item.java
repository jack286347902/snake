package abc.bcd.efg;


import abc.bcd.Message;

import io.netty.buffer.ByteBuf;




// item别想吴三桂顺中
public final class Item extends Message {

	public static final short CMD_SHORT = 1001;
	public static final int CMD_INT = 1001;
	public static final Integer CMD_INTEGER = 1001;

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
	 // list
	private double[] l1;
	 //
	private float[] l2;
	private byte[] l831;
	private short[] l31;
	private int[] l3;
	 //
	private long[] l4;
	private boolean[] l5;
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
	public double[] getL1() {
		return l1;
	}
	public void setL1(double[] l1) {
		this.l1 = l1;
	}
	public float[] getL2() {
		return l2;
	}
	public void setL2(float[] l2) {
		this.l2 = l2;
	}
	public byte[] getL831() {
		return l831;
	}
	public void setL831(byte[] l831) {
		this.l831 = l831;
	}
	public short[] getL31() {
		return l31;
	}
	public void setL31(short[] l31) {
		this.l31 = l31;
	}
	public int[] getL3() {
		return l3;
	}
	public void setL3(int[] l3) {
		this.l3 = l3;
	}
	public long[] getL4() {
		return l4;
	}
	public void setL4(long[] l4) {
		this.l4 = l4;
	}
	public boolean[] getL5() {
		return l5;
	}
	public void setL5(boolean[] l5) {
		this.l5 = l5;
	}
	public String[] getL6() {
		return l6;
	}
	public void setL6(String[] l6) {
		this.l6 = l6;
	}

	@Override
	public void parse(ByteBuf buf) throws Exception {

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

		l1 = readDoubleArray(buf);

		l2 = readFloatArray(buf);

		l831 = readByteArray(buf);

		l31 = readShortArray(buf);

		l3 = readIntArray(buf);

		l4 = readLongArray(buf);

		l5 = readBooleanArray(buf);

		l6 = readStringArray(buf);

	}

	@Override
	public void array(ByteBuf buf) throws Exception {

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

		writeDoubleArray(l1, buf);

		writeFloatArray(l2, buf);

		writeByteArray(l831, buf);

		writeShortArray(l31, buf);

		writeIntArray(l3, buf);

		writeLongArray(l4, buf);

		writeBooleanArray(l5, buf);

		writeStringArray(l6, buf);

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

		if(null == l1)
			builder.append(" l1 = null ");
		else {

			builder.append(" l1 size = ")
				.append(l1.length);

			builder.append(" {");
			for(double temp: l1)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		if(null == l2)
			builder.append(" l2 = null ");
		else {

			builder.append(" l2 size = ")
				.append(l2.length);

			builder.append(" {");
			for(float temp: l2)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		if(null == l831)
			builder.append(" l831 = null ");
		else {

			builder.append(" l831 size = ")
				.append(l831.length);

			builder.append(" {");
			for(byte temp: l831)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		if(null == l31)
			builder.append(" l31 = null ");
		else {

			builder.append(" l31 size = ")
				.append(l31.length);

			builder.append(" {");
			for(short temp: l31)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		if(null == l3)
			builder.append(" l3 = null ");
		else {

			builder.append(" l3 size = ")
				.append(l3.length);

			builder.append(" {");
			for(int temp: l3)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		if(null == l4)
			builder.append(" l4 = null ");
		else {

			builder.append(" l4 size = ")
				.append(l4.length);

			builder.append(" {");
			for(long temp: l4)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		if(null == l5)
			builder.append(" l5 = null ");
		else {

			builder.append(" l5 size = ")
				.append(l5.length);

			builder.append(" {");
			for(boolean temp: l5)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


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