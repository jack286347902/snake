package org.snake.login.sql.domain;

import java.util.Date;

import org.snake.login.controller.login.data.LoginResult;

import lombok.Data;
import lombok.NoArgsConstructor;


// only create and search, modified by other server
@Data
@NoArgsConstructor
public class UserState {

	private long id;					// login order
	private long uuid;					// user.id
	private String token;
	private int state;					// create loading loaded login logout
	
	private String ip = "";					// connector ip
	private int port = -1;					// connector port
	
	private Date query_time;
	private Date create_time;
	
	private int rank;					// login rank
	
	public LoginResult toLoginResult() {
		
		LoginResult loginResult = new LoginResult();
		
		loginResult.setToken(token);
		loginResult.setState(state);
		loginResult.setRank(rank);
		loginResult.setIp(ip);
		loginResult.setPort(port);
		
		return loginResult;
	}
	
	public boolean isValidateIPAndPort() {
		
		if(ip.isEmpty() || -1 == port)
			return false;
		
		return true;
	}
	
	
}
