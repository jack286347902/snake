package org.snake.login.sql.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseSqlUtil<V> {

	private Map<String, V> map = new ConcurrentHashMap<String, V>();
	
	public void add(String name, V V) {
		map.put(name, V);
	}

	public V get(String name) {
		return map.get(name);
	}
}
