package org.snake.centerserver.command.server.netty.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.snake.message.event.CommandMessageEvent;


import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommandEventProcessor {
	
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
    
    private static final int TIME_OUT = 20 * 1000;
    
    private static final int RING_BUFFER_SIZE = 64 * 1024;
   
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private Disruptor<CommandMessageEvent> disruptor;
    private RingBuffer<CommandMessageEvent> ringBuffer;

	private CommandEventProcessor() {

	}
	
    private static class CommandEventProcessorInstance{
        private static final CommandEventProcessor instance = new CommandEventProcessor();
    }
    
    /*
     * singleton instance
     */
    public static CommandEventProcessor getInstance(){
        return CommandEventProcessorInstance.instance;
    }
    
	// 写入事件
    public void enqueue(Channel channel, ByteBuf buf) throws Exception {
    	
    	if(null != ringBuffer) {
    		
	        long sequence = ringBuffer.next(); 
	       
	        try {
	        	
	        	CommandMessageEvent event = ringBuffer.get(sequence); 
	        	
	        	event.setChannel(channel);
	        	event.parseFromClient(buf);
	            
	        } finally {
      	
	            ringBuffer.publish(sequence);
	        }
    	} 

    }


    /**
     * Increases the reference count and creates and starts a new Disruptor and associated thread if none currently
     * exists.
     *
     * @see #stop()
     */
    public void start() {
    	    	
    	disruptor = 
    			new Disruptor<CommandMessageEvent>(CommandEventFactory.getInstance(), 
    								   RING_BUFFER_SIZE, 
    								   executor, 
    								   ProducerType.MULTI, 
    								   new SleepingWaitStrategy());

    	disruptor.setDefaultExceptionHandler(new CommandEventExceptionHandler());
    	
        disruptor.handleEventsWith(new CommandEventHandler());
        
        disruptor.start();
        
        ringBuffer = disruptor.getRingBuffer();
        
        log.info("disruptor started with buffer size {}", RING_BUFFER_SIZE);
    }
    
    /**
     * Decreases the reference count. If the reference count reached zero, the Disruptor and its associated thread are
     * shut down and their references set to {@code null}.
     */
    public boolean stop() {
    	
    	Disruptor<CommandMessageEvent> temp = disruptor;
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
    private boolean hasEvents(Disruptor<CommandMessageEvent> theDisruptor) {
    	
        RingBuffer<CommandMessageEvent> ringBuffer = theDisruptor.getRingBuffer();
        
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }
    
    
}
