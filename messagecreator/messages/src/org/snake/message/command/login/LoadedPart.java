package org.snake.message.command.login;

import net.sf.json.JSONObject;

public enum LoadedPart {
	START(Integer.MIN_VALUE),
	USER(1),
	ROLE(2),
	MAIL(3),
	FRIEND(4),
	END(Integer.MAX_VALUE);

	private int value;
	private LoadedPart(int value) {
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

	public static LoadedPart get(int value) {

		switch (value) {
		case 1:
			return USER;
		case 2:
			return ROLE;
		case 3:
			return MAIL;
		case 4:
			return FRIEND;
	}

		return END;
	}
}