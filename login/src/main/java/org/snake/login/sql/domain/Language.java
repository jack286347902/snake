package org.snake.login.sql.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Language {

	private int id;
	private String language_name;
	private Date create_time;

}
