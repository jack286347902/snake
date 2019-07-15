package org.snake.connector.command.client.handler;

import org.snake.message.pool.MessagePool;
import org.snake.message.publics.HeartBeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

// 心跳： 客户端向服务器发，服务器不回复
// 超时，断开连接，否则转发Event
public class CommandEventHeartBeatHandler extends ChannelInboundHandlerAdapter {
	
	public static final HeartBeat HEART_BEAT = (HeartBeat)MessagePool.borrowMessage(HeartBeat.CMD_INTEGER);
	
			
	
	 @Override
     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		 
         if (evt instanceof IdleStateEvent) {
        	 
             IdleStateEvent e = (IdleStateEvent) evt;
             
             if (e.state() == IdleState.READER_IDLE) {
            	 
            	 HEART_BEAT.retain();
            	 
            	 ctx.channel().writeAndFlush(HEART_BEAT);
                 
             }
         }
             
     }
}
