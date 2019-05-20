package test.client;

import java.io.UnsupportedEncodingException;

import org.snake.testmessage.login.ClientLogin;
import org.snake.testmessage.pool.MessagePool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import test.client.handler.crypt.Crypt;


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
    
	private static final Crypt crypt = new Crypt("dsfklsdfjkl390890238".getBytes());
	
	public static Crypt getCrypt() {
		return crypt;
	}

	public ClientLogin createClientLogin() {
		
		ClientLogin clientLogin = (ClientLogin)MessagePool.borrowMessage(ClientLogin.CMD_INTEGER);
		
		clientLogin.setToken("dsfklsdfjkl390890238");
		
		return clientLogin;
	}
  
	private void writeMessage(Channel channel) throws UnsupportedEncodingException {
		
		FillEvent fillEvent = new FillEvent();
		
		ClientMessageEvent event0 = new ClientMessageEvent();
		
    	event0.setMessage(createClientLogin());
    	
    	channel.writeAndFlush(event0);
		

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for(int i = 0; i < 10000; ++i) {
			
	    	ClientMessageEvent event = new ClientMessageEvent();
			
	    	event.setMessage(fillEvent.createItemMessage(i));
	    	
	    	channel.writeAndFlush(event);
	    	
	    	ClientMessageEvent event2 = new ClientMessageEvent();
	    	
	    	event2.setMessage(fillEvent.createEmptyMessage());
	    	
	    	channel.writeAndFlush(event2);
	    	
	    	ClientMessageEvent event3 = new ClientMessageEvent();
	    	
	    	event3.setMessage(fillEvent.createSmallMessage(i));
	    	
	    	channel.writeAndFlush(event3);
	    	
	    	ClientMessageEvent event4 = new ClientMessageEvent();
	    	
	    	event4.setMessage(fillEvent.createFirstRequestMessage(i));
	    	
	    	channel.writeAndFlush(event4);
	    	
	    	ClientMessageEvent event5 = new ClientMessageEvent();
	    	
	    	event5.setMessage(fillEvent.createSecondRequestMessage(i));
	    	
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
