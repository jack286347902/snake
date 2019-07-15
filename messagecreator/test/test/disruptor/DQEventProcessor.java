package test.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snake.message.event.MessageEvent;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class DQEventProcessor {
	
	public static final Logger logger = LogManager.getLogger("DQEventProcessor");
	
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
    
    private static final int TIME_OUT = 20 * 1000;
    
    private static final int RING_BUFFER_SIZE = 64 * 1024;
    
    private DQEventFactory eventFactory = DQEventFactory.getInstance();
    
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private Disruptor<MessageEvent> disruptor;
    private RingBuffer<MessageEvent> ringBuffer;

	private DQEventProcessor() {
		start();
	}
	
    private static class DQEventProcessorInstance{
        private static final DQEventProcessor instance = new DQEventProcessor();
    }
    
    /*
     * singleton instance
     */
    public static DQEventProcessor getInstance(){
        return DQEventProcessorInstance.instance;
    }
    
	// 写入事件
    public void enqueue(Channel channel, ByteBuf buf) throws Exception {
    	
    	if(null != ringBuffer) {
    		
	        long sequence = ringBuffer.next(); 
	       
	        try {
	        	
	        	MessageEvent event = ringBuffer.get(sequence); 
	        	
	        	event.setChannel(channel);
	        	event.parseFromClient(buf);
	            
	        } finally {
	        	
//	        	System.err.println("s enq: " + COUNTER.getAndIncrement() + "  " + sequence);
		    	
	        	
	            ringBuffer.publish(sequence);
	        }
    	} 

    }
    
    public static final AtomicInteger COUNTER = new AtomicInteger(0);



    /**
     * Increases the reference count and creates and starts a new Disruptor and associated thread if none currently
     * exists.
     *
     * @see #stop()
     */
    private void start() {
    	    	
    	WaitStrategy waitStrategy = new SleepingWaitStrategy();
    	
    	disruptor = 
    			new Disruptor<MessageEvent>(eventFactory, 
    								   RING_BUFFER_SIZE, 
    								   executor, 
    								   ProducerType.MULTI, 
    								   waitStrategy);
    
    	ExceptionHandler<MessageEvent> exceptionHandler = new DQEventExceptionHandler();
    	
    	disruptor.setDefaultExceptionHandler(exceptionHandler);
    	
    	EventHandler<MessageEvent> eventHandler = new DQEventHandler();
    	
        disruptor.handleEventsWith(eventHandler);
        
        disruptor.start();
        
        ringBuffer = disruptor.getRingBuffer();
    }
    
    /**
     * Decreases the reference count. If the reference count reached zero, the Disruptor and its associated thread are
     * shut down and their references set to {@code null}.
     */
    public boolean stop() {
    	
    	Disruptor<MessageEvent> temp = disruptor;
        if (temp == null) {
            return true; // disruptor was already shut down by another thread
        }
                
        // We must guarantee that publishing to the RingBuffer has stopped before we call disruptor.shutdown().
        disruptor = null; // client code fails with NPE if log after stop = OK
   
        // Calling Disruptor.shutdown() will wait until all enqueued events are fully processed,
        // but this waiting happens in a busy-spin. To avoid (postpone) wasting CPU,
        // we sleep in short chunks, up to 10 seconds, waiting for the ringbuffer to drain.
        for (int i = 0; hasEvents(temp) && i < MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN; i++) {
            try {
                Thread.sleep(SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS); // give up the CPU for a while
            } catch (final InterruptedException e) { // ignored
            	
            	e.printStackTrace();
            }
        }
        
        try {
            // busy-spins until all events currently in the disruptor have been processed, or timeout
        	// 20 seconds
            temp.shutdown(TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
        	e.printStackTrace();
            temp.halt(); // give up on remaining events, if any
        }

        executor.shutdown();
        
        return true;
    
    }
    
    
    /**
     * Returns {@code true} if the specified disruptor still has unprocessed events.
     */
    private boolean hasEvents(Disruptor<MessageEvent> theDisruptor) {
    	
        RingBuffer<MessageEvent> ringBuffer = theDisruptor.getRingBuffer();
        
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }
    
    
}
