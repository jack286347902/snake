package org.snake.centerserver.command.zookeeper;


import org.snake.centerserver.command.server.netty.disruptor.CommandEventProcessor;
import org.snake.centerserver.command.server.netty.server.CommandServer;
import org.snake.zookeeper.servers.Servers;
import org.snake.zookeeper.zookeeper.ZkClient;

import lombok.Getter;
import lombok.Setter;

public class ServersImpl implements Servers {
	
	public static final long START_TIME = System.currentTimeMillis();

	@Getter
	private CommandEventProcessor commandEventProcessor = CommandEventProcessor.getInstance();
	private CommandServer commandServer = CommandServer.getInstance();
	
	@Setter
	private ZkClient zkClient;
	
	private ServersImpl() {

	}
	
    private static class ServersImplInstance{
        private static final ServersImpl instance = new ServersImpl();
    }
    
    /*
     * singleton instance
     */
    public static ServersImpl getInstance(){
        return ServersImplInstance.instance;
    }
	
	public void start() {
		
		commandEventProcessor.start();
		commandServer.start();
		
		zkClient.start();
	}
	
	public void stop() {
		
		try {
			
			zkClient.stop();
			
			commandServer.shutdown();
			commandEventProcessor.stop();
		
		} finally {
		
			System.exit(0);
		
		}
	}

}
