package org.jack.messagecreator.message;

import java.util.LinkedList;
import java.util.List;

import org.jack.messagecreator.message.parser.MessageParser;

public class Message {

	private String type = null;
	private String commont = null;
	private int cmd;
	private String messageName;
	private int itemIndex = 0;
	private List<MessageItem> items = new LinkedList<MessageItem>();

	
	public String toString() {
		
		StringBuilder result = new StringBuilder();
		
		result.append("\r\ncommont: ")
			  .append(commont)
			  .append("\r\ncmd: ")
			  .append(cmd)
			  .append("\r\nmessage_name: ")
			  .append(messageName)
			  .append("\r\nitems: ");
		
		for(MessageItem item: items)
			result.append(item);
		
		return result.toString();
	}
	
	public boolean isMessageType() {
		return MessageParser.MESSAGE.equals(type);
	}
	
	public boolean isEnumType() {
		return MessageParser.ENUM.equals(type);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCommont() {
		return commont;
	}
	public void setCommont(String commont) {
		this.commont = commont;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public String getMessageName() {
		return messageName;
	}
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	public void addMessageItem(MessageItem item) {
		items.add(itemIndex++, item);
	}

	public List<MessageItem> getItems() {
		return items;
	}
	public void setItems(List<MessageItem> items) {
		this.items = items;
	}
	

}
