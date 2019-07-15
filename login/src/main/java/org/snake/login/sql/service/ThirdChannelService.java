package org.snake.login.sql.service;

import org.snake.login.sql.domain.ThirdChannel;
import org.snake.login.sql.mapper.ThirdChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdChannelService {

	private final ThirdChannelMapper thirdChannelMapper;
	
	@Autowired
	public ThirdChannelService(ThirdChannelMapper thirdChannelMapper) {
		this.thirdChannelMapper = thirdChannelMapper;
	}
	
	public void saveThirdChannel(ThirdChannel thirdChannel) {
		thirdChannelMapper.saveThirdChannel(thirdChannel);
	}
	
	public ThirdChannel selectThirdChannel(String channel_name) {
		return thirdChannelMapper.selectThirdChannel(channel_name);
	}

}
