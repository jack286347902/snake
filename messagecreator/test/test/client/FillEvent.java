package test.client;

import java.util.List;

import org.snake.testmessage.Message;
import org.snake.testmessage.event.MessageEvent;
import org.snake.testmessage.login.ClientLogin;
import org.snake.testmessage.m1.Empty;
import org.snake.testmessage.m1.Item;
import org.snake.testmessage.m1.Small;
import org.snake.testmessage.m2.FirstRequest;
import org.snake.testmessage.m3.SecondRequest;
import org.snake.testmessage.pool.MessagePool;

public class FillEvent {
	


	  public FirstRequest createFirstRequestMessage(int i32) {
	    	
	    	
	    	FirstRequest message = (FirstRequest)MessagePool.borrowMessage(FirstRequest.CMD_INTEGER);
	    	
	    	message.setAdd(0.001);
	    	message.setAff(0.002f);
	    	message.setAi32(i32);
	    	message.setAi64(64);
	    	message.setAisArray(true);
	    	message.setAname("AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);"
	    			+ "AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);");
	    	
	    	
//	    	message.setName("jack");
	    	Item item = message.getAitem();
	    	
//	    	fillItemBaseType(item, i32);
	    	
	    	fillItem(item, i32);
	    	

	    	
	    	String[] l6 = new String[]{"abc", "def", "dkdk"};

	    	message.setAl6(l6);
	    	
	    	List<Item> list = message.getAl7();
	    	
	    	for(int i = 0; i < 4; ++i) {
	    		
	    		Item iTemp = (Item)MessagePool.borrowMessage(Item.CMD_INTEGER);
	    		
	    		fillItem(iTemp, i32 * 1000 + i);
	    		
	    		list.add(iTemp);
	    	}
	    	
	    	return message;
	   		


	    }
	        
	    private void fillItemBaseType(Item item, int sign) {
	    	
	    	item.setId(sign);
	    	item.setCount(sign);
	    	item.setFf(0.002f);
	    	item.setDd(0.3);
	    	item.setI8((byte) 8);
	    	item.setI16((short) 16);
	    	item.setI32(32);
	    	item.setI64(64);
	    	item.setIsArray(true);
//	    	item.setName("jack liu");
	    	

	    
	    }
	    private void fillItem(Item item, int sign) {
	    	
	    	fillItemBaseType(item, sign);
	    	
	    	item.setName(sign + "      jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
	    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu");
	    	
	    	
	    	String[] l6 = new String[]{"abc", "def", "dkdk"};

	    	item.setL6(l6);
	    	
	    	
	    }
	    
	    public SecondRequest createSecondRequestMessage(int i32) {
	    	
	    	SecondRequest message = (SecondRequest)MessagePool.borrowMessage(SecondRequest.CMD_INTEGER);
	    	
	    	message.setAdd(11.11);
	    	
	    	message.setAdd(0.001);
	    	message.setAff(0.002f);
	    	message.setAi32(i32);
	    	message.setAi64(64);
	    	message.setAisArray(true);
	    	message.setAname(i32 + "         jack liu");
	    	
	    	Item item = message.getAitem();
	    	
	    	fillItem(item, i32);
	    	
	    	String[] l6 = new String[]{"abc", "def", "dkdk", "abc"};

	    	message.setAl6(l6);
	    	
	    	List<Item> al7 = message.getAl7();
	    	
	    	for(int i = 0; i < 5; ++i) {
	    		
	    		Item iTemp = (Item)MessagePool.borrowMessage(Item.CMD_INTEGER);
	    		
	    		fillItem(iTemp, i32 * 1000 + i);
	    		
	    		al7.add(iTemp);
	    	}
	    	
	    	List<FirstRequest> fRequest = message.getFRequest();
	    	
	    	for(int i = 0; i < 1; ++i) {    		
	    		fRequest.add(createFirstRequestMessage(i32++));
	    	}
	    	
	    	return message;
	    }
	    
	    
	    public Item createItemMessage(int i32) {
	    	
	    	Item message = (Item)MessagePool.borrowMessage(Item.CMD_INTEGER);
	    	
	    	fillItem(message, i32);
	    	
	    	return message;
	    }
	    
	    public Item createItemWithNullMessage(int i32) {
	    	
	    	Item message = (Item)MessagePool.borrowMessage(Item.CMD_INTEGER);
	    	
	    	fillItemBaseType(message, i32);
	    	
	    	return message;
	    }
				
	    
	    public Empty createEmptyMessage() {
	    	
	    	Empty message = (Empty)MessagePool.borrowMessage(Empty.CMD_INTEGER);
	    	
	    	return message;
	    }
	    
	    
	    public Small createSmallMessage(int i32) {
	    	
	    	Small message = (Small)MessagePool.borrowMessage(Small.CMD_INTEGER);
	    	
	    	message.setId(i32);
	    	message.setCount(i32);
	    	message.setFf(0.002f);
	    	message.setDd(0.3);
	    	message.setI8((byte) 8);
	    	message.setI16((short) 16);
	    	message.setI32(32);
	    	message.setI64(64);
	    	
	    	return message;
	    }
	    
	    
	    public void process(MessageEvent event) {
	    	
	    	Message message = event.getMessage();
	    	
	    	int sign = 0;
	    	
	    	switch (message.getCmdInt()) {
			case Empty.CMD_INT:
				
				message.release();
				
				message = MessagePool.borrowMessage(Empty.CMD_INT);

				break;
				
			case Item.CMD_INT:
				
				Item item = (Item)message;
				
				sign = item.getId();
				
				message.release();
				
				item = (Item)MessagePool.borrowMessage(Item.CMD_INT);
				
				message = createItemMessage(sign);
				
				break;
				
			case Small.CMD_INT:
				
				Small small = (Small)message;
				
				sign = small.getI32();
				
				message.release();
				
				small = (Small)MessagePool.borrowMessage(Small.CMD_INT);
				
				message = createSmallMessage(sign);
				
				break;
				
			case FirstRequest.CMD_INT:
				
				
				FirstRequest fr = (FirstRequest)message;
				
				sign = fr.getAi32();
				
				message.release();
				
				fr = (FirstRequest)MessagePool.borrowMessage(FirstRequest.CMD_INT);
				
				message = createFirstRequestMessage(sign);
				
				break;
				
			case SecondRequest.CMD_INT:
				
				SecondRequest sr = (SecondRequest)message;
				
				sign = sr.getAi32();
				
				message.release();
				
				sr = (SecondRequest)MessagePool.borrowMessage(SecondRequest.CMD_INT);
				
				message = createSecondRequestMessage(sign);
				
				break;

			default:
				break;
			}
	    	
	    	event.setMessage(message);
	    	
	    	event.getChannel().writeAndFlush(event);
	    	
	    	
	    }
}
