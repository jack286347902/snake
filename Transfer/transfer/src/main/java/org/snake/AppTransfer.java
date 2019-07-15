package org.snake;

import org.snake.transfer.command.zookeeper.ServersImpl;
import org.snake.transfer.command.zookeeper.ZkDataImpl;
import org.snake.transfer.command.zookeeper.ZkManagerImpl;
import org.snake.zookeeper.manager.zookeeper.ZkData;
import org.snake.zookeeper.servers.Servers;
import org.snake.zookeeper.zookeeper.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.Setter;



@SpringBootApplication
public class AppTransfer  implements CommandLineRunner {
	
	@Autowired
	@Setter
	private ZkClient zkClient;
	
    public static void main( String[] args ) {
    	
    	SpringApplication.run(AppTransfer.class, args);

    	
    }

    
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		ServersImpl serversImpl = ServersImpl.getInstance();
		ZkManagerImpl zkManagerImpl = ZkManagerImpl.getInstance();
	
		ZkData zkData = new ZkDataImpl();
		
		serversImpl.setZkClient(zkClient);
		
		Servers servers = serversImpl;
		
		zkManagerImpl.init(zkData, zkClient, servers);
		
		servers.start();

	}

}
