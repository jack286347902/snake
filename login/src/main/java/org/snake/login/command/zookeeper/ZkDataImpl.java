package org.snake.login.command.zookeeper;

import org.snake.message.publics.ServerType;
import org.snake.zookeeper.manager.zookeeper.ServerData;
import org.snake.zookeeper.manager.zookeeper.ZkData;

public class ZkDataImpl implements ZkData {
	
	public static final ServerType SERVER_TYPE = ServerType.LOGIN_WEB;
	
	private static final String PATH = "/login_web";
	private static final String PATH_DATA = Integer.toString(SERVER_TYPE.getValue());

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return PATH;
	}

	@Override
	public String getPathData() {
		// TODO Auto-generated method stub
		return PATH_DATA;
	}

	@Override
	public ServerData getServerData() {
		// TODO Auto-generated method stub
		
		ServerData serverData = new ServerData();
		
		serverData.setServerType(SERVER_TYPE);
		serverData.setTime(ServersImpl.START_TIME);
		
		return serverData;
	}


}
