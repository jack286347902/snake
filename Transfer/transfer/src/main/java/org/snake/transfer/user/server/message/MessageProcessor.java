package org.snake.transfer.user.server.message;


import org.snake.message.event.CommandMessageEvent;
import org.snake.message.publics.HeartBeat;

public class MessageProcessor {
	
	public static void process(CommandMessageEvent event) {
		
		switch(event.getMessage().getCmdInt()) {

			
		case HeartBeat.CMD_INT:
			
			break;
		
		}

	}

}
