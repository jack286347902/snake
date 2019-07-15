package org.snake.connector.transfer.server.handler.crypt;


public class Crypt {
	
	private byte send_i;
	private byte send_j;
	private byte recv_i;
	private byte recv_j;
		
	private byte[] key;
	private int key_length;

	private byte recv_i_back;
	private byte recv_j_back;
	
	
	public Crypt(byte[] key) {
		
		resetKey(key);
				
	}
	
	public void resetKey(byte[] key) {
		
		this.key = key;
		this.key_length = key.length;
		
		this.send_i = this.send_j = this.recv_i = this.recv_j = 0;
		
	}
	
	public void saveRecvIndex() {
		recv_i_back = recv_i;
		recv_j_back = recv_j;
	}
	
	public void resetRecvIndex() {
		recv_i = recv_i_back;
		recv_j = recv_j_back;
	}
	
	
	// 保存接收Index，如果解码data失败，重置Index
	public void decryptRecv(byte[] data, int offset, int len) {
						
		for(int i = offset; i < offset + len; i++) {
			
			recv_i %= key_length;
			byte x = (byte) (((byte) (data[i] - recv_j)) ^ key[recv_i]);
			++recv_i;
			recv_j=data[i];
			data[i] = x;
			
		}
		
	}
	
	
	public void encryptSend(byte[] data, int offset, int len) {
				
		for(int i = offset; i < offset + len; i++) {
			
			send_i %= key_length;
			byte x = (byte) (((byte) (data[i] ^ key[send_i])) + send_j);
			++send_i;
			data[i] = send_j = x;
		}
		
	}

}
