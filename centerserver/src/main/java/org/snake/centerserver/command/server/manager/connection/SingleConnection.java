package org.snake.centerserver.command.server.manager.connection;


import org.snake.message.publics.ServerType;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SingleConnection {
		
	private Channel channel;
	private ServerType serverType;
	private int maxLoad;
	private int loaded;				// 当前人数
	private long time;
	private String ip;
	private int port;
	
}
