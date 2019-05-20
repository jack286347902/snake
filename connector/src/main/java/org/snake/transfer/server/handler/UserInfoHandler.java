package org.snake.transfer.server.handler;

import java.util.HashMap;
import java.util.Map;

import org.snake.testmessage.Message;
import org.snake.testmessage.event.MessageEvent;
import org.snake.testmessage.login.ClientLogin;
import org.snake.transfer.client.TransferClient;
import org.snake.user.UserInfo;
import org.snake.user.UserState;
import org.snake.user.map.ClientChannelToUserInfo;
import org.snake.user.map.TokenToUserInfo;
import org.snake.user.map.TransferChannelToUserInfo;
import org.snake.user.netty.exception.channel.close.ReleaseChannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class UserInfoHandler extends ChannelInboundHandlerAdapter {
	
	private TokenToUserInfo tokenToUserInfo = TokenToUserInfo.getInstance();

	private ClientChannelToUserInfo clientChannelToUserInfo = ClientChannelToUserInfo.getInstance();
	
	private TransferChannelToUserInfo transferChannelToUserInfo = TransferChannelToUserInfo.getInstance();
	
	private ReleaseChannel releaseChannel = ReleaseChannel.getInstance();

	private static final  Map<Channel, ByteBuf> LOGIN_MAP = new HashMap<Channel, ByteBuf>();
	
	// LOGIN
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	Channel channel = ctx.channel();
    	
    	UserInfo userInfo = clientChannelToUserInfo.get(channel);
    	
    	// 认证成功后首次登录
    	if(null == userInfo) {
    		
    		ByteBuf in = (ByteBuf)msg;
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
    		
    		buf.markReaderIndex();
    		
    		Message message = MessageEvent.parseMessageFromClient(buf);    		
    		
    		buf.resetReaderIndex();
    		
    		LOGIN_MAP.put(channel, buf);
    		
    		if(message instanceof ClientLogin) {
    			
    			ClientLogin clientLogin = (ClientLogin)message;
        		
        		userInfo = tokenToUserInfo.get(clientLogin.getToken());
        		
        		if(null == userInfo) {
        			releaseChannel.clientChannelClose(channel);
    				return;
        		}
        		
        		if(userInfo.getState() != UserState.LOADED
        				&& userInfo.getState() != UserState.LOGOUT) {
        			releaseChannel.clientChannelClose(channel);
        			return;
        		}
    			
    			userInfo.setEventLoop(channel.eventLoop());
    			userInfo.setClientChannel(channel);
    			
    			// close client channel -> not close transfer channel ?
    			// connect to transfer server
    			if(!TransferClient.connect(userInfo)) {
    				releaseChannel.clientChannelClose(channel);
    				return;
    			}
    			
    			userInfo.resetCrypt();
    			userInfo.setState(UserState.LOGIN);
    			
    			clientChannelToUserInfo.add(channel, userInfo);
    			transferChannelToUserInfo.add(userInfo.getTransferChannel(), userInfo);
        		
    		} else {
    			releaseChannel.clientChannelClose(channel);
    		}
    		
    		return;

    	}

    	ctx.fireChannelRead(msg);
    }
    
    
	public static void transferClientLogin(UserInfo userInfo) {
		
		Channel clientChannel = userInfo.getClientChannel();
		Channel transferChannel = userInfo.getTransferChannel();
		
		if(LOGIN_MAP.containsKey(clientChannel))
			transferChannel.writeAndFlush(LOGIN_MAP.remove(clientChannel));

		
	}
    
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	
    	UserInfo userInfo = clientChannelToUserInfo.get(ctx.channel());
    	
    	if(null != userInfo)
    		userInfo.setState(UserState.LOGOUT);
    	
        ctx.fireChannelInactive();
    }
    
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();

        releaseChannel.clientChannelClose(ctx.channel());
    }
}
