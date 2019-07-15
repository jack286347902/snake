package org.snake.transfer.user.server.netty.disruptor;

import org.snake.message.event.CommandMessageEvent;

import com.lmax.disruptor.EventFactory;



public class DQEventFactory implements EventFactory<CommandMessageEvent> {
	
	private DQEventFactory() {
		
	}
	
    private static class DQEventFactoryInstance{
        private static final DQEventFactory instance = 
        						new DQEventFactory();
    }
    
    /*
     * singleton instance
     */
    public static DQEventFactory getInstance(){
        return DQEventFactoryInstance.instance;
    }

	@Override
	public CommandMessageEvent newInstance() {
		// TODO Auto-generated method stub
		return new CommandMessageEvent();
	}

}
