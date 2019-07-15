package org.snake.login.sql.service;

import org.apache.ibatis.annotations.Param;
import org.snake.login.sql.domain.UserState;
import org.snake.login.sql.mapper.UserStateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStateService {
	
	private final UserStateMapper userStateMapper;
	
	@Autowired
	public UserStateService(UserStateMapper userStateMapper) {
		this.userStateMapper = userStateMapper;
	}

	public void saveUserState(UserState userState) {
		userStateMapper.saveUserState(userState);
	}
	
	
	public UserState selectUserState(String token) {
		return userStateMapper.selectUserState(token);
	}
	
	public UserState selectUserStateByUuid(long uuid) {
		return userStateMapper.selectUserStateByUuid(uuid);
	}
	
	public void deleteUserState(long uuid) {
		userStateMapper.deleteUserState(uuid);
	}
	
	public void updateState(UserState userState) {
		userStateMapper.updateState(userState);
	}
	
	public void updateQueryTime(UserState userState) {
		userStateMapper.updateQueryTime(userState);
	}
	
	public void updateIpAndPort(UserState userState) {
		userStateMapper.updateIpAndPort(userState);
	}

	public void updateRemoveUser(int state, long uuid) {
		userStateMapper.updateRemoveUser(state, uuid);
	}
	
	public void updateConnectorShutdown(@Param("ip") String ip, @Param("port") int port) {
		userStateMapper.updateConnectorShutdown(ip, port);
	}
}
