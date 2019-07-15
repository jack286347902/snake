package org.snake.login.controller.login.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class LoginResult {
	
	@NonNull
	private String token;
	@NonNull
	private int state;			// 当前状态
	@NonNull
	private int rank;			// 前面还有多少人
	@NonNull
	private String ip;
	@NonNull
	private int port;
	

}
