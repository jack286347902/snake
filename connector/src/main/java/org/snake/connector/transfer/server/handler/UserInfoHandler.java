package org.snake.connector.transfer.server.handler;

import org.snake.connector.transfer.user.UserInfo;
import org.snake.connector.transfer.user.map.TokenToUserInfo;
import org.snake.message.Message;
import org.snake.message.event.MessageEvent;
import org.snake.message.login.ClientLogin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class UserInfoHandler extends ChannelInboundHandlerAdapter {
	
	private TokenToUserInfo tokenToUserInfo = TokenToUserInfo.getInstance();

	
	// LOGIN
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	UserInfo userInfo = ctx.channel()
				    	       .attr(UserInfo.ATTR_KEY)
				    	       .get();
    	
    	ByteBuf in = (ByteBuf)msg;
    	
    	// 认证成功后首次登录
    	if(null == userInfo) {
    		
    		
    		ByteBuf buf = null;
    		
    		if (in.isReadable(MessageEvent.SERVER_SIZE_LENGTH)) {
    			
    			// 标记读位置
    			in.markReaderIndex();
    			
    			int size = in.readShort();
    			
    			in.resetReaderIndex();
    			
    			if(in.isReadable(size)) 
    				buf = in.readRetainedSlice(size);
    			else 
    				return;
    		}
    		
    		Message message = MessageEvent.parseMessageFromClient(buf);  
    		    		
			ClientLogin clientLogin = (ClientLogin)message;
    		
    		userInfo = tokenToUserInfo.get(clientLogin.getToken());
    		
    		if(null == userInfo) {
    			
    			ctx.close();
    			
				return;
    		}
    		
    		if(null != userInfo.getClientChannel()) {
    			
    			ctx.close();
    			
				return;
    		}
    		
    		
    		Channel channel = ctx.channel();
    		
    		channel.attr(UserInfo.ATTR_KEY).set(userInfo);
			
			userInfo.setClientChannel(channel);
			
			userInfo.resetCrypt();
			
			Channel transferChannel = userInfo.getTransferChannel();
			
			if(null != transferChannel
					&& transferChannel.isActive()) {
				
				transferChannel.writeAndFlush(buf);
				
			} else {
				
				if(null != transferChannel) {
					
					transferChannel.close();
					transferChannel = null;
				
				}
				
				userInfo.doConnect(channel.eventLoop());
				
//				while(null != userInfo.getClientChannel()) {
//				
//					if(null != userInfo.getTransferChannel()) {
//						
//						userInfo.getTransferChannel().writeAndFlush(buf);
//						
//					} else {
//						
//						Thread.sleep(500);
//					}
//				
//				}
				
			}
			
			return;
    	}

    	if(in.refCnt() > 0)
    		ctx.fireChannelRead(msg);
    }
    

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	
    	// notify logout
    	
    	UserInfo userInfo = ctx.channel()
	    	       .attr(UserInfo.ATTR_KEY)
	    	       .get();
    	
    	if(null != userInfo)
    		userInfo.setClientChannel(null);
    	
    }
    
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();

        ctx.close();
    }
}
