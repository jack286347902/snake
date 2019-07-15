package test.disruptor;

import org.snake.message.event.MessageEvent;

import com.lmax.disruptor.EventFactory;


public class DQEventFactory implements EventFactory<MessageEvent> {
	
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
	public MessageEvent newInstance() {
		// TODO Auto-generated method stub
		return new MessageEvent();
	}

}
