package org.snake.login.sql.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Country {

	private int id;					// 0 不存在
	private String country_name;
	private Date create_time;
	

	
	
}
