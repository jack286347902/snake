package org.snake;


import org.snake.command.client.CommandClient;
import org.snake.transfer.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 *
 */
@SpringBootApplication
public class AppConnector implements CommandLineRunner {


	@Autowired
	private Server server;
	
	@Autowired
	private CommandClient commandClient;

	public void setServer(Server server) {
		this.server = server;
	}

    public void setCommandClient(CommandClient commandClient) {
		this.commandClient = commandClient;
	}

	public static void main( String[] args )
    {
    	SpringApplication.run(AppConnector.class, args);
    	
    }

	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
				
		commandClient.connect("127.0.0.1", 8888);
		server.start();

	}


}
