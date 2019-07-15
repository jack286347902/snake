package org.snake.connector.command.zookeeper;

import org.snake.connector.command.client.CommandClientFactory;
import org.snake.connector.command.client.disruptor.CommandEventProcessor;
import org.snake.connector.transfer.server.Server;
import org.snake.zookeeper.servers.Servers;
import org.snake.zookeeper.zookeeper.ZkClient;

import lombok.Getter;
import lombok.Setter;

public class ServersImpl implements Servers {
	
	public static final long START_TIME = System.currentTimeMillis();

	@Getter
	private CommandEventProcessor commandEventProcessor = CommandEventProcessor.getInstance();
	
	@Setter
	private ZkClient zkClient;
	
	private Server server = new Server();
	
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
		
		zkClient.start();
		
		server.start();
	}
	
	public void stop() {
		
		try {
			
			server.shutdown();
			
			zkClient.stop();

			commandEventProcessor.stop();
			
			CommandClientFactory.getInstance().shutdown();
		
		} finally {
		
			System.exit(0);
		
		}
	}

}
