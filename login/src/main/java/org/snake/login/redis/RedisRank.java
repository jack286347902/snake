package org.snake.login.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRank {
	
	private static final String KEY = "LOGIN";
	
	private final StringRedisTemplate stringRedisTemplate;

	@Autowired
	public RedisRank(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}
	
	public void add(String value, double score) {
		stringRedisTemplate.opsForZSet().add(KEY, value, score);
	}
	
	public void remove(String value) {
		stringRedisTemplate.opsForZSet().remove(KEY, value);
	}
	
	public Long getRank(String value) {
		return stringRedisTemplate.opsForZSet().rank(KEY, value);
	}
	
}

