package org.snake.zookeeper.manager.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.snake.zookeeper.servers.Servers;
import org.snake.zookeeper.zookeeper.ZkClient;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;


@Log4j2
public abstract class ZkManagerBase implements ZkManager {

	@Setter
	private ZkClient zkClient;
	@Setter
	private ZkData zkData;
	
	private Servers servers;
	
	protected ServerDataManager serverDataManager = ServerDataManager.getInstance();
	
	public void init(ZkData zkData, ZkClient zkClient, Servers servers) {
		this.zkData = zkData;
		this.zkClient = zkClient;
		this.servers = servers;
		
		
		zkClient.setServers(servers);
		zkClient.setZkManager(this);
	}

	public void registerSelf() {
		
		String path = zkData.getPath();
		String pathData = zkData.getPathData();
		
		if(zkClient.isExistNode(path)) {
		
			byte[] data = zkClient.getNodeData(path);
			
			String dataString = new String(data);
			
			if(!dataString.equals(pathData)) {
				
				log.error("path={}, path data={}, not equals registered data={}", 
						  path, pathData, dataString);
				
				servers.stop();
			}
			
		
		} else {
			
			zkClient.createNode(CreateMode.PERSISTENT, path, pathData);
		}
		
				
		ServerData serverData = zkData.getServerData();
		
		String serverPath = path + "/" + serverData.getUniquePath();
		
		if(!zkClient.isExistNode(serverPath)) {
			
			zkClient.createNode(CreateMode.EPHEMERAL, serverPath, serverData.toJsonString());
		
		} else {
			
			log.error("path={}, already exist, path data={}", 
					serverPath, serverData.toJsonString());
			
			servers.stop();
			
			
		}

	}
	

	public void serverRemoved(ServerData serverData) {
		serverDataManager.setRemoved(serverData);
	}
}
