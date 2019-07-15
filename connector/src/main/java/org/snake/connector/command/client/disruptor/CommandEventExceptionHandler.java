package org.snake.connector.command.client.disruptor;


import org.snake.connector.command.zookeeper.ServersImpl;
import org.snake.message.event.CommandMessageEvent;

import com.lmax.disruptor.ExceptionHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommandEventExceptionHandler implements ExceptionHandler<CommandMessageEvent> {
	

	@Override
	public void handleEventException(Throwable throwable, long sequence, CommandMessageEvent event) {

        StringBuilder sb = new StringBuilder();
        sb.append("DQEventProducer error handling event seq=").append(sequence).append(", value='");
        
        try {
            sb.append(event);
        } catch (final Exception ignored) {
        	
            sb.append("[ERROR calling ").append(event.getClass());
            sb.append(ignored).append("]");
            
            ignored.printStackTrace();
        }
        sb.append("\r\n");

        log.error(sb);
        log.error(throwable.getMessage());
        
        throwable.printStackTrace();
	}

	@Override
	public void handleOnShutdownException(Throwable throwable) {

		log.error("DQEventProducer error shutting down:");
		log.error(throwable.getMessage());
		
		throwable.printStackTrace();
	}

	@Override
	public void handleOnStartException(Throwable throwable) {

		log.error("DQEventProducer error starting:");
		log.error(throwable.getMessage());
		
		throwable.printStackTrace();
		
		ServersImpl.getInstance().stop();
	}

}
