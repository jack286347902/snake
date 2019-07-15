package org.snake.message.publics;

import net.sf.json.JSONObject;

public enum ServerType {
	START(Integer.MIN_VALUE),
	CENTER_SERVER(1),
	LOGIN_WEB(2),
	CONNECTOR(3),
	TRANSFER_SERVER(4),
	LOGIC_SERVER(5),
	END(Integer.MAX_VALUE);

	private int value;
	private ServerType(int value) {
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

	public static ServerType get(int value) {

		switch (value) {
		case 1:
			return CENTER_SERVER;
		case 2:
			return LOGIN_WEB;
		case 3:
			return CONNECTOR;
		case 4:
			return TRANSFER_SERVER;
		case 5:
			return LOGIC_SERVER;
	}

		return END;
	}
}