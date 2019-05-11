package test.disruptor;

import java.util.concurrent.atomic.AtomicInteger;

import org.snake.testmessage.event.MessageEvent;

import com.lmax.disruptor.EventHandler;


public class DQEventHandler implements EventHandler<MessageEvent> {
	

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
    	
		event.getChannel().writeAndFlush(event);
				
	}
	
	public static final AtomicInteger COUNTER = new AtomicInteger(0);


}
