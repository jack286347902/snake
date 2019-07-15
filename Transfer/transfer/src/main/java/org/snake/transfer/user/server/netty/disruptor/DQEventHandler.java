package org.snake.transfer.user.server.netty.disruptor;

import java.util.concurrent.atomic.AtomicInteger;

import org.snake.message.event.CommandMessageEvent;
import org.snake.transfer.user.server.message.MessageProcessor;

import com.lmax.disruptor.EventHandler;


public class DQEventHandler implements EventHandler<CommandMessageEvent> {
	

	@Override
	public void onEvent(CommandMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
    	
		MessageProcessor.process(event);
				
	}
	
	public static final AtomicInteger COUNTER = new AtomicInteger(0);


}
