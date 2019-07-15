package org.snake.centerserver.command.server.netty.disruptor;


import org.snake.centerserver.command.server.message.manager.CommandMessageManager;
import org.snake.message.event.CommandMessageEvent;


import com.lmax.disruptor.EventHandler;


public class CommandEventHandler implements EventHandler<CommandMessageEvent> {

	private CommandMessageManager commandMessageManager = CommandMessageManager.getInstance();

	@Override
	public void onEvent(CommandMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
    	
		commandMessageManager.process(event);
				
	}
	
}
