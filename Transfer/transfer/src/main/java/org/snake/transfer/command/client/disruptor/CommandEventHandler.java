package org.snake.transfer.command.client.disruptor;


import org.snake.message.event.CommandMessageEvent;
import org.snake.transfer.command.client.manager.message.CommandMessageManager;

import com.lmax.disruptor.EventHandler;


public class CommandEventHandler implements EventHandler<CommandMessageEvent> {

	private CommandMessageManager commandMessageManager = CommandMessageManager.getInstance();

	@Override
	public void onEvent(CommandMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
    	
		commandMessageManager.process(event);
				
	}
	
}
