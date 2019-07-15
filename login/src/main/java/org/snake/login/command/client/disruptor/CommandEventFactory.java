package org.snake.login.command.client.disruptor;


import org.snake.message.event.CommandMessageEvent;

import com.lmax.disruptor.EventFactory;


public class CommandEventFactory implements EventFactory<CommandMessageEvent> {
	
	private CommandEventFactory() {
		
	}
	
    private static class CommandEventFactoryInstance{
        private static final CommandEventFactory instance = 
        						new CommandEventFactory();
    }
    
    /*
     * singleton instance
     */
    public static CommandEventFactory getInstance(){
        return CommandEventFactoryInstance.instance;
    }

	@Override
	public CommandMessageEvent newInstance() {
		// TODO Auto-generated method stub
		return new CommandMessageEvent();
	}

}
