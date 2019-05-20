package org.snake.command.client.handler;

import java.io.UnsupportedEncodingException;

import org.snake.testmessage.Message;
import org.snake.testmessage.event.MessageEvent;
import org.snake.testmessage.login.ClientRemove;
import org.snake.testmessage.login.ConnectorIPPort;
import org.snake.testmessage.login.UserLoaded;
import org.snake.testmessage.login.UserMoving;
import org.snake.testmessage.login.UserMovingSuccess;
import org.snake.testmessage.pool.MessagePool;
import org.snake.transfer.client.TransferClient;
import org.snake.user.UserInfo;
import org.snake.user.UserState;
import org.snake.user.map.TokenToUserInfo;
import org.snake.user.map.TransferChannelToUserInfo;
import org.snake.user.map.UuidToUserInfo;
import org.snake.user.netty.exception.channel.close.ReleaseChannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CommandClientHandler extends ChannelInboundHandlerAdapter {
	

	private TokenToUserInfo tokenToUserInfo = TokenToUserInfo.getInstance();
	

	private UuidToUserInfo uuidToUserInfo = UuidToUserInfo.getInstance();
	

	private ReleaseChannel releaseChannel = ReleaseChannel.getInstance();
	

	private TransferChannelToUserInfo transferChannelToUserInfo = TransferChannelToUserInfo.getInstance();
	

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
		ConnectorIPPort connectorIPPort = 
				(ConnectorIPPort)MessagePool.borrowMessage(ConnectorIPPort.CMD_INTEGER);
		
		connectorIPPort.setConnectorIPPort("127.0.0.1:6666");
		
		MessageEvent messageEvent = new MessageEvent();
		
		messageEvent.setMessage(connectorIPPort);
		
		ctx.writeAndFlush(messageEvent);
    	
    	
        ctx.fireChannelActive();
    }

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
		process((ByteBuf)msg);
		
    }

    private void process(ByteBuf buf) throws UnsupportedEncodingException {
		
		Message message = MessageEvent.parseMessageFromServer(buf);

		UserInfo userInfo = null;
				
    	switch (message.getCmdInt()) {
		case UserLoaded.CMD_INT:
			
			UserLoaded userLoaded = (UserLoaded)message;
	    	
	    	userInfo = uuidToUserInfo.get(userLoaded.getUuid());
	    	
	    	if(null == userInfo) {
	    		
	    		userInfo = new UserInfo();
	    		userInfo.setUuid(userLoaded.getUuid());	
	    	}
	    	
			userInfo.setToken(userLoaded.getToken());
			userInfo.setTransferIp(userLoaded.getTransferIp());
			userInfo.setTransferPort(userLoaded.getTransferPort());
//			userInfo.resetCrypt();
			userInfo.setState(UserState.LOADED);
			
			uuidToUserInfo.add(userLoaded.getUuid(), userInfo);
			
			tokenToUserInfo.add(userLoaded.getToken(), userInfo);
			
			break;
		case UserMoving.CMD_INT:
			
			UserMoving userMoving = (UserMoving)message;
			
			userInfo = uuidToUserInfo.get(userMoving.getUuid());

	    	userInfo.setState(UserState.MOVING);
			
			break;
		case UserMovingSuccess.CMD_INT:
			
			UserMovingSuccess userMovingSuccess = (UserMovingSuccess)message;
			
			userInfo = uuidToUserInfo.get(userMovingSuccess.getUuid());

    		userInfo.setTransferIp(userMovingSuccess.getTransferIp());
    		userInfo.setTransferPort(userMovingSuccess.getTransferPort());
    		
			releaseChannel.removeTransferChannel(userInfo);
			
			// connect to transfer server
			if(!TransferClient.connect(userInfo)) {
				releaseChannel.clientChannelClose(userInfo.getClientChannel());
				return;
			}
			
			transferChannelToUserInfo.add(userInfo.getTransferChannel(), userInfo);
			userInfo.setState(UserState.LOGIN);

			break;
			
		case ClientRemove.CMD_INT:
			
			ClientRemove clientRemove = (ClientRemove)message;
			
			userInfo = uuidToUserInfo.get(clientRemove.getUuid());
			
			uuidToUserInfo.remove(userInfo.getUuid());
			
			tokenToUserInfo.remove(userInfo.getToken());
			
		default:
			break;
		}
    	
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	
    	
        cause.printStackTrace();
        ctx.close();
    }
}
