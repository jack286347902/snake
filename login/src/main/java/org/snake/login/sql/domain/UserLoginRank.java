package org.snake.login.sql.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


// only create and search, modified by other server
@Data
@NoArgsConstructor
public class UserLoginRank {

	private long id;					// login order
	private long uuid;					// user.id

	
	
}
