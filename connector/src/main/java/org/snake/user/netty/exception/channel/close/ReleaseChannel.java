package org.snake.user.netty.exception.channel.close;

import org.snake.user.UserInfo;
import org.snake.user.UserState;
import org.snake.user.map.ClientChannelToUserInfo;
import org.snake.user.map.TransferChannelToUserInfo;

import io.netty.channel.Channel;


public class ReleaseChannel {
	
	private ReleaseChannel() {
		
	}
	
    private static class ReleaseChannelInstance{
        private static final ReleaseChannel instance = 
        						new ReleaseChannel();
    }
    
    /*
     * singleton instance
     */
    public static ReleaseChannel getInstance(){
        return ReleaseChannelInstance.instance;
    }

	private ClientChannelToUserInfo clientChannelToUserInfo = ClientChannelToUserInfo.getInstance();
	
	private TransferChannelToUserInfo transferChannelToUserInfo = TransferChannelToUserInfo.getInstance();

	
	public void clientChannelClose(Channel clientChannel) {
		
		UserInfo userInfo = clientChannelToUserInfo.get(clientChannel);
		
		if(null != userInfo) {
			
			synchronized (userInfo) {
			
				Channel transferChannel = userInfo.getTransferChannel();
				
				if(null != transferChannel)
					transferChannel.close();
				
				clientChannel.close();
				
				clientChannelToUserInfo.remove(clientChannel);
				transferChannelToUserInfo.remove(transferChannel);
			
				userInfo.setState(UserState.LOGOUT);
			}
		
		}
	}
	
	public void transferChannelClose(Channel transferChannel) {
		
		UserInfo userInfo = transferChannelToUserInfo.get(transferChannel);
		
		if(null != userInfo) {
			synchronized (userInfo) {
			
				Channel clientChannel = userInfo.getTransferChannel();
				
				if(null != clientChannel)
					clientChannel.close();
				
				transferChannel.close();
				
				clientChannelToUserInfo.remove(clientChannel);
				transferChannelToUserInfo.remove(transferChannel);
			
				userInfo.setState(UserState.LOGOUT);
			}
		}
	}
	
	public void removeTransferChannel(UserInfo userInfo) {
		
		synchronized (userInfo) {
			
			Channel transferChannel = userInfo.getTransferChannel();
		
			if(null != transferChannel)
				transferChannel.close();
			
			transferChannelToUserInfo.remove(transferChannel);
		
		}
	}
}
