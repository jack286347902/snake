package org.snake.zookeeper.manager.zookeeper;

import org.snake.zookeeper.servers.Servers;
import org.snake.zookeeper.zookeeper.ZkClient;

public interface ZkManager {
	
	public void registerSelf();
	
	public void addServer(ServerData serverData);
	public void serverRemoved(ServerData serverData);

	public void setZkData(ZkData zkData);
	public void setZkClient(ZkClient zkClient);
	
	public void init(ZkData zkData, ZkClient zkClient, Servers servers);
}
