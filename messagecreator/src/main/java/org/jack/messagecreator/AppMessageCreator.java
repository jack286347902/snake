package org.jack.messagecreator;

import java.io.File;

import org.jack.messagecreator.message.parser.MessageParser;
import org.jack.messagecreator.message.writer.JavaMessageWriter;

/**
 * Hello world!
 *
 */
public class AppMessageCreator 
{
    public static void main( String[] args ) throws Exception
    {
       
    	String inputPath = null;
    	String outputPath = null;
    	
    	switch (args.length) {

		case 1:
			
			inputPath = args[0];
					
			break;
		case 2:
			
			inputPath = args[0];
			outputPath = args[1];
			break;

		}
    	
    	
    	if(null == inputPath) {
    		
    		File directory = new File("");//设定为当前文件夹 
    		
    		inputPath = directory.getAbsolutePath();
    	
    		
    	}
    	if(null == outputPath) 
    		outputPath = inputPath + "\\src";

    	
    	File outPath = new File(outputPath);
    	outPath.mkdirs();
    	
    	MessageParser.getInstance().parse(inputPath);
    	
    	
    	JavaMessageWriter.getInstance().write(outputPath);
    	
    }
    
//	public static void main(String[] args) throws Exception {
//		
//		MessageParser mp = new MessageParser("G:\\Server arc\\design\\Server\\messagecreator\\testmessage.message");
//		
//		mp.parse();
//		
//	}
}
