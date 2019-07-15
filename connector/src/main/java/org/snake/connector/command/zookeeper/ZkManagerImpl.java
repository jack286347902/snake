package org.snake.connector.command.zookeeper;

import org.snake.connector.command.client.CommandClientFactory;
import org.snake.message.publics.ServerType;
import org.snake.zookeeper.manager.zookeeper.ServerData;
import org.snake.zookeeper.manager.zookeeper.ZkManagerBase;

public class ZkManagerImpl extends ZkManagerBase {
	

	private CommandClientFactory commandClientFactory
				= CommandClientFactory.getInstance();
	
	
	private ZkManagerImpl() {

	}
	
    private static class ZkManagerImplInstance{
        private static final ZkManagerImpl instance = new ZkManagerImpl();
    }
    
    /*
     * singleton instance
     */
    public static ZkManagerImpl getInstance(){
        return ZkManagerImplInstance.instance;
    }
    
	@Override
	public void addServer(ServerData serverData) {
		// TODO Auto-generated method stub

		if(serverDataManager.add(serverData)) {
			
			if(serverData.getServerType() == ServerType.CENTER_SERVER)
				commandClientFactory.connect(serverData);
			
		}
	}




}
