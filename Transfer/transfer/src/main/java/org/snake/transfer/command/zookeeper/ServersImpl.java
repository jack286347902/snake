package org.snake.transfer.command.zookeeper;

import org.snake.transfer.command.client.CommandClientFactory;
import org.snake.transfer.command.client.disruptor.CommandEventProcessor;
import org.snake.transfer.user.server.netty.server.TransferServer;
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
	
	private TransferServer server = TransferServer.getInstance();
	
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
