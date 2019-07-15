package org.snake.login.sql.service;

import org.snake.login.sql.domain.UserLoginRank;
import org.snake.login.sql.mapper.UserLoginRankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginRankService {

	private final UserLoginRankMapper userLoginRankMapper;
	
	@Autowired
	public UserLoginRankService(UserLoginRankMapper userLoginRankMapper) {
		this.userLoginRankMapper = userLoginRankMapper;
	}
	
	
	public void saveUserLoginRank(UserLoginRank userLoginRank) {
		userLoginRankMapper.saveUserLoginRank(userLoginRank);
	}
	
	public void deleteUserLoginRank(long uuid) {
		userLoginRankMapper.deleteUserLoginRank(uuid);
	}
	
	
	public int selectUserLoginRankOrder(long uuid) {
		return userLoginRankMapper.selectUserLoginRankOrder(uuid);
	}
}
