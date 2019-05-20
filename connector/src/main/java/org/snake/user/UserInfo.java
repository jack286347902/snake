package org.snake.user;

import java.io.UnsupportedEncodingException;

import org.snake.testmessage.event.MessageEvent;
import org.snake.transfer.server.handler.crypt.Crypt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;


// login center notify user login
public class UserInfo {
	
	private String token;
	private Long uuid;	// 是否已存在uuid
	
	private String transferIp;
	private int transferPort;
	
	// modify at same time
	private Channel clientChannel;
	private Channel transferChannel;
	
	public EventLoop eventLoop;
	
	private volatile UserState state;
	
	private Crypt crypt;
	
	private ByteBuf sizeBuf = Unpooled.buffer(MessageEvent.CLIENT_SIZE_LENGTH);
	
	public void resetCrypt() throws UnsupportedEncodingException {
		
		if(null == crypt)
			crypt = new Crypt(token.getBytes("UTF-8"));
		else
			crypt.resetKey(token.getBytes("UTF-8"));
		
	}
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getUuid() {
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public String getTransferIp() {
		return transferIp;
	}
	public void setTransferIp(String transferIp) {
		this.transferIp = transferIp;
	}
	public int getTransferPort() {
		return transferPort;
	}
	public void setTransferPort(int transferPort) {
		this.transferPort = transferPort;
	}
	public Channel getClientChannel() {
		return clientChannel;
	}
	public void setClientChannel(Channel clientChannel) {
		this.clientChannel = clientChannel;
	}
	public Channel getTransferChannel() {
		return transferChannel;
	}
	public void setTransferChannel(Channel transferChannel) {
		this.transferChannel = transferChannel;
	}
	public EventLoop getEventLoop() {
		return eventLoop;
	}
	public void setEventLoop(EventLoop eventLoop) {
		this.eventLoop = eventLoop;
	}
	public UserState getState() {
		return state;
	}
	public void setState(UserState state) {
		this.state = state;
	}
	public Crypt getCrypt() {
		return crypt;
	}
	public void setCrypt(Crypt crypt) {
		this.crypt = crypt;
	}
	public ByteBuf getSizeBuf() {
		
		sizeBuf.clear();
		
		return sizeBuf;
	}
	
	
}
