package org.snake.connector.transfer.server.handler.crypt;

public class ByteValidate {
	
	public static byte xor(byte[] date, int offset, int len) {
		
		byte result = 0;
		
		for(int i = offset; i < offset + len; ++i)
			result ^= date[i];
		
		return result;
		
	}

}
