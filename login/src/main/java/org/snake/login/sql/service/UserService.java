package org.snake.login.sql.service;

import org.snake.login.sql.domain.User;
import org.snake.login.sql.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

	private final UserMapper userMapper;

	@Autowired
	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	public void saveUser(User user) {
    	userMapper.saveUser(user);
    }

	public User selectUser( long channel_id, 
							long sub_channel_id, 
						    String channel_uuid) {
		return userMapper.selectUser(channel_id, sub_channel_id, channel_uuid);
	}

}
