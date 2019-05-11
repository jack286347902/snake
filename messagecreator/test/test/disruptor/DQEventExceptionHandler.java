package test.disruptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snake.testmessage.event.MessageEvent;

import com.lmax.disruptor.ExceptionHandler;

public class DQEventExceptionHandler implements ExceptionHandler<MessageEvent> {
	
	private static final Logger logger = LogManager.getLogger("DQEventExceptionHandler"); 

	@Override
	public void handleEventException(Throwable throwable, long sequence, MessageEvent event) {

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

        logger.error(sb);
        logger.error(throwable.getMessage());
        
        throwable.printStackTrace();
	}

	@Override
	public void handleOnShutdownException(Throwable throwable) {

		logger.error("DQEventProducer error shutting down:");
		logger.error(throwable.getMessage());
		
		throwable.printStackTrace();
	}

	@Override
	public void handleOnStartException(Throwable throwable) {

		logger.error("DQEventProducer error starting:");
		logger.error(throwable.getMessage());
		
		throwable.printStackTrace();
	}

}
