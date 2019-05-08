package org.jack.messagecreator.message;

public class MessageItem {
	
	private String option;
	private String type;
	private String itemName;
	private String itemCommont;
	
	public String toString() {
		
		StringBuilder result = new StringBuilder();
		
		result.append("\r\noption: ")
			  .append(option)
			  .append(" type: ")
			  .append(type)
			  .append(" itemName: ")
			  .append(itemName)
			  .append("commont: ")
			  .append(itemCommont);
		
		return result.toString();
	}
	
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemCommont() {
		return itemCommont;
	}
	public void setItemCommont(String itemCommont) {
		this.itemCommont = itemCommont;
	}

	
}
