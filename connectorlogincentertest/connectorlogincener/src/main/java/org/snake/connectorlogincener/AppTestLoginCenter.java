package org.snake.connectorlogincener;

import org.snake.connectorlogincener.logincenter.LoginCenterServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */

@SpringBootApplication
public class AppTestLoginCenter implements CommandLineRunner {
	
	@Autowired
	private LoginCenterServer loginCenterServer;

    public void setLoginCenterServer(LoginCenterServer loginCenterServer) {
		this.loginCenterServer = loginCenterServer;
	}

	public static void main( String[] args ) {

    	SpringApplication.run(AppTestLoginCenter.class, args);
    }

	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		loginCenterServer.start();
	}
}
