package org.snake.connector.command.client.manager.message;


import org.snake.connector.transfer.user.UserInfo;
import org.snake.connector.transfer.user.map.TokenToUserInfo;
import org.snake.connector.transfer.user.map.UuidToUserInfo;
import org.snake.message.command.login.RemoveUser;
import org.snake.message.command.login.UserLoaded;
import org.snake.message.event.CommandMessageEvent;
import org.snake.message.publics.HeartBeat;

import io.netty.channel.Channel;


public class CommandMessageManager {
	
	private static final TokenToUserInfo TOKEN_TO_USER_INFO 
								= TokenToUserInfo.getInstance();
	
	private static final UuidToUserInfo UUID_TO_USER_INFO
								= UuidToUserInfo.getInstance();
	
	private CommandMessageManager() {
		
	}
	
    private static class CommandMessageManagerInstance{
        private static final CommandMessageManager instance = 
        						new CommandMessageManager();
    }
    
    /*
     * singleton instance
     */
    public static CommandMessageManager getInstance(){
        return CommandMessageManagerInstance.instance;
    }
	
	public void process(CommandMessageEvent event) {
		
		switch(event.getMessage().getCmdInt()) {

		case UserLoaded.CMD_INT:
			
			handleUserLoaded((UserLoaded)event.getMessage());			
			break;
			
		case RemoveUser.CMD_INT:
			
			handleRemoveUser((RemoveUser)event.getMessage());			
			break;
		
		}

	}
	
	private void handleUserLoaded(UserLoaded userLoaded) {
		
		UserInfo userInfo = UUID_TO_USER_INFO.get(userLoaded.getUuid());
		
		if(null != userInfo) {
			
			UUID_TO_USER_INFO.remove(userInfo.getUuid());
			TOKEN_TO_USER_INFO.remove(userInfo.getToken());
			
		} else {
			userInfo = new UserInfo();
		}
				
		userInfo.setToken(userLoaded.getToken());
		userInfo.setUuid(userLoaded.getUuid());
		userInfo.setTransferIp(userLoaded.getIp());
		userInfo.setTransferPort(userLoaded.getPort());
		
		TOKEN_TO_USER_INFO.add(userInfo.getToken(), userInfo);
		UUID_TO_USER_INFO.add(userInfo.getUuid(), userInfo);
		
	}

	private void handleRemoveUser(RemoveUser removeUser) {
		
		UserInfo userInfo = UUID_TO_USER_INFO.get(removeUser.getUuid());
		
		if(null == userInfo)
			return;
		
		Channel channel = userInfo.getClientChannel();
		
		if(null != channel
				&& channel.isActive()) {
			
			channel.close();
		}
		
		channel = userInfo.getTransferChannel();
		
		if(null != channel
				&& channel.isActive()) {
			
			channel.close();
		}
		
		UUID_TO_USER_INFO.remove(userInfo.getUuid());
		TOKEN_TO_USER_INFO.remove(userInfo.getToken());
	}
}
