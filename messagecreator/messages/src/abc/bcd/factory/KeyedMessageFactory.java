package abc.bcd.factory;

import abc.bcd.efgh.*;
import abc.bcd.efg.*;

import abc.bcd.Message;


public final class KeyedMessageFactory {

	public static final int[] CMDS = new int[] {
			AReq.CMD_INT,
			Item.CMD_INT,
			Empty.CMD_INT
			};

	/*
	 * create new message by message cmd
	 * @param cmd message unique cmd
	 * @return message new message
	 */
	public static Message create(int cmd) {

		switch(cmd) {

		 case AReq.CMD_INT: 
			return new AReq();
		 case Item.CMD_INT: 
			return new Item();
		 case Empty.CMD_INT: 
			return new Empty();
		}

		return null;
	}

}