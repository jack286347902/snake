package org.snake;

import org.snake.login.command.client.manager.message.CommandMessageManager;
import org.snake.login.command.zookeeper.ServersImpl;
import org.snake.login.command.zookeeper.ZkDataImpl;
import org.snake.login.command.zookeeper.ZkManagerImpl;
import org.snake.zookeeper.manager.zookeeper.ZkData;
import org.snake.zookeeper.servers.Servers;
import org.snake.zookeeper.zookeeper.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import lombok.Setter;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class AppLogin implements CommandLineRunner {
	
	public static ApplicationContext ctx = null;

	@Autowired
	@Setter
	private ZkClient zkClient;
	
	public static void main(String[] args) throws Exception {
	
		ctx = SpringApplication.run(AppLogin.class, args);

		CommandMessageManager.getInstance().init(ctx);
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
