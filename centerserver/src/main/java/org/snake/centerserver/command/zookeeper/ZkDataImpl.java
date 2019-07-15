package org.snake.centerserver.command.zookeeper;

import org.snake.centerserver.command.server.netty.server.CommandServer;
import org.snake.message.publics.ServerType;
import org.snake.zookeeper.manager.zookeeper.ServerData;
import org.snake.zookeeper.manager.zookeeper.ZkData;

public class ZkDataImpl implements ZkData {
	
	private static final ServerType SERVER_TYPE = ServerType.CENTER_SERVER;
	
	private static final String PATH = "/center_server";
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
		serverData.setIp("192.168.0.105");
		serverData.setCommandPort(CommandServer.PORT);
		serverData.setTime(ServersImpl.START_TIME);
		
		return serverData;
	}


}
