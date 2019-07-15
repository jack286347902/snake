package org.snakle.login.controller.login;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.snake.login.controller.login.data.CheckLoginState;
import org.snake.login.controller.login.data.LoginRequest;
import org.snake.login.controller.login.data.LoginResult;

import java.util.Random;
import java.util.UUID;
import java.nio.charset.Charset;

import net.sf.json.JSONObject;

public class Login {
	
	
	public static void main(String[] args) throws Exception {
		
		LoginRequest loginRequest = new LoginRequest();
		
		int rand = new Random().nextInt(10);
		
		loginRequest.setChannel("channel" + rand);
		loginRequest.setSubChannel("subChannel" + rand);
		loginRequest.setChannelUuid("channelUuid" + rand);
		loginRequest.setPassword("password" + rand);
		loginRequest.setCountry("country" + rand);
		loginRequest.setLanguage("language" + rand);

		JSONObject jsonObject = JSONObject.fromObject(loginRequest);
		
		String token = httpPostWithJson(jsonObject, "http://localhost:8080/login", "" + rand);
		
		CheckLoginState checkLoginState = new CheckLoginState();
		
		checkLoginState.setToken(token);
		
		jsonObject = JSONObject.fromObject(checkLoginState);
		
		httpPostWithJson(jsonObject, "http://localhost:8080/check", "" + rand);
	}
	


	public static String httpPostWithJson(JSONObject jsonObj,String url,String appId){

	    String token = null;
	    HttpPost post = null;
	    try {
	        HttpClient httpClient = new DefaultHttpClient();

	        // 设置超时时间
	        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
	        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
	            
	        post = new HttpPost(url);
	        // 构造消息头
	        post.setHeader("Content-type", "application/json; charset=utf-8");
	        post.setHeader("Connection", "Close");
	        String sessionId = getSessionId();
	        post.setHeader("SessionId", sessionId);
	        post.setHeader("appid", appId);
	                    
	        // 构建消息实体
	        StringEntity entity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
	        entity.setContentEncoding("UTF-8");
	        // 发送Json格式的数据请求
	        entity.setContentType("application/json");
	        post.setEntity(entity);
	            
	        HttpResponse response = httpClient.execute(post);
	            
	        // 检验返回码
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != HttpStatus.SC_OK){


	        }else{
	            int retCode = 0;
	            String sessendId = "";
	            // 返回码中包含retCode及会话Id
	            for(Header header : response.getAllHeaders()){
	                if(header.getName().equals("retcode")){
	                    retCode = Integer.parseInt(header.getValue());
	                }
	                if(header.getName().equals("SessionId")){
	                    sessendId = header.getValue();
	                }
	            }
	            

            	String result=EntityUtils.toString(response.getEntity());
            	System.err.println(result);

                JSONObject jsonObject=JSONObject.fromObject(result);

                LoginResult loginResult = (LoginResult)JSONObject.toBean(jsonObject, LoginResult.class);
	            
                token = loginResult.getToken();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();

	    }finally{
	        if(post != null){
	            try {
	                post.releaseConnection();
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    return token;

	}

	// 构建唯一会话Id
	public static String getSessionId(){
	    UUID uuid = UUID.randomUUID();
	    String str = uuid.toString();
	    return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
	}

}
