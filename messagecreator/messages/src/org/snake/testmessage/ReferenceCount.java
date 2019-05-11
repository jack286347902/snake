package org.snake.testmessage;

public interface ReferenceCount {

	/*
	 * add reference count
	 */
	void retain();
	
	/*
	 * minus reference count
	 * 
	 * if count = 0, return this message to pool and clear data
	 */
	void release();
	

}
