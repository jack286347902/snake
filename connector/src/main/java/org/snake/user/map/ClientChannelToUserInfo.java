package org.snake.user.map;

import io.netty.channel.Channel;


public class ClientChannelToUserInfo extends UserInfoMap<Channel> {

	private ClientChannelToUserInfo() {
		
	}
	
    private static class ClientChannelToUserInfoInstance{
        private static final ClientChannelToUserInfo instance = 
        						new ClientChannelToUserInfo();
    }
    
    /*
     * singleton instance
     */
    public static ClientChannelToUserInfo getInstance(){
        return ClientChannelToUserInfoInstance.instance;
    }
}
