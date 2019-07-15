package org.snake.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.snake.client.handler.Crypt;
import org.snake.message.login.ClientLogin;
import org.snake.message.pool.MessagePool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import net.sf.json.JSONObject;


public class Client extends Thread {
	
	public static Client INSTANCE = null;
    
	private EventLoopGroup group = new NioEventLoopGroup();

	private String token;
	private String ip;
	private int port;
	
	private int count;
	
	public void setCount(int count) {
		this.count = count;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
//	private Client() {
//		
//	}
//	
//	public static Client getInstance() {
//		
//		if(null == INSTANCE)
//			INSTANCE = new Client();
//		
//		return INSTANCE;
//	}
	


    public static void connect(int i) throws Exception {
    
    	
    	Client client = new Client();
    	
    	client.setCount(i);
    	
    	LoginRequest loginRequest = new LoginRequest();
    	
    	loginRequest.setChannel("1");
    	loginRequest.setSubChannel("2");
    	loginRequest.setChannelUuid("33i" + i);
    	loginRequest.setPassword("4");
    	loginRequest.setCountry("5");
    	loginRequest.setLanguage("6");
    	
    	JSONObject js = JSONObject.fromObject(loginRequest);
    	
    	String result = client.invokeLogUrl(js.toString(), "login");
    	    	
    	LoginResult loginResult = (LoginResult) JSONObject.toBean(JSONObject.fromObject(result), LoginResult.class);
    	
    	CheckLoginState checkLoginState = new CheckLoginState();
    	
    	checkLoginState.setToken(loginResult.getToken());
    	
    	js = JSONObject.fromObject(checkLoginState);
    	
    	Thread.sleep(500);
    	
    	result = client.invokeLogUrl(js.toString(), "check");
    	
    	
    	loginResult = (LoginResult) JSONObject.toBean(JSONObject.fromObject(result), LoginResult.class);
    	
    	client.setIp(loginResult.getIp());
    	client.setPort(loginResult.getPort());
    	client.setToken(loginResult.getToken());
    	
    	client.start();
    	
    }
    
	public static final AttributeKey<Crypt> ATTR_KEY 
							= AttributeKey.valueOf("Crypt");
    
	private Crypt crypt;
	
	public Crypt getCrypt() throws UnsupportedEncodingException {
		
		if(null == crypt)
			crypt = new Crypt(token.getBytes("UTF-8"));
		
		return crypt;
	}

	public ClientLogin createClientLogin() {
		
		ClientLogin clientLogin = (ClientLogin)MessagePool.borrowMessage(ClientLogin.CMD_INTEGER);
		
		clientLogin.setToken(token);
		
		return clientLogin;
	}
	
	public String invokeLogUrl(String params, String path) throws IOException {
		
		
		String urlHead = "http://192.168.0.105:81/" + path;
		
		int timeOut = 1000; // 1 min
		
		URL url = new URL(urlHead);
		HttpURLConnection mHttpConn = (HttpURLConnection) url.openConnection();

//		mHttpConn.setConnectTimeout(timeOut);
//		mHttpConn.setReadTimeout(timeOut);
		
		mHttpConn.setDoOutput(true);
		mHttpConn.setDoInput(true);
		
//		mHttpConn.setRequestProperty("Accept", "*/*");
//		mHttpConn.setRequestProperty("Accept-Language", "zh-CN, zh");
//		mHttpConn.setRequestProperty("Charset",
//				"UTF-8,ISO-8859-1,US-ASCII,ISO-10646-UCS-2;q=0.6");
//		mHttpConn.setRequestProperty(
//				"User-Agent",
//				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
//		mHttpConn.setRequestProperty("Connection", "Keep-Alive");
//		
//		mHttpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		mHttpConn.setRequestMethod("POST");
		mHttpConn.setRequestProperty("Content-Type", "application/json");
		

		
		PrintStream ps = new PrintStream(mHttpConn.getOutputStream());
	    ps.print(params);
	    ps.close();
		
//		mHttpConn.getOutputStream().write(params.getBytes("UTF-8"));
	    
//	    mHttpConn.getInputStream();
	    
	    int code = mHttpConn.getResponseCode();
		
	    BufferedReader br = new BufferedReader(new InputStreamReader(mHttpConn.getInputStream()));
	    String line = br.readLine();
	 
	    br.close();
	    
	    return line;
	}
	
	public static void main(String[] args) throws Exception {
		
		for(int i = 0; i < 1; ++i)
			connect(i);
		
	}
  
	private void writeMessage(Channel channel) throws UnsupportedEncodingException {
		
		FillEvent fillEvent = new FillEvent();
		
//		channel.writeAndFlush(fillEvent.createSmallMessage(11));

		System.err.println(System.currentTimeMillis());
		
    	channel.writeAndFlush(createClientLogin());
		

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	channel.writeAndFlush(fillEvent.createSmallMessage(11));
		
		for(int i = 0; i < 100000; ++i) {
			
//	    	ClientMessageEvent event = new ClientMessageEvent();
//			
//	    	event.setMessage(fillEvent.createItemMessage(i));
//	    	
//	    	channel.writeAndFlush(event);
	    	
//	    	ClientMessageEvent event2 = new ClientMessageEvent();
//	    	
//	    	event2.setMessage(fillEvent.createEmptyMessage());
//	    	
//	    	channel.writeAndFlush(event2);
//	    	

	    	
	    	channel.writeAndFlush(fillEvent.createSmallMessage(i + count * 1000000));
//	    	
//	    	ClientMessageEvent event4 = new ClientMessageEvent();
//	    	
//	    	event4.setMessage(fillEvent.createFirstRequestMessage(i));
//	    	
//	    	channel.writeAndFlush(fillEvent.createFirstRequestMessage(i));
//	    	
//	    	ClientMessageEvent event5 = new ClientMessageEvent();
//	    	
//	    	event5.setMessage(fillEvent.createSecondRequestMessage(i));
//	    	
//	    	channel.writeAndFlush(event5);
			
		}
		

	}
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
    	try {
    		

    		Bootstrap bootstrap = new Bootstrap();
    		bootstrap.group(group)
    			.channel(NioSocketChannel.class)
    			.handler(new ClientInitializer())
    			.option(ChannelOption.TCP_NODELAY, true);
    		
    		ChannelFuture f = bootstrap.connect(ip, 
    								port).sync();
    		
    		Channel channel = f.channel();
    		
    		channel.attr(ATTR_KEY).set(getCrypt());
    		
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
