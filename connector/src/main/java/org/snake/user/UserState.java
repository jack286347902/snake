package org.snake.user;

public enum UserState {
	
	LOADED, 	// 加载数据成功
	LOGIN, 		// 客户端登录成功, 转发数据
	LOGOUT, 	// 客户端退出
	MOVING,		// 客户端迁移连接中

}
