package org.snake.message.pool;


import org.snake.message.Message;


public final class MessagePool {

	private static final KeyedMessagePool MESSAGE_POOL 
							= KeyedMessagePool.getInstance();
	
	
	/*
	 * 
	 * Obtains an instance from this pool for the specified <code>key</code>.
     *
     * @param cmd the message cmd used to obtain the object
     *
     * @return an instance from this pool.
     *              
     * if key not found, return null
     * 
	 *
	 * how to return message to message pool
	 * 
	 * 1. if refCnt > 0, --refCnt, minus reference count of submessages
	 * 		if not return to pool, collect by gc
	 * 2. if refCnt == 0, return message to pool, 
	 * 		clear list messages,
	 * 		submessage = null
	 * 
	 *
	 */
	public static Message borrowMessage(short cmd) {
		
		return MESSAGE_POOL.borrowMessage(new Integer(cmd));
	}
	
	public static Message borrowMessage(Integer cmd) {
		
		return MESSAGE_POOL.borrowMessage(cmd);
	}
	
	public static void printObjectCount() { 
		MESSAGE_POOL.printObjectCount();
	}
}
