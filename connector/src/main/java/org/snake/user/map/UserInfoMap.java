package org.snake.user.map;

import java.util.HashMap;
import java.util.Map;

import org.snake.user.UserInfo;

public abstract class UserInfoMap<T> {
	
	private Map<T, UserInfo> map = new HashMap<T, UserInfo>(100000);
	
	public void add(T t, UserInfo userInfo) {
		map.put(t, userInfo);
	}
	
	public void remove(T t) {
		map.remove(t);
	}
	
	public UserInfo get(T t) {
		return map.get(t);
	}

}
