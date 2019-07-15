package org.snake.centerserver.command.server.message.manager;


import org.snake.centerserver.command.server.manager.connection.CommandConnectionManager;
import org.snake.centerserver.command.server.manager.connection.SingleConnection;
import org.snake.message.Message;
import org.snake.message.command.RegisterServer;
import org.snake.message.command.login.CreateUser;
import org.snake.message.command.login.LoadUser;
import org.snake.message.command.login.UserLoaded;
import org.snake.message.event.CommandMessageEvent;
import org.snake.message.pool.MessagePool;
import org.snake.message.publics.HeartBeat;
import org.snake.message.publics.ServerType;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;

@Component
public class CommandMessageManager {
	
	public static final CommandConnectionManager COMMAND_CONNECTION_MANAGER
							= CommandConnectionManager.getInstance();
	
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
				
		Message message = event.getMessage();
		
		System.err.println(message);
		
		switch(message.getCmdInt()) {

		case RegisterServer.CMD_INT:
			
			handleRegisterServer(event.getChannel(), message);
			
			break;
			
		case CreateUser.CMD_INT:
			
			handleCreateUser(event.getChannel(), message);
			break;
			
		case LoadUser.CMD_INT:
			
			handleLoadUser(event.getChannel(), message);
			break;

		case HeartBeat.CMD_INT:

			break;
		
		}
		
		message.release();

	}
	
	private void handleRegisterServer(Channel channel, Message message) {
		
		RegisterServer registerServer = (RegisterServer)message;
		
		SingleConnection sConn = new SingleConnection(channel, 
													  registerServer.getServerType(), 
													  registerServer.getMaxLoad(), 
													  0,
													  registerServer.getTime(),
													  registerServer.getIp(),
													  registerServer.getPort());
		
		COMMAND_CONNECTION_MANAGER.addConnection(sConn);
	}

	
	private void handleCreateUser(Channel channel, Message message) {
		
		CreateUser createUser = (CreateUser)message;
		
		handlUseLoaded(createUser.getToken(), createUser.getUuid(), createUser.getSign());
	}
	
	private void handlUseLoaded(String token, long uuid, long sign) {
		SingleConnection connector = 
				COMMAND_CONNECTION_MANAGER.getSingleConnection(ServerType.CONNECTOR, sign);
		
		UserLoaded userLoaded = (UserLoaded)MessagePool.borrowMessage(UserLoaded.CMD_INTEGER);
	
		userLoaded.setToken(token);
		userLoaded.setUuid(uuid);
		userLoaded.setIp(connector.getIp());
		userLoaded.setPort(connector.getPort());
			
		SingleConnection loginWeb = 
				COMMAND_CONNECTION_MANAGER.getSingleConnection(ServerType.LOGIN_WEB, sign);
		loginWeb.getChannel().writeAndFlush(userLoaded);
		
		// connector
		// transfer ip and port
		
		SingleConnection transfer = 
				COMMAND_CONNECTION_MANAGER.getSingleConnection(ServerType.TRANSFER_SERVER, sign);
		
		UserLoaded userLoadedTransfer = (UserLoaded)MessagePool.borrowMessage(UserLoaded.CMD_INTEGER);
		
		userLoadedTransfer.setToken(token);
		userLoadedTransfer.setUuid(uuid);
		userLoadedTransfer.setIp(transfer.getIp());
		userLoadedTransfer.setPort(transfer.getPort());
		connector.getChannel().writeAndFlush(userLoadedTransfer);
	}
	
	private void handleLoadUser(Channel channel, Message message) {
	
		LoadUser loadUser = (LoadUser)message;
		
		handlUseLoaded(loadUser.getToken(), loadUser.getUuid(), loadUser.getSign());
		
//		SingleConnection connection = 
//				COMMAND_CONNECTION_MANAGER.getSingleConnection(ServerType.CONNECTOR, loadUser.getSign());
//		
//		UserLoaded userLoaded = (UserLoaded)MessagePool.borrowMessage(UserLoaded.CMD_INTEGER);
//	
//		userLoaded.setToken(loadUser.getToken());
//		userLoaded.setUuid(loadUser.getUuid());
//		userLoaded.setIp(connection.getIp());
//		userLoaded.setPort(connection.getPort());
//	
//		userLoaded.retain();
//		channel.writeAndFlush(userLoaded);
//		
//		connection.getChannel().writeAndFlush(userLoaded);
		
		
	}

}
