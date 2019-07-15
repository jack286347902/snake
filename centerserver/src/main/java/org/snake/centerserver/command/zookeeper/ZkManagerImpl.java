package org.snake.centerserver.command.zookeeper;

import org.snake.zookeeper.manager.zookeeper.ServerData;
import org.snake.zookeeper.manager.zookeeper.ZkManagerBase;

public class ZkManagerImpl extends ZkManagerBase {
	
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

	}

	@Override
	public void serverRemoved(ServerData serverData) {
		// TODO Auto-generated method stub

	}


}
