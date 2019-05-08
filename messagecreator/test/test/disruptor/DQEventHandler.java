package test.disruptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lmax.disruptor.EventHandler;

import abc.bcd.event.MessageEvent;

public class DQEventHandler implements EventHandler<MessageEvent> {
	
	private static final Logger logger = LogManager.getLogger("DQEventHandler"); 
	

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
		
		event.getChannel().writeAndFlush(event);
				
	}

}
