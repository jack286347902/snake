package org.snake.connector.transfer.user.map;

import java.util.HashMap;
import java.util.Map;

import org.snake.connector.transfer.user.UserInfo;

public abstract class UserInfoMap<T> {
	
	private Map<T, UserInfo> map = new HashMap<T, UserInfo>();
	
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
