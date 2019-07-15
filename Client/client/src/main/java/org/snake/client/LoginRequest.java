package org.snake.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class LoginRequest {

	@NonNull
	private String channel;
	@NonNull
	private String subChannel;
	@NonNull
	private String channelUuid;
	private String password;
	@NonNull
	private String country;
	@NonNull
	private String language;
	

	
}
