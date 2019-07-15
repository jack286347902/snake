package test.disruptor;

import java.util.concurrent.atomic.AtomicInteger;

import org.snake.message.event.MessageEvent;

import com.lmax.disruptor.EventHandler;

import test.client.FillEvent;


public class DQEventHandler implements EventHandler<MessageEvent> {
	

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
    	
		new FillEvent().process(event);
		
//		event.getChannel().writeAndFlush(event);
				
	}
	
	public static final AtomicInteger COUNTER = new AtomicInteger(0);


}
