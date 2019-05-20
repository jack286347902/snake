package org.snake.connectorlogincener.logincenter.controller;


import java.io.UnsupportedEncodingException;

import org.snake.connectorlogincener.logincenter.IpPortToChannel;
import org.snake.testmessage.event.MessageEvent;
import org.snake.testmessage.login.ClientRemove;
import org.snake.testmessage.login.UserLoaded;
import org.snake.testmessage.login.UserMoving;
import org.snake.testmessage.login.UserMovingSuccess;
import org.snake.testmessage.pool.MessagePool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.netty.channel.Channel;


@Controller
public class TestController {
	
	private IpPortToChannel ipPortToChannel = IpPortToChannel.getInstance();

	@RequestMapping(value = "/userloaded/ipPort={ipPort}/token={token}/uuid={uuid}/transferIp={transferIp}/transferPort={transferPort}", method = RequestMethod.GET)
	@ResponseBody
	public void userloaded(@PathVariable("ipPort") String ipPort,
						   @PathVariable("token") String token,
						   @PathVariable("uuid") long uuid,
						   @PathVariable("transferIp") String transferIp,
						   @PathVariable("transferPort") int transferPort) throws UnsupportedEncodingException {
		
		System.err.println("userloadeduserloadeduserloadeduserloadeduserloadeduserloaded");
		sendUserLoaded(ipPort, token, uuid, transferIp, transferPort);
	}
	
	private void sendUserLoaded(String ipPort,
			   String token,
			   long uuid,
			   String transferIp,
			   int transferPort) throws UnsupportedEncodingException {
		
		UserLoaded userLoaded = 
				(UserLoaded)MessagePool.borrowMessage(UserLoaded.CMD_INTEGER);
		
		userLoaded.setToken(token);
		userLoaded.setUuid(uuid);
		userLoaded.setTransferIp(transferIp);
		userLoaded.setTransferPort(transferPort);
		
		MessageEvent messageEvent = new MessageEvent();
		
		messageEvent.setMessage(userLoaded);
		
		ipPort = new String(ipPort.getBytes(), "UTF-8");
		
		Channel channel = ipPortToChannel.get(ipPort);
		
		if(null != channel && channel.isActive())
			channel.writeAndFlush(messageEvent);
		
	}
	
	@RequestMapping(value = "/usermoving/ipPort={ipPort}/uuid={uuid}", method = RequestMethod.GET)
	@ResponseBody
	public void usermoving(@PathVariable("ipPort") String ipPort,
			@PathVariable("uuid") long uuid) {
		
		sendUserMoving(ipPort, uuid);
	}
	
	private void sendUserMoving(String ipPort,
			   long uuid) {
		
		UserMoving userMoving = 
				(UserMoving)MessagePool.borrowMessage(UserMoving.CMD_INTEGER);
		
		userMoving.setUuid(uuid);
		
		MessageEvent messageEvent = new MessageEvent();
		
		messageEvent.setMessage(userMoving);
		
		Channel channel = ipPortToChannel.get(ipPort);
		
		if(null != channel && channel.isActive())
			channel.writeAndFlush(messageEvent);
		
	}
	
	@RequestMapping(value = "/usermovingsuccess/ipPort={ipPort}/uuid={uuid}/transferIp={transferIp}/transferPort={transferPort}", method = RequestMethod.GET)
	@ResponseBody
	public void usermovingsuccess(@PathVariable("ipPort") String ipPort,
				@PathVariable("uuid") long uuid,
			   @PathVariable("transferIp") String transferIp,
			   @PathVariable("transferPort") int transferPort) {
		
		sendMovingSuccess(ipPort, uuid, transferIp, transferPort);
	}
	
	private void sendMovingSuccess(String ipPort,
			   long uuid,
			   String transferIp,
			   int transferPort) {
		
		UserMovingSuccess userMovingSuccess = 
				(UserMovingSuccess)MessagePool.borrowMessage(UserMovingSuccess.CMD_INTEGER);
		
		userMovingSuccess.setUuid(uuid);
		userMovingSuccess.setTransferIp(transferIp);
		userMovingSuccess.setTransferPort(transferPort);
		
		MessageEvent messageEvent = new MessageEvent();
		
		messageEvent.setMessage(userMovingSuccess);
		
		Channel channel = ipPortToChannel.get(ipPort);
		
		if(null != channel && channel.isActive())
			channel.writeAndFlush(messageEvent);
		
	}
	
	@RequestMapping(value = "/clientremove/ipPort={ipPort}/uuid={uuid}", method = RequestMethod.GET)
	@ResponseBody
	public void clientremove(@PathVariable("ipPort") String ipPort,
			@PathVariable("uuid") long uuid) {
		
		sendClientRemove(ipPort, uuid);
	}
	
	private void sendClientRemove(String ipPort,
			   long uuid) {
		
		ClientRemove clientRemove = 
				(ClientRemove)MessagePool.borrowMessage(ClientRemove.CMD_INTEGER);
		
		clientRemove.setUuid(uuid);
		
		MessageEvent messageEvent = new MessageEvent();
		
		messageEvent.setMessage(clientRemove);
		
		Channel channel = ipPortToChannel.get(ipPort);
		
		if(null != channel && channel.isActive())
			channel.writeAndFlush(messageEvent);
		
	}
}
