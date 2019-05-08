package org.jack.messagecreator.message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MessageGroup {
	
	private String fileName;
	private List<String> imports = new LinkedList<String>();
	private String javaPackage;
	private int groupCmd;
	private List<Message> messages = new LinkedList<Message>();
	private Map<String, Message> messageMap = new HashMap<String, Message>();
	private int messageIndex = 0;
	private int messageCmd;
	
	
	public String toString() {
		
		StringBuilder result = new StringBuilder();
		result.append("fileName: ")
			  .append(fileName)
			  .append("\r\nimports: ");
		
		for(String temp: imports)
			result.append(temp).append(" ");
		
		result.append("\r\njava_package: ")
			  .append(javaPackage)
			  .append("\r\ngroup_cmd: ")
			  .append(groupCmd);
		
		result.append("\r\nmessages: ");
		
		for(Message message: messages)
			result.append(message);
		
		
		return result.toString();
	}
	
	public void addImports(String importFile) {
		imports.add(importFile);
	}
	
	public Message getMessage(String name) {
		return messageMap.get(name);
	}
	
	public void addMessage(Message message) {
		messages.add(messageIndex++, message);
		messageMap.put(message.getMessageName(), message);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		
		int positon = fileName.lastIndexOf("\\");
		
		this.fileName = fileName.substring(++positon);
	}

	public int getMessageCmd() {
		return ++messageCmd;
	}
	
	public List<String> getImports() {
		return imports;
	}
	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	public String getJavaPackage() {
		return javaPackage;
	}
	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}
	public int getGroupCmd() {
		return groupCmd;
	}
	public void setGroupCmd(int groupCmd) {
		this.messageCmd = this.groupCmd = groupCmd;	
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
	

}
