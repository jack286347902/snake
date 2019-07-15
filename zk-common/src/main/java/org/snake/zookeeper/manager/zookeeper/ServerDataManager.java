package org.snake.zookeeper.manager.zookeeper;


import java.util.concurrent.ConcurrentHashMap;

public class ServerDataManager {
	
	private ConcurrentHashMap<String, ServerData> map = new ConcurrentHashMap<String, ServerData>();
	
	private ServerDataManager() {
		
	}
	
    private static class ServerDataManagerInstance{
        private static final ServerDataManager instance = new ServerDataManager();
    }
    
    /*
     * singleton instance
     */
    public static ServerDataManager getInstance(){
        return ServerDataManagerInstance.instance;
    }

	public synchronized boolean add(ServerData serverData) {
		
		ServerData saved = get(serverData.getUniquePath());
		
		if(null != saved) {
			
			if(saved.isRemoved()) {
				
				saved.setRemoved(false);
			
				return true;
			}
			
			return false;
		}
		
		map.put(serverData.getUniquePath(), serverData);
		
		return true;
	}
	
	public synchronized void remove(ServerData serverData) {
		map.remove(serverData.getUniquePath());
	}
	
	public ServerData get(String key) {
		return map.get(key);
	}
	
	public boolean contains(ServerData serverData) {
		return map.containsKey(serverData.getUniquePath());
	}

	
	public synchronized boolean setRemoved(ServerData serverData) {
		
		ServerData saved = get(serverData.getUniquePath());
		
		if(null != saved) {
			
			saved.setRemoved(true);
			
			return true;
		}
		
		return false;
	}
}
