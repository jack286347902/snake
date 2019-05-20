package org.snake.testmessage.factory;

import org.snake.testmessage.m2.*;
import org.snake.testmessage.m1.*;
import org.snake.testmessage.login.*;
import org.snake.testmessage.m3.*;

import org.snake.testmessage.Message;


public final class KeyedMessageFactory {

	public static final int[] CMDS = new int[] {
			FirstRequest.CMD_INT,
			Item.CMD_INT,
			Empty.CMD_INT,
			Small.CMD_INT,
			ClientLogin.CMD_INT,
			UserLoaded.CMD_INT,
			UserMoving.CMD_INT,
			UserMovingSuccess.CMD_INT,
			ClientRemove.CMD_INT,
			ConnectorIPPort.CMD_INT,
			SecondRequest.CMD_INT
			};

	/*
	 * create new message by message cmd
	 * @param cmd message unique cmd
	 * @return message new message
	 */
	public static Message create(int cmd) {

		switch(cmd) {

		 case FirstRequest.CMD_INT: 
			return new FirstRequest();
		 case Item.CMD_INT: 
			return new Item();
		 case Empty.CMD_INT: 
			return new Empty();
		 case Small.CMD_INT: 
			return new Small();
		 case ClientLogin.CMD_INT: 
			return new ClientLogin();
		 case UserLoaded.CMD_INT: 
			return new UserLoaded();
		 case UserMoving.CMD_INT: 
			return new UserMoving();
		 case UserMovingSuccess.CMD_INT: 
			return new UserMovingSuccess();
		 case ClientRemove.CMD_INT: 
			return new ClientRemove();
		 case ConnectorIPPort.CMD_INT: 
			return new ConnectorIPPort();
		 case SecondRequest.CMD_INT: 
			return new SecondRequest();
		}

		return null;
	}

}