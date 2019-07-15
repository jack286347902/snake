package org.snake.connector.command.client.disruptor;


import org.snake.connector.command.client.manager.message.CommandMessageManager;
import org.snake.message.event.CommandMessageEvent;

import com.lmax.disruptor.EventHandler;


public class CommandEventHandler implements EventHandler<CommandMessageEvent> {

	private CommandMessageManager commandMessageManager = CommandMessageManager.getInstance();

	@Override
	public void onEvent(CommandMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
    	
		commandMessageManager.process(event);
				
	}
	
}
