package org.snake.transfer.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

// 心跳： 客户端向服务器发，服务器不回复
// 超时，断开连接，否则转发Event
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
	
	 @Override
     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		 
         if (evt instanceof IdleStateEvent) {
        	 
             IdleStateEvent e = (IdleStateEvent) evt;
             
             if (e.state() == IdleState.READER_IDLE) {
            	 
                 ctx.close();
                 
             }
         }
             
     }
}
