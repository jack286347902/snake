package org.snake.transfer.user;


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
	
//	private String transferIp;
//	private int transferPort;
	
	// modify at same time
//	private Channel clientChannel;		// 客户端
//	private Channel transferChannel;	// 内网
			
	
	private ByteBuf sizeBuf = Unpooled.buffer(MessageEvent.CLIENT_SIZE_LENGTH);

	
}
