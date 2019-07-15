package org.snake.message.factory;

import org.snake.message.command.*;
import org.snake.message.m2.*;
import org.snake.message.m1.*;
import org.snake.message.login.*;
import org.snake.message.publics.*;
import org.snake.message.command.login.*;
import org.snake.message.m3.*;

import org.snake.message.Message;


public final class KeyedMessageFactory {

	public static final int[] CMDS = new int[] {
			RegisterServer.CMD_INT,
			FirstRequest.CMD_INT,
			Item.CMD_INT,
			Empty.CMD_INT,
			Small.CMD_INT,
			ClientLogin.CMD_INT,
			HeartBeat.CMD_INT,
			CreateUser.CMD_INT,
			NotifyCreateUser.CMD_INT,
			LoadUser.CMD_INT,
			NotifyLoadUser.CMD_INT,
			UserLoadedPart.CMD_INT,
			UserLoaded.CMD_INT,
			RemoveUser.CMD_INT,
			UserRemoved.CMD_INT,
			ConnectorShutdown.CMD_INT,
			SecondRequest.CMD_INT
			};

	/*
	 * create new message by message cmd
	 * @param cmd message unique cmd
	 * @return message new message
	 */
	public static Message create(int cmd) {

		switch(cmd) {

		 case RegisterServer.CMD_INT: 
			return new RegisterServer();
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
		 case HeartBeat.CMD_INT: 
			return new HeartBeat();
		 case CreateUser.CMD_INT: 
			return new CreateUser();
		 case NotifyCreateUser.CMD_INT: 
			return new NotifyCreateUser();
		 case LoadUser.CMD_INT: 
			return new LoadUser();
		 case NotifyLoadUser.CMD_INT: 
			return new NotifyLoadUser();
		 case UserLoadedPart.CMD_INT: 
			return new UserLoadedPart();
		 case UserLoaded.CMD_INT: 
			return new UserLoaded();
		 case RemoveUser.CMD_INT: 
			return new RemoveUser();
		 case UserRemoved.CMD_INT: 
			return new UserRemoved();
		 case ConnectorShutdown.CMD_INT: 
			return new ConnectorShutdown();
		 case SecondRequest.CMD_INT: 
			return new SecondRequest();
		}

		return null;
	}

}