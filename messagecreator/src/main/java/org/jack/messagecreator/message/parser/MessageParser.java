package org.jack.messagecreator.message.parser;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jack.messagecreator.message.AllMessage;
import org.jack.messagecreator.message.Message;
import org.jack.messagecreator.message.MessageGroup;
import org.jack.messagecreator.message.MessageItem;
import org.jack.messagecreator.message.reader.FileReader;

public class MessageParser {
	
	public static final Logger logger = LogManager.getLogger(MessageParser.class);
	
	public static final String IMPORT = "import";
	public static final String JAVA_PACKAGE = "java_package";
	public static final String GROUP_CMD = "group_cmd";
	public static final String MESSAGE = "message";
	public static final String ENUM = "enum";
	public static final String REQUIRED = "required";
	public static final String REPEATED = "repeated";
	
	public static final String SUFFIX = "message";
	
	
	public static final char SLASH = '/';
	public static final char ASTERISK = '*';
	public static final char MESSAGE_END = '}';
	
	public static final String MESSAGE_END_STR = "}";
	
	private String fileName;
	private String[] lines;
	private int lineNumber = 0;
	private int colNumber = 0;
	private StringBuilder messageCommont = new StringBuilder();
	
	private MessageParser() {
		
	}
	
    private static class MessageParserInstance{
        private static final MessageParser instance = 
        						new MessageParser();
    }
    
    /*
     * singleton instance
     */
    public static MessageParser getInstance(){
        return MessageParserInstance.instance;
    }
	
	
	public void parse(String inPath) throws Exception {
		
		logger.trace(inPath);
		
		File path = new File(inPath);
		
		if(path.isDirectory()) {
			
			File[] messageFiles = path.listFiles();
			
			for(File mFile: messageFiles) {
				
				if(mFile.isFile()) {
					
					String mFileName = mFile.getName();
					
					int position = mFileName.lastIndexOf(".");
										
					if(position < mFileName.length()) {
						
						String suffix = mFileName.substring(++position);
						
						if(null != suffix
								&& !suffix.isEmpty()
								&& suffix.equals(SUFFIX)) {
							
							reset(mFile.getAbsolutePath());
							
							parse();
						}
					
					}
				}
				
			}
			
		}
		
	}
	
	private void reset(String fileName) throws Exception {
		
		this.fileName = fileName;
		
		this.lines = FileReader.read(fileName);
		this.lineNumber = 0;
		this.colNumber = 0;
		this.messageCommont = new StringBuilder();
	}
	
	
	private void parse() throws Exception {
		
		MessageGroup messageGroup = new MessageGroup();
		messageGroup.setFileName(fileName);
		
		AllMessage.getInstance().addMessageGroup(messageGroup);
				
		while(lineNumber < lines.length) {
		
									
			if(isCommontStart()) {
				
				messageCommont.append("\r\n")
							  .append(parseCommont());
				
			} else {
				
				String option = parseString();
				
				if(null == option) {
					
					++lineNumber;
					colNumber = 0;
					continue;
					
				}					
				
				
				switch (option) {
				case IMPORT:
					
					messageGroup.addImports(parsePackageString(option));
					break;
					
				case JAVA_PACKAGE:
										
					messageGroup.setJavaPackage(parsePackageString(option));
					break;
					
				case GROUP_CMD:
										
					int groupCmd = Integer.parseInt(parseOptionValue(option));
					
					messageGroup.setGroupCmd(groupCmd);
					break;
					
				case MESSAGE:
					
					Message message = new Message();
					
					message.setType(MESSAGE);
					if(messageCommont.length() > 0) {
						
						message.setCommont(messageCommont.toString());
						messageCommont = new StringBuilder();
					
					}
					
					message.setCmd(messageGroup.getMessageCmd());
					
					String messageName = parseOptionValue(option);
					
					if(!isUpperCase(messageName.charAt(0)))
						throw new Exception("message: " + messageName + " need upper case first letter");
					
					message.setMessageName(messageName);
					
					parseMessageItem(message);
					
					messageGroup.addMessage(message);
					break;
				
				case ENUM:
					
					Message enumMessage = new Message();
					
					enumMessage.setType(ENUM);
					
					if(messageCommont.length() > 0) {
						
						enumMessage.setCommont(messageCommont.toString());
						messageCommont = new StringBuilder();
					
					}
					
					String enumName = parseOptionValue(option);
					
					if(!isUpperCase(enumName.charAt(0)))
						throw new Exception("message: " + enumName + " need upper case first letter");
					
					enumMessage.setMessageName(enumName);
					
					messageGroup.addMessage(enumMessage);
					
					parseEnumItem(enumMessage);
					break;
					
				default:
					
					++lineNumber;
					colNumber = 0;
					break;
				}
			}

		}		
		
		System.err.println(messageGroup);
		
		
	}
	
	private void parseEnumItem(Message message) throws Exception {
		
		while(lineNumber < lines.length) {
						
			skip();
			
			String itemOption = parseString();
			
			if(null != itemOption) {
				
				if (MESSAGE_END_STR.equals(itemOption)) {
					
					++lineNumber;
					colNumber = 0;
					
					return;
				
				}
				
				MessageItem item = new MessageItem();
				
				item.setOption(itemOption.toUpperCase());
				item.setType(parseString());
				
				if(isCommontStart())
					item.setItemCommont(parseCommont());
				else {
					++lineNumber;
					colNumber = 0;
				}
				
				message.addMessageItem(item);
				
			} else {
				
				String ex = "in file: " + fileName + ", line: " + lineNumber + ", col: " + colNumber
						+ ", message: " + message.getMessageName() + ", itemOption: " + itemOption + " = null";
				
				System.out.println(ex);
				
				++lineNumber;
				colNumber = 0;
			}
		}
		
				
	}
	
	private void parseMessageItem(Message message) throws Exception {
		
		while(lineNumber < lines.length) {
						
			skip();
			
			String itemOption = parseString();
			
			if(null != itemOption) {
				
				if (MESSAGE_END_STR.equals(itemOption)) {
					
					++lineNumber;
					colNumber = 0;
					
					return;
				
				}
				
				MessageItem item = new MessageItem();
				
				item.setOption(itemOption);
				item.setType(parseString());
				item.setItemName(parseString());
				
				if(isCommontStart())
					item.setItemCommont(parseCommont());
				else {
					++lineNumber;
					colNumber = 0;
				}
				message.addMessageItem(item);
				
			} else {
				
				String ex = "in file: " + fileName + ", line: " + lineNumber + ", col: " + colNumber
						+ ", message: " + message.getMessageName() + ", itemOption: " + itemOption + " = null";
				
				System.out.println(ex);
				
				++lineNumber;
				colNumber = 0;
			}
		}
		
				
	}
	
	
	/*
	 * group_cmd
	 * message
	 * enum
	 */
	private String parseOptionValue(String option) throws Exception {
		
		String temp = parseString();
		
		if(null == temp) {
			
			String ex = "in file: " + fileName + ", line: " + lineNumber + ", col: " + colNumber
					+ ", option: " + option + " = null";
			
			throw new Exception(ex);
			
		} 
		
		++lineNumber;
		colNumber = 0;
		
		return temp;
	}
	
	/*
	 * import
	 * java_package
	 */
	private String parsePackageString(String option) throws Exception {
		
		skip();
		
		String line = lines[lineNumber]; 
		
		int wordStartColNumber = colNumber;
		
		while(colNumber < line.length()) {
			
			if(isPackLetter(line.charAt(colNumber)))
				++colNumber;
			else 
				break;
			
		}
		
		int wordEndColNumber = colNumber;
		
		++lineNumber;
		colNumber = 0;

		if(wordStartColNumber < wordEndColNumber)
			return line.substring(wordStartColNumber, wordEndColNumber);

		String ex = "in file: " + fileName + ", line: " + (lineNumber - 1) + ", col: " + colNumber
				+ ", option: " + option + " = null";
		
		throw new Exception(ex);

	}
	
	// null
	private String parseString() throws Exception {
		
		skip();
		
		String line = lines[lineNumber]; 
		
		int wordStartColNumber = colNumber;
		
		while(colNumber < line.length()) {	
			
			char c = line.charAt(colNumber);
			
			if(isMessageEnd(c)) {
				
				++lineNumber;
				colNumber = 0;
				
				return MESSAGE_END_STR;
				
			}
			
			if(isAlphanumeric(c))
				++colNumber;
			else 
				break;
			
		}

		if(wordStartColNumber < colNumber)
			return line.substring(wordStartColNumber, colNumber);
		
		return null;
	}
	
	
	
	private void skipEmptyLine() {
		
		String line = lines[lineNumber];
		
		while(line.isEmpty() 
				&& lineNumber < lines.length) {
			
			line = lines[++lineNumber];
			colNumber = 0;	
		}
	}
	
	private void skip() {
		
		skipEmptyLine();
		
		String line = lines[lineNumber]; 
		
		while(colNumber < line.length()) {
			
			char c = line.charAt(colNumber);
			
			if(!(isAlphanumeric(c)
					|| isSlash(c)
					|| isMessageEnd(c)))
				++colNumber;
			else 
				break;
			
		}
	}
	
	private boolean isUpperCase(char c) {
		return 'A' <= c && c <= 'Z';
	}
	
	private boolean isPackLetter(char c) {
		return ('a' <= c && c <= 'z') ||
               ('A' <= c && c <= 'Z') ||
               ('0' <= c && c <= '9') ||
               (c == '_') || 
               (c == '.');
	}
	
	private boolean isAlphanumeric(char c) {
		return ('a' <= c && c <= 'z') ||
               ('A' <= c && c <= 'Z') ||
               ('0' <= c && c <= '9') ||
               (c == '_');
	}

	private boolean isSlash(char c) {
		return SLASH == c;
	}
	
	private boolean isMessageEnd(char c) {
		return MESSAGE_END == c;
	}
	
	private String parseCommont() {
		
		skip();
				
		switch (commontStart()) {
		case LINE_COMMENT:
			return parseLineCommont();
		case BLOCK_COMMENT:
			return parseBlockCommont();
		case SLASH_NOT_COMMENT:
		case NO_COMMENT:
		
		}
		
		return null;
	}
	
	private String parseLineCommont() {
		
		String commont =  lines[lineNumber].substring(colNumber);
		
		++lineNumber;
		colNumber = 0;
		
		return commont;
	}
	

	
	private String parseBlockCommont() {
		
		StringBuilder commont = new StringBuilder();
		
		while(lineNumber < lines.length) {
			
			String line = lines[lineNumber];
			
			int position = colNumber;
			
			while(colNumber < line.length()) {
				
				if(isBlockCommontEnd(line)) {
					
					++colNumber;
					++colNumber;
					
					commont.append("\r\n")
					       .append(line.substring(position, colNumber));
					
					++lineNumber;
					colNumber = 0;
										
					return commont.toString();
					
				} else {
					++colNumber;
				}
			}
			
			commont.append("\r\n")
			       .append(line.substring(position, colNumber));
			
			++lineNumber;
			colNumber = 0;	
				
		}
		
		return null;
	}


	
	public static final int LINE_COMMENT = 0;
	public static final int BLOCK_COMMENT = 1;
	public static final int SLASH_NOT_COMMENT = 2;
	public static final int NO_COMMENT = 3;
	

	
	private boolean isCommontStart() {
		switch (commontStart()) {
		case LINE_COMMENT:
		case BLOCK_COMMENT:
			return true;

		}
		
		return false;
	}
	
	private int commontStart() {
		
		skip(); 
		
		String line = lines[lineNumber];
			
		if(line.length() - colNumber > 1) {
			
			if(line.charAt(colNumber) == SLASH) {
				
				char next = line.charAt(colNumber + 1);
				
				if(SLASH == next)
					return LINE_COMMENT;
				else if(ASTERISK == next)
					return BLOCK_COMMENT;
				else 
					return SLASH_NOT_COMMENT;
				
			}
		
		}

		
		return NO_COMMENT;
	}
	
	private boolean isBlockCommontEnd(String line) {
					
		if(line.length() - colNumber > 1) {
		
			if(line.charAt(colNumber) == ASTERISK) {
				
				if(SLASH == line.charAt(colNumber + 1))
					return true;
				
			}
		
		}

		return false;
		
	}
}
