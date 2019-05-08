package abc.bcd.pool;


import abc.bcd.Message;
import abc.bcd.factory.KeyedMessageFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/*
 * message pool
 */
public final class KeyedMessagePool {

	public static final int POOL_MAP_LENGTH = KeyedMessageFactory.CMDS.length * 2;
	
	private KeyedMessagePool() {
		// TODO Auto-generated constructor stub
		
		for(int cmd: KeyedMessageFactory.CMDS)
			register(cmd);
	}
	
	
    private static class KeyedMessagePoolInstance{
        private static final KeyedMessagePool instance = 
        						new KeyedMessagePool();
    }
    
    /*
     * singleton instance
     */
    public static KeyedMessagePool getInstance(){
        return KeyedMessagePoolInstance.instance;
    }
    
    
    // cmd - list map
    private final Map<Integer, ConcurrentLinkedDeque<Message>> poolMap =
            new ConcurrentHashMap<Integer, ConcurrentLinkedDeque<Message>>(POOL_MAP_LENGTH); 
    
    private final ReadWriteLock cmdLock = new ReentrantReadWriteLock(true);
    
    /**
     * Register the use of a cmd by an object.
     *
     * @param cmd The message cmd to register
     *
     */
    private void register(final Integer cmd) {
    	
    	Lock lock = cmdLock.readLock();
    	ConcurrentLinkedDeque<Message> deque = null;
    	
    	try {
    		
    		 lock.lock();
    		 deque = poolMap.get(cmd);
    		 
    		 if(null == deque) {
    			 
    			 lock.unlock();    			 
    			 lock = cmdLock.writeLock();
                 lock.lock();
                 deque = poolMap.get(cmd);
                 
                 if(null == deque) {
                	 
                	 deque = new ConcurrentLinkedDeque<Message>();
                	 
                	 poolMap.put(cmd, deque);
                	 
                 } 
    			 
    		 } 
    		 
    	} finally {
    		lock.unlock();
    	}
    	
    }
    

    /**
     * Obtains an instance from this pool for the specified <code>key</code>.
     *
     * @param cmd the message cmd used to obtain the object
     *
     * @return an instance from this pool.
     *              
     * if key not found, return null
     */
	public Message borrowMessage(final Integer cmd) {
		// TODO Auto-generated method stub
		
		ConcurrentLinkedDeque<Message> deque = poolMap.get(cmd);
		
		if(null == deque)
			return null;
		
		Message	message = deque.pollFirst();
		
		if(null == message) 				
			message = KeyedMessageFactory.create(cmd);
		
		message.retain();
			
		return message;
	}
	
	
    /**
     * Return an instance to the pool. 
	 *
     * @param message an instance to be returned.
     *
     */
	public void returnMessage(final Message message) {
		// TODO Auto-generated method stub
		
		final ConcurrentLinkedDeque<Message> deque = poolMap.get(message.getCmdInteger());
		
		if(null != deque)
			deque.addFirst(message);
		
	}
	

	public void printObjectCount() {

		
		for(Map.Entry<Integer, ConcurrentLinkedDeque<Message>> entry: poolMap.entrySet())
			System.err.println("object count: " + entry.getValue().size() + " cmd: " + entry.getKey());
	

	}
	
}