package org.snake.login.sql.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


// 区域： 中国南部、北方

@Data
@NoArgsConstructor
public class User{
	
	private long id;
	private int channel_id;
	private int sub_channel_id;
	private String channel_uuid;
	private String password;
	
	private int country_id;
	private int language_id;
	
	private Date create_time;

	
}
