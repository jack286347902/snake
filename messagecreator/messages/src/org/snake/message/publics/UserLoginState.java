package org.snake.message.publics;

import net.sf.json.JSONObject;

public enum UserLoginState {
	START(Integer.MIN_VALUE),
	CREATING(1),
	LOADING(2),
	LOADED(3),
	LOGIN(4),
	RELOGIN(5),
	LOGOUT(6),
	REMOVE(7),
	END(Integer.MAX_VALUE);

	private int value;
	private UserLoginState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String toString() {
		JSONObject object = new JSONObject();
		object.put("value", value);
		return object.toString();
	}

	public static UserLoginState get(int value) {

		switch (value) {
		case 1:
			return CREATING;
		case 2:
			return LOADING;
		case 3:
			return LOADED;
		case 4:
			return LOGIN;
		case 5:
			return RELOGIN;
		case 6:
			return LOGOUT;
		case 7:
			return REMOVE;
	}

		return END;
	}
}