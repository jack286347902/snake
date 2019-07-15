package org.snake.connector.transfer.user;

import java.io.UnsupportedEncodingException;

import org.snake.connector.transfer.client.TransferClient;
import org.snake.connector.transfer.server.handler.crypt.Crypt;
import org.snake.message.event.MessageEvent;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import lombok.Data;


// login center notify user login
@Data
public class UserInfo {
	
	public static final AttributeKey<UserInfo> ATTR_KEY 
								= AttributeKey.valueOf("UserInfo");

	
	private String token;
	private Long uuid;	// 是否已存在uuid
	
	private String transferIp;
	private int transferPort;
	
	// modify at same time
	private Channel clientChannel;		// 客户端
	private Channel transferChannel;	// 内网
			
	private Crypt crypt;
	
	private ByteBuf sizeBuf = Unpooled.buffer(MessageEvent.CLIENT_SIZE_LENGTH);
	
	public void resetCrypt() throws UnsupportedEncodingException {
		
		if(null == crypt)
			crypt = new Crypt(token.getBytes("UTF-8"));
		else
			crypt.resetKey(token.getBytes("UTF-8"));
		
	}
	
	public void doConnect(EventLoopGroup eventLoopGroup) {

		TransferClient.doConnect(this, eventLoopGroup);
		
	}
	
}
