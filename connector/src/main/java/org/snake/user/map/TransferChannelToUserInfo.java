package org.snake.user.map;


import io.netty.channel.Channel;

public class TransferChannelToUserInfo extends UserInfoMap<Channel> {

	private TransferChannelToUserInfo() {
		
	}
	
    private static class TransferChannelToUserInfoInstance{
        private static final TransferChannelToUserInfo instance = 
        						new TransferChannelToUserInfo();
    }
    
    /*
     * singleton instance
     */
    public static TransferChannelToUserInfo getInstance(){
        return TransferChannelToUserInfoInstance.instance;
    }
    
}
