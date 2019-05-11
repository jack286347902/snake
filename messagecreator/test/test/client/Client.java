package test.client;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.snake.testmessage.m1.Empty;
import org.snake.testmessage.m1.Item;
import org.snake.testmessage.m1.Small;
import org.snake.testmessage.m2.FirstRequest;
import org.snake.testmessage.m3.SecondRequest;
import org.snake.testmessage.pool.MessagePool;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import test.client.handler.ClientMessageEvent;


public class Client extends Thread {
	
	public static Client INSTANCE = null;
    
	public EventLoopGroup group = null;

	
	private Client() {
		
	}
	
	public static Client getInstance() {
		
		if(null == INSTANCE)
			INSTANCE = new Client();
		
		return INSTANCE;
	}

    public static void main(String[] args) throws Exception {
    
    	
    	Client client = Client.getInstance();
    	
    	client.start();
    	
    }
    

    
    public FirstRequest CreateFirstRequestMessage(int i32) {
    	
    	
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
    	
    	
//    	message.setName("jack");
    	Item item = message.getAitem();
    	
//    	fillItemBaseType(item, i32);
    	
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
//    	item.setName("jack liu");
    	

    
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
    	
    	for(int i = 0; i < 2; ++i) {    		
    		fRequest.add(CreateFirstRequestMessage(i32++));
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
    
    
	private void writeMessage(Channel channel) throws UnsupportedEncodingException {
		
		for(int i = 0; i < 10000; ++i) {
			
			
	    	ClientMessageEvent event = new ClientMessageEvent();
			
	    	event.setMessage(createItemMessage(i));
	    	
	    	channel.writeAndFlush(event);
	    	
	    	ClientMessageEvent event2 = new ClientMessageEvent();
	    	
	    	event2.setMessage(createEmptyMessage());
	    	
	    	channel.writeAndFlush(event2);
	    	
	    	ClientMessageEvent event3 = new ClientMessageEvent();
	    	
	    	event3.setMessage(createSmallMessage(i));
	    	
	    	channel.writeAndFlush(event3);
	    	
	    	ClientMessageEvent event4 = new ClientMessageEvent();
	    	
	    	event4.setMessage(CreateFirstRequestMessage(i));
	    	
	    	channel.writeAndFlush(event4);
	    	
	    	ClientMessageEvent event5 = new ClientMessageEvent();
	    	
	    	event5.setMessage(createSecondRequestMessage(i));
	    	
	    	channel.writeAndFlush(event5);
			
		}
		

	}
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
    	try {
    		
    		group = new NioEventLoopGroup(1);

    		Bootstrap bootstrap = new Bootstrap();
    		bootstrap.group(group)
    			.channel(NioSocketChannel.class)
    			.handler(new ClientInitializer())
    			.option(ChannelOption.TCP_NODELAY, true);
    		
    		ChannelFuture f = bootstrap.connect("127.0.0.1", 
    								6666).sync();
    		
    		Channel channel = f.channel();
    		
    		
    		
	    	writeMessage(channel);
    		
    		channel.closeFuture().sync();

        	
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
        } finally {
        	
            group.shutdownGracefully();

        }
		
	}
	


    
    
    
    public void close() {
    	
    	if(null != group)
    		group.shutdownGracefully();

    }
     
    
    
	
}
