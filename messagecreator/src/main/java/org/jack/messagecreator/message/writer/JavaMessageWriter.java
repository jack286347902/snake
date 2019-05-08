package org.jack.messagecreator.message.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jack.messagecreator.message.AllMessage;
import org.jack.messagecreator.message.Message;
import org.jack.messagecreator.message.MessageGroup;
import org.jack.messagecreator.message.MessageItem;



public class JavaMessageWriter {
	
	
	private String parentPackage = null;
	
    private static class MessageWriterInstance{
        private static final JavaMessageWriter instance = 
        						new JavaMessageWriter();
    }
    
    private JavaMessageWriter()  {

    }
    
    /*
     * singleton instance
     */
    public static JavaMessageWriter getInstance(){
        return MessageWriterInstance.instance;
    }
    
    public void write(String outPath) throws Exception {
    	getParentPackage();
    	writeFile(outPath);
    }
	
	private void getParentPackage() throws Exception {
		
		Iterator<String> iter = AllMessage.getInstance().getMgMap().keySet().iterator();
		
		while(iter.hasNext() && null == parentPackage) {
			
			String abc = iter.next();
			
			String packagePath = AllMessage.getInstance().getMgMap().get(abc).getJavaPackage();
			
			int index = packagePath.lastIndexOf(".");
			
			if(-1 != index)
				parentPackage = packagePath.substring(0, index);
			else
				throw new Exception("ParentPackage error");
				
		}
	}
	
	private void clearDirectory(String outPath) {
		
		File directory = new File(outPath);
		
		if(directory.isDirectory()) {
						
			for(File mFile: directory.listFiles()) {
				
				if(mFile.isDirectory()) {
					clearDirectory(mFile.getAbsolutePath());
				} else {
					mFile.delete();
				}
				
			}
			
		} 
			
		directory.delete();

		
	}
	
	private void writeFile(String outPath) throws Exception {
		
		clearDirectory(outPath);
		
		Iterator<Entry<String, MessageGroup>> iter = AllMessage.getInstance().getMgMap().entrySet().iterator();
		
		while(iter.hasNext()) {
			
			MessageGroup mg = iter.next().getValue();
						
			String dirPath = outPath + "\\" + mg.getJavaPackage().replace('.', '\\');
			
			File directory = new File(dirPath);
			directory.mkdirs();
			
			
			for(Message message: mg.getMessages()) {
				
				if(message.isMessageType())
					writeMessageFile(mg, message, directory.getAbsolutePath());
				else if (message.isEnumType())
					writeEnumFile(mg, message, directory.getAbsolutePath());
				else
					throw new Exception("Unknown message type: " + message.getType());
				
			}
			
			
			writeBasePackage(outPath);
			
		}
	}
	
	
	private void writeBasePackage(String outPath) throws Exception {
		
		File file = new File("");
		
		String dir = file.getAbsolutePath() + "\\resources\\";
		
		String dirPath = outPath + "\\" + parentPackage.replace('.', '\\');
		
		copyFile(dir, dirPath, parentPackage, "", "Cmd.java");
		
		String imports = "\r\nimport " + parentPackage + ".pool.KeyedMessagePool;\r\n"; 
		copyFile(dir, dirPath, parentPackage, imports, "Message.java");
		copyFile(dir, dirPath, parentPackage, "", "ReferenceCount.java");
		
		dirPath = outPath + "\\" + (parentPackage + ".pool").replace('.', '\\');
	
		imports = "\r\nimport " + parentPackage + ".Message;\r\n" 
				+ "import " + parentPackage + ".factory.KeyedMessageFactory;\r\n\r\n";
		copyFile(dir, dirPath, parentPackage + ".pool", imports, "KeyedMessagePool.java");
		
		imports = "\r\nimport " + parentPackage + ".Message;\r\n\r\n";
		copyFile(dir, dirPath, parentPackage + ".pool", imports, "MessagePool.java");
		
		dirPath = outPath + "\\" + (parentPackage + ".event").replace('.', '\\');
		imports = "\r\nimport " + parentPackage + ".Message;\r\n"
				+ "import " + parentPackage + ".pool.MessagePool;\r\n\r\n";
		copyFile(dir, dirPath, parentPackage + ".event", imports, "MessageEvent.java");
		
		dirPath = outPath + "\\" + (parentPackage + ".factory").replace('.', '\\');
		
		createMessageFactory(dir, dirPath, parentPackage + ".factory", "KeyedMessageFactory.java");
	}
	
	
	
	private void copyFile(String dirPath, String outPath, String package_, 
			String imports, String fileName) throws Exception {
		
		File file = new File(dirPath + fileName);
		
		Long fileLength = file.length(); // 获取文件长度
        byte[] fileContent = new byte[fileLength.intValue()];
        
        FileInputStream in = null;
        try {
        	
            in = new FileInputStream(file);
            in.read(fileContent);
            
        } finally {
			
        	if(null != in)
        		in.close();
        	
		}
        
        File directory = new File(outPath);
        directory.mkdirs();
        
        File outFile = new File(outPath + "\\" + fileName);
		
		FileOutputStream out = new FileOutputStream(outFile);
        
		out.write(("package " + package_ + ";\r\n\r\n").getBytes("utf-8"));
		out.write(imports.getBytes("utf-8"));
		out.write(new String(fileContent, "utf-8").getBytes("utf-8"));
		
		out.flush();
		out.close();
	}
	
	
	public void createMessageFactory(String dirPath, String outPath, String package_, String fileName) throws Exception {
		
		
		Map<String, MessageGroup> map = AllMessage.getInstance().getMgMap();
		
		StringBuilder head = new StringBuilder();
		
		head.append("package ")
			   .append(parentPackage)
			   .append(".factory;\r\n\r\n");
		
		for(Map.Entry<String, MessageGroup> entry: map.entrySet()) {
			
			head.append("import ")
				   .append(entry.getValue().getJavaPackage())
				   .append(".*;\r\n");
			
		}
		
		head.append("\r\nimport ")
			.append(parentPackage)
			.append(".Message;\r\n\r\n");
		
		head.append("\r\npublic final class KeyedMessageFactory {\r\n\r\n")
		   .append("\tpublic static final int[] CMDS = new int[] {\r\n");
		
		StringBuilder creator = new StringBuilder();
		
		creator.append("\t/*\r\n")
		   .append("\t * create new message by message cmd\r\n")
		   .append("\t * @param cmd message unique cmd\r\n")
		   .append("\t * @return message new message\r\n")
		   .append("\t */\r\n")
		   .append("\tpublic static Message create(int cmd) {\r\n\r\n")
		   .append("\t\tswitch(cmd) {\r\n\r\n");
		
		
		
		boolean isNotFirstCmd = false;
		for(Map.Entry<String, MessageGroup> entry: map.entrySet()) {
			
			for(Message message: entry.getValue().getMessages()) {
				

				if(isNotFirstCmd)
					head.append(",\r\n");
				
				isNotFirstCmd = true;
				
				head.append("\t\t\t")
					   .append(message.getMessageName())
					   .append(".CMD_INT");
				
				creator.append("\t\t case ")
					   .append(message.getMessageName())
					   .append(".CMD_INT: \r\n")
					   .append("\t\t\treturn new ")
					   .append(message.getMessageName())
					   .append("();\r\n");
			}
			
		}
		
		head.append("\r\n\t\t\t};\r\n\r\n");
		
		creator.append("\t\t}\r\n\r\n")
			   .append("\t\treturn null;\r\n")
			   .append("\t}\r\n\r\n")
			   .append("}");
			   
		File directory = new File(outPath);
        directory.mkdirs();
        
        File outFile = new File(outPath + "\\" + fileName);
		
		FileOutputStream out = new FileOutputStream(outFile);
        
		out.write(head.toString().getBytes());
		out.write(creator.toString().getBytes());
		
		out.flush();
		out.close();
	}

	
	private void addCmd(int cmd, StringBuilder builder) {
		
		builder.append("\tpublic static final short CMD_SHORT = ")
			   .append(cmd)
			   .append(";\r\n")
			   .append("\tpublic static final int CMD_INT = ")
			   .append(cmd)
			   .append(";\r\n")
			   .append("\tpublic static final Integer CMD_INTEGER = ")
			   .append(cmd)
			   .append(";\r\n\r\n");
	}
	
	private void addCmdGetterAndSetter(StringBuilder getterAndSetter) {
		
		getterAndSetter.append("\r\n\r\n\t@Override\r\n")
		   			   .append("\tpublic Integer getCmdInteger() {\r\n")
		   			   .append("\t\treturn CMD_INTEGER;\r\n")
		   			   .append("\t}\r\n")
		   			   .append("\t@Override\r\n")
		   			   .append("\tpublic int getCmdInt() {\r\n")
		   			   .append("\t\treturn CMD_INT;\r\n")
		   			   .append("\t}\r\n")
		   			   .append("\t@Override\r\n")
		   			   .append("\tpublic short getCmdShort() {\r\n")
		   			   .append("\t\treturn CMD_SHORT;\r\n")
		   			   .append("\t}\r\n\r\n");
	}
	
	private static final Map<String, String> BASE_TYPE_MAP = createBaseTypeMap();
			
	private static Map<String, String> createBaseTypeMap() {
		
		Map<String, String> baseTypeMap = new HashMap<String, String>();
	
		baseTypeMap.put("double", "double");
		baseTypeMap.put("float", "float");
		baseTypeMap.put("int8", "byte");
		baseTypeMap.put("int16", "short");
		baseTypeMap.put("int32", "int");
		baseTypeMap.put("int64", "long");
		baseTypeMap.put("bool", "boolean");
		baseTypeMap.put("string", "String");
	
		return baseTypeMap;
	}
	
	
	private static final Map<String, String> BASE_BUFFER_READ_MAP = createBaseBufferReadMap();
	
	private static Map<String, String> createBaseBufferReadMap() {
		
		Map<String, String> baseTypeMap = new HashMap<String, String>();
	
		baseTypeMap.put("double", "buf.readDouble()");
		baseTypeMap.put("float", "buf.readFloat()");
		baseTypeMap.put("int8", "buf.readByte()");
		baseTypeMap.put("int16", "buf.readShort()");
		baseTypeMap.put("int32", "buf.readInt()");
		baseTypeMap.put("int64", "buf.readLong()");
		baseTypeMap.put("bool", "buf.readBoolean()");
		baseTypeMap.put("string", "readString(buf)");
	
		return baseTypeMap;
	}
	
	
	private static final Map<String, String> BASE_BUFFER_WRITE_MAP = createBaseBufferWriteMap();
	
	private static Map<String, String> createBaseBufferWriteMap() {
		
		Map<String, String> baseTypeMap = new HashMap<String, String>();
	
		baseTypeMap.put("double", "buf.writeDouble(");
		baseTypeMap.put("float", "buf.writeFloat(");
		baseTypeMap.put("int8", "buf.writeByte(");
		baseTypeMap.put("int16", "buf.writeShort(");
		baseTypeMap.put("int32", "buf.writeInt(");
		baseTypeMap.put("int64", "buf.writeLong(");
		baseTypeMap.put("bool", "buf.writeBoolean(");
	
		return baseTypeMap;
	}
	

	public static final String TYPE_STRING = "string";
	public static final String REQUIRED = "required";
	public static final String REPEATED = "repeated";

	private void writeMessageFile(MessageGroup mg, Message message, String outPath) throws Exception {
		
		StringBuilder head = new StringBuilder();
		
		head.append("package ")
			  .append(mg.getJavaPackage())
			  .append(";\r\n\r\n\r\n")
			  .append("import ")
			  .append(parentPackage)
			  .append(".Message;\r\n\r\n")
			  .append("import io.netty.buffer.ByteBuf;\r\n\r\n");
		
		Map<String, String> importMap = new HashMap<String, String>();
		
		
		StringBuilder body = new StringBuilder();
		
		if(null != message.getCommont()) {
			
			body.append(message.getCommont())
				.append("\r\n");
		}
		
		body.append("public final class ")
			.append(message.getMessageName())
			.append(" extends Message {\r\n\r\n");
		
		addCmd(message.getCmd(), body);
			
		StringBuilder getterAndSetter = new StringBuilder();
		
		addCmdGetterAndSetter(getterAndSetter);
		
		StringBuilder parseBody = new StringBuilder();
		
		parseBody.append("\r\n\t@Override\r\n")
				 .append("\tpublic void parse(ByteBuf buf) throws Exception {\r\n\r\n");
		
		StringBuilder arrayBody = new StringBuilder();
		arrayBody.append("\r\n\t@Override\r\n")
				 .append("\tpublic void array(ByteBuf buf) throws Exception {\r\n\r\n");
		
		StringBuilder releaseBody = new StringBuilder();
		releaseBody.append("\r\n\t@Override\r\n")
		 .append("\tpublic void releaseMessage() {\r\n\r\n");
		
		StringBuilder releaseClearBody = new StringBuilder();
		
		releaseClearBody.append("\r\n\t\tif(getRefCnt() == 0) {\r\n\r\n");
		
		StringBuilder retainBody = new StringBuilder();
		retainBody.append("\r\n\t@Override\r\n")
		 .append("\tpublic void retainMessage() {\r\n\r\n");
		
		StringBuilder toStringBody = new StringBuilder();
		toStringBody.append("\r\n\t@Override\r\n")
		 		.append("\tpublic String toString() {\r\n\r\n")
		 		.append("\t\tStringBuilder builder = new StringBuilder();\r\n\r\n")
		 		.append("\t\tbuilder.append(\"cmd=\")\r\n")
		 		.append("\t\t\t.append(getCmdInt())\r\n")
		 		.append("\t\t\t.append(\" refCnt = \")\r\n")
		 		.append("\t\t\t.append(getRefCnt());");
		
		for(MessageItem item: message.getItems()) {
			
			String name = item.getItemName();
			
			String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
			String commont = item.getItemCommont();
			
			if(null != commont) {
				
		 		body.append("\t ")
		 			.append(item.getItemCommont())
		 			.append("\r\n");
		 		
			}
			
			if(REQUIRED.equals(item.getOption())) {
				
				baseToString(name, toStringBody);
				
				String type = BASE_TYPE_MAP.get(item.getType());
				
				if(null == type) {
					
					type = item.getType();
					
					if(null == mg.getMessage(type)) {
						
						if(null == importMap.get(type)) {
						
							int found = 0;
						
							for(String fileName: mg.getImports()) {
								
								MessageGroup mgtemp = AllMessage.getInstance().getMgMap().get(fileName);
								
								if(null == mgtemp.getMessage(type))
									continue;
								else  {

									++found;
									
									importMap.put(type, mgtemp.getJavaPackage());
								}
									
								
							}
							
							if(0 == found)
								throw new Exception("not found message: " + type);
							else if (found > 1)
								throw new Exception("found message: " + type + " times: " + found);
						
						}
					
					}
					
					
					objectBody(type, name, body);
					objectGetterAndSetter(type, name, upperName, getterAndSetter);
					objectRetainBody(type, name, retainBody);

					objectReleaseBody(name, releaseBody);
					objectReleaseClearBody(name, releaseClearBody);
					
					
					
				} else {
				
								
					baseBody(type, name, body);					
					baseGetterAndSetter(type, name, upperName, getterAndSetter);
				
					baseReadBody(item, name, parseBody);
					baseWriteBody(item, name, arrayBody);
				
				}

			} else if (REPEATED.equals(item.getOption())) {
				
				String type = BASE_TYPE_MAP.get(item.getType());
				
//				arraySizeToString(name, toStringBody);
									
				if(null == type) {
					
					if(null == importMap.get("List")) {
						
						importMap.put("List", "java.util");
						importMap.put("LinkedList", "java.util");
						importMap.put("Iterator", "java.util");
					}
					
					type = item.getType();
					
					arrayObjectToString(type, name, toStringBody);
					
					objectArray(type, name, body);
					objectArrayGetterAndSetter(type, name, upperName, getterAndSetter);
					
					readObjectList(name, type, parseBody);
					writeObjectList(name, type, arrayBody);
					
					objectReleaseBodyList(name, type, releaseBody);
					objectReleaseClearBodyList(name, type, releaseClearBody);
					
				} else {
					
					arrayToString(type, name, toStringBody);
					
					baseArray(type, name, body);
					baseArrayGetterAndSetter(type, name, upperName, getterAndSetter);
										
					readBaseList(name, type, parseBody);
					writeBaseList(name, type, arrayBody);				

				}
				
			} else {
				
				throw new Exception("unknown option: file: " + mg.getFileName() + 
						" message: " + message.getMessageName() + " "
						+ "option: " + item.getOption());
			}
			 
			
		}
		
		File file = new File(outPath + "\\" + message.getMessageName() + ".java");
		
		FileOutputStream out = new FileOutputStream(file);
		
		for(Map.Entry<String, String> entry: importMap.entrySet()) {
			
			head.append("import ")
				.append(entry.getValue())
				.append(".")
				.append(entry.getKey())
				.append(";\r\n");
		}
		
		head.append("\r\n\r\n");
		
		out.write(head.toString().getBytes("UTF-8"));
		out.write(body.toString().getBytes("UTF-8"));
		out.write(getterAndSetter.toString().getBytes("UTF-8"));
		out.write(parseBody.toString().getBytes("UTF-8"));
		out.write(PARSE_END.getBytes("UTF-8"));
		out.write(arrayBody.toString().getBytes("UTF-8"));
		out.write(ARRAY_END.getBytes("UTF-8"));
		
		out.write(releaseBody.toString().getBytes("UTF-8"));
		out.write(releaseClearBody.toString().getBytes("UTF-8"));
		out.write(PARSE_CLEAR_END.getBytes("UTF-8"));
		out.write(RELEASE_END.getBytes("UTF-8"));
		
		
		out.write(retainBody.toString().getBytes("UTF-8"));
		out.write(RETAIN_END.getBytes("UTF-8"));
		
		out.write(toStringBody.toString().getBytes("UTF-8"));
		out.write(TO_STRING_END.getBytes("UTF-8"));

		
		out.flush();
		out.close();
	}
	
	
	
	public static final String TO_STRING_END = "\r\n\t\t"
			+ "return builder.toString();\r\n"
			+ "\r\n\t}\r\n"
			+ "}";
	public static final String PARSE_CLEAR_END = "\r\n\t\t\t"
			+ "MESSAGE_POOL.returnMessage(this);\r\n"
			+ "\r\n\t\t}\r\n";
	public static final String PARSE_END = "\r\n\t}\r\n";
	public static final String ARRAY_END = "\r\n\t\t"
			+ "release();\r\n"
			+ "\t}\r\n";
	
	public static final String RELEASE_END = "\r\n\t}\r\n";
	
	public static final String RETAIN_END = "\r\n\t}\r\n";
	
	private void baseToString(String name, StringBuilder builder) {
	
		builder.append("\r\n\t\tbuilder.append(\" ")
					.append(name)
					.append(" = \")\r\n")
					.append("\t\t\t.append(")
					.append(name)
					.append(");\r\n");
	
	}
	
	private void arraySizeToString(String name, StringBuilder builder) {
		
		builder.append("\r\n\t\tbuilder.append(\"")
		.append(name)
		.append(" size = \")\r\n")
		.append("\t\t\t.append(")
		.append(name)
		.append(".size());\r\n\r\n");

	}

	private void arrayToString(String type, String name, StringBuilder builder) {
		
		builder.append("\r\n\t\tif(null == ")
			   .append(name)
			   .append(")\r\n")
			   .append("\t\t\tbuilder.append(\" ")
			   .append(name)
			   .append(" = null \");\r\n")
			   .append("\t\telse {\r\n\r\n");
		
		builder.append("\t\t\tbuilder.append(\" ")
		.append(name)
		.append(" size = \")\r\n")
		.append("\t\t\t\t.append(")
		.append(name)
		.append(".length);\r\n\r\n");

		builder.append("\t\t\tbuilder.append(\" {\");\r\n")
		.append("\t\t\tfor(")
		.append(type)
		.append(" temp: ")
		.append(name)
		.append(")\r\n")
		.append("\t\t\t\tbuilder.append(temp).append(\", \");\r\n")
		.append("\t\t\tbuilder.append(\"} \");\r\n\r\n")
		.append("\t\t}\r\n\r\n");
	}
	
	private void arrayObjectToString(String type, String name, StringBuilder builder) {
		
		builder.append("\r\n\t\tbuilder.append(\" ")
		.append(name)
		.append(" size = \")\r\n")
		.append("\t\t\t.append(")
		.append(name)
		.append(".size());\r\n\r\n");

		builder.append("\t\tbuilder.append(\" {\");\r\n")
		.append("\t\tfor(")
		.append(type)
		.append(" temp: ")
		.append(name)
		.append(")\r\n")
		.append("\t\t\tbuilder.append(temp).append(\", \");\r\n")
		.append("\t\tbuilder.append(\"}\");\r\n\r\n");
	}
	

	
	private void baseBody(String type, String name, StringBuilder builder) {
		
		builder.append("\tprivate ")
			 	.append(type)
			 	.append(" ")
			 	.append(name)
			 	.append(";\r\n");
		
	}

	private void baseGetterAndSetter(String type, String name, String upperName, StringBuilder builder) {
		
		builder.append("\tpublic ")
			   .append(type)
			   .append(" get")
			   .append(upperName)
			   .append("() {\r\n")
			   .append("\t\treturn ")
			   .append(name)
			   .append(";\r\n")
			   .append("\t}")
			   .append("\r\n\tpublic void set")
			   .append(upperName)
			   .append("(")
			   .append(type)
			   .append(" ")
			   .append(name)
			   .append(") {\r\n")
			   .append("\t\tthis.")
			   .append(name)
			   .append(" = ")
			   .append(name)
			   .append(";\r\n")
			   .append("\t}\r\n");
	}


	private void baseReadBody(MessageItem item, String name, StringBuilder builder) {
		
		String readType = BASE_BUFFER_READ_MAP.get(item.getType());
		
		builder.append("\t\t")
				 .append(name)
				 .append(" = ")
				 .append(readType)
				 .append(";\r\n");
	}
	
	private void baseWriteBody(MessageItem item, String name, StringBuilder builder) {
	
		String writeType = BASE_BUFFER_WRITE_MAP.get(item.getType());
		
		if(null == writeType) {
			
			writeType = item.getType();
			
			if(TYPE_STRING.equals(writeType)) {
				
				builder.append("\r\n\t\twriteString(")
						 .append(name)
						 .append(", buf);\r\n");
			}
			
		} else {
			
			builder.append("\t\t")
				     .append(writeType)
				     .append(name)
				     .append(");\r\n");
		}

	}
	
	
	private void objectBody(String type, String name, StringBuilder builder) {
		
		builder.append("\tprivate ")
		 	.append(type)
		 	.append(" ")
		 	.append(name)
		 	.append(" = null;\r\n");
	
	}
	
	private void objectGetterAndSetter(String type, String name, String upperName, StringBuilder builder) {
	
		builder.append("\tpublic ")
			   .append(type)
			   .append(" get")
			   .append(upperName)
			   .append("() {\r\n")
			   .append("\t\treturn ")
			   .append(name)
			   .append(";\r\n")
			   .append("\t}\r\n");
	
	}
	
	private void objectRetainBody(String type, String name, StringBuilder builder) {
	
		builder.append("\t\tif(null == ")
			   .append(name)
			   .append(")\r\n")
			   .append("\t\t\t")
			   .append(name)
			   .append(" = (")
			   .append(type)
			   .append(")MESSAGE_POOL.borrowMessage(")
			   .append(type)
			   .append(".CMD_INTEGER);\r\n")
			   .append("\t\telse\r\n")
			   .append("\t\t\t")
			   .append(name)
			   .append(".retain();\r\n\r\n");
	
	}
	
	private void objectReleaseBody(String name, StringBuilder builder) {
	
		builder.append("\t\t")
			   .append(name)
			   .append(".release();\r\n");
	
	}
	
	private void objectReleaseClearBody(String name, StringBuilder builder) {
		builder.append("\t\t\t")
				.append(name)
				.append(" = null;\r\n");
	}

	
	private void objectArray(String type, String name, StringBuilder builder) {
		
		builder.append("\tprivate final List<")
				.append(type)
				.append("> ")
				.append(name)
				.append(" = new LinkedList<")
				.append(type)
				.append(">();\r\n");
	
	}
	
	private void objectArrayGetterAndSetter(String type, String name, String upperName, StringBuilder builder) {
		
		builder.append("\tpublic List<")
		   .append(type)
		   .append("> get")
		   .append(upperName)
		   .append("() {\r\n")
		   .append("\t\treturn ")
		   .append(name)
		   .append(";\r\n")
		   .append("\t}\r\n");
	
	}
	
	private void baseArray(String type, String name, StringBuilder builder) {
		builder.append("\tprivate ")
		.append(type)
		.append("[] ")
		.append(name)
		.append(";\r\n");
	}
	
	private void baseArrayGetterAndSetter(String type, String name, String upperName, StringBuilder builder) {
		builder.append("\tpublic ")
			   .append(type)
			   .append("[] get")
			   .append(upperName)
			   .append("() {\r\n")
			   .append("\t\treturn ")
			   .append(name)
			   .append(";\r\n")
			   .append("\t}\r\n")
			   .append("\tpublic void set")
			   .append(upperName)
			   .append("(")
			   .append(type)
			   .append("[] ")
			   .append(name)
			   .append(") {\r\n")
			   .append("\t\tthis.")
			   .append(name)
			   .append(" = ")
			   .append(name)
			   .append(";\r\n")
			   .append("\t}\r\n");
		
	}
	
	private void readObjectList(String name, String readType, StringBuilder builder) {
	
		String nameLen = name + "Len";
		String tempItemName = readType.substring(0, 1).toLowerCase() 
				+ readType.substring(1) + "Temp";
		
		builder.append("\r\n\t\tshort ")
			   .append(nameLen)
			   .append(" = buf.readShort();\r\n")
			   .append("\t\tfor(int i = 0; i < ")
			   .append(nameLen)
			   .append("; ++i) {\r\n")
			   .append("\t\t\t")
			   .append(readType)
			   .append(" ")
			   .append(tempItemName)
			   .append(" = (")
			   .append(readType)
			   .append(")MESSAGE_POOL.borrowMessage(")
			   .append(readType)
			   .append(".CMD_INTEGER);\r\n")
			   .append("\t\t\t")
			   .append(tempItemName)
			   .append(".parse(buf);\r\n")
			   .append("\t\t\t")
			   .append(name)
			   .append(".add(")
			   .append(tempItemName)
			   .append(");\r\n")
			   .append("\t\t}\r\n\r\n");
	}
	
	private void writeObjectList(String name, String type, StringBuilder builder) {
	
		String tempItemName = type.substring(0, 1).toLowerCase() 
				+ type.substring(1) + "Temp";
		
		builder.append("\r\n\t\tbuf.writeShort(")
			   .append(name)
			   .append(".size());\r\n")
			   .append("\t\tfor(")
			   .append(type)
			   .append(" ")
			   .append(tempItemName)
			   .append(": ")
			   .append(name)
			   .append(")\r\n")
			   .append("\t\t\t")
			   .append(tempItemName)
			   .append(".array(buf);\r\n\r\n");
			   
			   
	}
	
	private void readBaseList(String name, String readType, StringBuilder builder) {
		
		builder.append("\r\n\t\t")
			   .append(name)
			   .append(" = ")
			   .append("read")
			   .append(readType.substring(0, 1).toUpperCase())
			   .append(readType.substring(1))
			   .append("Array(buf);\r\n");
		
	}
	
	
	private void writeBaseList(String name, String writeType, StringBuilder builder) {
		
		builder.append("\r\n\t\t")
		   .append("write")
		   .append(writeType.substring(0, 1).toUpperCase())
		   .append(writeType.substring(1))
		   .append("Array(")
		   .append(name)
		   .append(", buf);\r\n");
	}
	
	private void objectReleaseBodyList(String name, String type, StringBuilder builder) {
		
		String iterName = name + "Iter";
		
		builder.append("\r\n\t\tIterator<")
			   .append(type)
			   .append("> ")
			   .append(iterName)
			   .append(" = ")
			   .append(name)
			   .append(".iterator();\r\n")
			   .append("\t\twhile(")
			   .append(iterName)
			   .append(".hasNext())\r\n")
			   .append("\t\t\t")
			   .append(iterName)
			   .append(".next().release();\r\n\r\n");
	
	}
	
	private void objectReleaseClearBodyList(String name, String type, StringBuilder builder) {

		String iterName = name + "Iter";
		
		builder.append("\r\n\t\t\t")
			   .append(iterName)
			   .append(" = ")
			   .append(name)
			   .append(".iterator();\r\n")
			   .append("\t\t\twhile(")
			   .append(iterName)
			   .append(".hasNext()) {\r\n")
			   .append("\t\t\t\t")
			   .append(iterName)
			   .append(".next().release();\r\n")
			   .append("\t\t\t\t")
			   .append(iterName)
			   .append(".remove();\r\n")
			   .append("\t\t\t}\r\n\r\n");
		
		
	}

	
	private void writeEnumFile(MessageGroup mg, Message message, String outPath) {
		
	}

}
