package org.snake.login.command.zookeeper;

import org.snake.login.command.client.CommandClientFactory;
import org.snake.login.command.client.disruptor.CommandEventProcessor;
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
	}
	
	public void stop() {
		
		try {
			
			zkClient.stop();

			commandEventProcessor.stop();
			
			CommandClientFactory.getInstance().shutdown();
		
		} finally {
		
			System.exit(0);
		
		}
	}

}
