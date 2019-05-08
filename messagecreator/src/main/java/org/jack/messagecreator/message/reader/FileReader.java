package org.jack.messagecreator.message.reader;

import java.io.File;
import java.io.FileInputStream;


public class FileReader {
	
	private static final String ENCODING = "UTF-8";
	
	public static String[] read(String fileName) throws Exception {
		
        File file = new File(fileName);
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
        
        String[] lines = new String(fileContent, ENCODING).split("\r\n");
        
        return lines;// 返回文件内容, UTF-8编码

	}
	


}
