package test.client;

import java.util.List;

import abc.bcd.efg.Item;
import abc.bcd.efgh.AReq;
import abc.bcd.event.MessageEvent;
import abc.bcd.pool.MessagePool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class Client extends Thread {
	
	public static Client INSTANCE = null;
    
	public static EventLoopGroup group = null;

	
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
    

    
    public void writeMessage(Channel channel) throws Exception {
    	
    	
    	AReq message = (AReq)MessagePool.borrowMessage(AReq.CMD_INTEGER);
    	
    	message.setDd(0.001);
    	message.setFf(0.002f);
    	message.setI32(32);
    	message.setI64(64);
    	message.setIsArray(true);
    	message.setName("jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu"
    			+ "jack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liujack liu");
    	
    	Item item = message.getItem();
    	
    	fillItemBaseType(item, 66);
    	
    	double[] l1 = new double[]{10.1, 20.1};
    	float[] l2 = new float[]{0.002f, 30.2f};
    	int[] l3 = new int[]{11, 22, 33};
    	long[] l4 = new long[]{22, 55, 66};
    	boolean[] l5 = new boolean[]{true, false,  true};
    	String[] l6 = new String[]{"abc", "def", "dkdk"};
    	
    	message.setL1(l1);
    	message.setL2(l2);
    	message.setL3(l3);
    	message.setL4(l4);
    	message.setL5(l5);
    	message.setL6(l6);
    	
    	List<Item> list = message.getL7();
    	
    	for(int i = 0; i < 4; ++i) {
    		
    		Item iTemp = (Item)MessagePool.borrowMessage(Item.CMD_INTEGER);
    		
    		fillItem(iTemp, i);
    		
    		list.add(iTemp);
    	}
    		
	    for(int i = 0; i < 10000; ++i)  { 
	    	
	    	MessageEvent event = new MessageEvent();
	    	
	    	event.setMessage(message);
	    	
	    	channel.writeAndFlush(event);

    		
    	}
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
    	item.setName("jack liu");
    
    }
    private void fillItem(Item item, int sign) {
    	
    	fillItemBaseType(item, sign);
    	
    	double[] l1 = new double[]{10.1, 20.1};
    	float[] l2 = new float[]{0.002f, 30.2f};
    	byte[] l831 = new byte[]{0xf, 0x5};
    	short[] l31 = new short[]{10, 20};
    	int[] l3 = new int[]{11, 22, 33};
    	long[] l4 = new long[]{22, 55, 66};
    	boolean[] l5 = new boolean[]{true, false,  true};
    	String[] l6 = new String[]{"abc", "def", "dkdk"};
    	
    	item.setL1(l1);
    	item.setL2(l2);
    	item.setL831(l831);
    	item.setL31(l31);
    	item.setL3(l3);
    	item.setL4(l4);
    	item.setL5(l5);
    	item.setL6(l6);
    	
    	
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
