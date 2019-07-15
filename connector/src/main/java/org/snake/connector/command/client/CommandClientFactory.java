package org.snake.connector.command.client;

import org.snake.zookeeper.manager.zookeeper.ServerData;


import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;



public class CommandClientFactory {
	
	private static final EventLoopGroup EVENT_LOOP_GROUP 
							= new NioEventLoopGroup();
	
	private CommandClientFactory() {
		
	}
	
    private static class CommandClientFactoryInstance{
        private static final CommandClientFactory instance = 
        						new CommandClientFactory();
    }
    
    /*
     * singleton instance
     */
    public static CommandClientFactory getInstance(){
        return CommandClientFactoryInstance.instance;
    }
    
	public void connect(ServerData serverData) {
		
		new SingleConnection(serverData).doConnect(EVENT_LOOP_GROUP);

	}


	public void shutdown() {
		
		EVENT_LOOP_GROUP.shutdownGracefully();
		
	}

     
    
    
	
}
