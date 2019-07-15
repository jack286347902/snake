package org.snake.message.m2;


import org.snake.message.Message;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;

import org.snake.message.m1.Item;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;



//FirstRequest

/*
 * FirstRequest testing.........
 */
public final class FirstRequest extends Message {

	public static final short CMD_SHORT = 20001;
	public static final int CMD_INT = 20001;
	public static final Integer CMD_INTEGER = 20001;

	 //	 double
	private double add;
	 //
	private float aff;
	 //
	private int ai32;
	 //
	private long ai64;
	 //
	private boolean aisArray;
	 //
	private String aname;
	private Item aitem = null;
	private String[] al6;
	private final List<Item> al7 = new LinkedList<Item>();


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

	public double getAdd() {
		return add;
	}
	public void setAdd(double add) {
		this.add = add;
	}
	public float getAff() {
		return aff;
	}
	public void setAff(float aff) {
		this.aff = aff;
	}
	public int getAi32() {
		return ai32;
	}
	public void setAi32(int ai32) {
		this.ai32 = ai32;
	}
	public long getAi64() {
		return ai64;
	}
	public void setAi64(long ai64) {
		this.ai64 = ai64;
	}
	public boolean getAisArray() {
		return aisArray;
	}
	public void setAisArray(boolean aisArray) {
		this.aisArray = aisArray;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public Item getAitem() {
		return aitem;
	}
	public String[] getAl6() {
		return al6;
	}
	public void setAl6(String[] al6) {
		this.al6 = al6;
	}
	public List<Item> getAl7() {
		return al7;
	}

	@Override
	public void parse(ByteBuf buf) throws UnsupportedEncodingException {

		add = buf.readDouble();
		aff = buf.readFloat();
		ai32 = buf.readInt();
		ai64 = buf.readLong();
		aisArray = buf.readBoolean();
		aname = readString(buf);
		aitem.parse(buf);

		al6 = readStringArray(buf);

		short al7Len = buf.readShort();
		for(int i = 0; i < al7Len; ++i) {
			Item itemTemp = (Item)MESSAGE_POOL.borrowMessage(Item.CMD_INTEGER);
			itemTemp.parse(buf);
			al7.add(itemTemp);
		}


	}

	@Override
	public void array(ByteBuf buf) throws UnsupportedEncodingException {

		buf.writeDouble(add);
		buf.writeFloat(aff);
		buf.writeInt(ai32);
		buf.writeLong(ai64);
		buf.writeBoolean(aisArray);

		writeString(aname, buf);
		aitem.array(buf);

		writeStringArray(al6, buf);

		buf.writeShort(al7.size());
		for(Item itemTemp: al7)
			itemTemp.array(buf);


	}

	@Override
	public void releaseMessage() {

		aitem.release();

		Iterator<Item> al7Iter = al7.iterator();
		while(al7Iter.hasNext())
			al7Iter.next().release();


		if(getRefCnt() == 0) {

			aname = null;
			aitem = null;

			al6 = null;

			al7.clear();

			MESSAGE_POOL.returnMessage(this);

		}

	}

	@Override
	public void retainMessage() {

		if(null == aitem)
			aitem = (Item)MESSAGE_POOL.borrowMessage(Item.CMD_INTEGER);
		else
			aitem.retain();


		Iterator<Item> al7Iter = al7.iterator();
		while(al7Iter.hasNext())
			al7Iter.next().retain();


	}

	@Override
	public int getSize() {

		int size = SIZE;

		if(null !=aname)
			size += aname.length();

		size += aitem.getSize();


		if(null != al6)
			for(String temp: al6)
				size += temp.length() + 2;


		for(Item temp: al7)
			size += temp.getSize();


		return size;

	}

	private static final int SIZE = 31;


	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("cmd=")
			.append(getCmdInt())
			.append(" refCnt = ")
			.append(getRefCnt());
		builder.append(" add = ")
			.append(add);

		builder.append(" aff = ")
			.append(aff);

		builder.append(" ai32 = ")
			.append(ai32);

		builder.append(" ai64 = ")
			.append(ai64);

		builder.append(" aisArray = ")
			.append(aisArray);

		builder.append(" aname = ")
			.append(aname);

		builder.append(" aitem = ")
			.append(aitem);

		if(null == al6)
			builder.append(" al6 = null ");
		else {

			builder.append(" al6 size = ")
				.append(al6.length);

			builder.append(" {");
			for(String temp: al6)
				builder.append(temp).append(", ");
			builder.append("} ");

		}


		if(null == al7)
			builder.append(" al7 = null ");
		else {

			builder.append(" al7 size = ")
				.append(al7.size());

			builder.append(" {");
			for(Item temp: al7)
				builder.append(temp).append(", ");
			builder.append("}");

		}


		return builder.toString();

	}
}