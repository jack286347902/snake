package org.snake.login.controller.login;

import org.snake.login.controller.login.data.CheckLoginState;
import org.snake.login.controller.login.data.LoginRequest;
import org.snake.login.controller.login.data.LoginResult;
import org.snake.login.manager.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
	
	
	private final UserManager userManager;
	
	@Autowired
	public LoginController(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@RequestMapping(value = "/login", method= RequestMethod.POST)
	public LoginResult login(@RequestBody LoginRequest loginRequest) {
		
		return userManager.login(loginRequest).toLoginResult();
		 
	 }
	 
	@RequestMapping(value = "/check", method= RequestMethod.POST)
	public LoginResult check(@RequestBody CheckLoginState checkLoginState) {
		 
		return userManager.login(checkLoginState.getToken()).toLoginResult();
		 
	 }
	
	
	
}
