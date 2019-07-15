package org.snake.login.sql.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ThirdChannel {
	
	private int id;
	private String channel_name;
	private Date create_time;
	
	
	
}
