package test.disruptor;

import com.lmax.disruptor.EventFactory;

import abc.bcd.event.MessageEvent;

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
