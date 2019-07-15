package org.snake.login.command.client.manager.message;


import org.snake.login.sql.domain.UserState;
import org.snake.login.sql.service.UserLoginRankService;
import org.snake.login.sql.service.UserStateService;
import org.snake.message.Message;
import org.snake.message.command.login.ConnectorShutdown;
import org.snake.message.command.login.RemoveUser;
import org.snake.message.command.login.UserLoaded;
import org.snake.message.command.login.UserRemoved;
import org.snake.message.event.CommandMessageEvent;
import org.snake.message.publics.UserLoginState;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;


public class CommandMessageManager {
	
	private UserStateService userStateService;

	private UserLoginRankService userLoginRankService;
	
	public CommandMessageManager() {
		
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
    
    public void init(ApplicationContext ctx) {
    	
    	userStateService = (UserStateService) ctx.getBean("userStateService");

    	userLoginRankService = (UserLoginRankService) ctx.getBean("userLoginRankService");;
    	
    }
	
	public void process(CommandMessageEvent event) {
		
		Message message = event.getMessage();
		
		switch(message.getCmdInt()) {
		
		case UserLoaded.CMD_INT:
			
			handleUserLoaded(message);
			break;
			
		case RemoveUser.CMD_INT:
			
			handleRemoveUser(message);
			break;

		case UserRemoved.CMD_INT:
			
			handleUserRemoved(message);
			break;
			
		case ConnectorShutdown.CMD_INT:
			
			handleConnectorShutdown(message);
			break;
		
		}

		message.release();
	}
	
	@Transactional
	private void handleUserLoaded(Message message) {
		
		UserLoaded userLoaded = (UserLoaded)message;
		
		long uuid = userLoaded.getUuid();
		
		userLoginRankService.deleteUserLoginRank(uuid);
		
		UserState userState = userStateService.selectUserStateByUuid(uuid);
		
		userState.setState(UserLoginState.LOADED.getValue());
		userState.setIp(userLoaded.getIp());
		userState.setPort(userLoaded.getPort());
		
		userStateService.updateIpAndPort(userState);
	}
	
	@Transactional
	private void handleRemoveUser(Message message) {
		
		RemoveUser removeUser = (RemoveUser)message;
				
		userStateService.updateRemoveUser(UserLoginState.REMOVE.getValue(), 
										  removeUser.getUuid());
	}
	
	@Transactional
	private void handleUserRemoved(Message message) {
		
		UserRemoved userRemoved = (UserRemoved)message;
		
		userStateService.deleteUserState(userRemoved.getUuid());
	}
	
	@Transactional
	private void handleConnectorShutdown(Message message) {
		
		ConnectorShutdown connectorShutdown = (ConnectorShutdown)message;
		
		userStateService.updateConnectorShutdown(connectorShutdown.getIp(), 
												 connectorShutdown.getPort());
	}

}
