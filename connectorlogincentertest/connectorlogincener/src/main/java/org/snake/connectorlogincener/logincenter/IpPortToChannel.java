package org.snake.connectorlogincener.logincenter;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;

public class IpPortToChannel {
	
	private IpPortToChannel() {
		
	}
	
    private static class IpPortToChannelInstance{
        private static final IpPortToChannel instance = new IpPortToChannel();
    }
    
    /*
     * singleton instance
     */
    public static IpPortToChannel getInstance(){
        return IpPortToChannelInstance.instance;
    }
	
	private Map<String, Channel> map = new HashMap<String, Channel>();
	
	public void add(String ipPort, Channel channel) {
		map.put(ipPort, channel);
	}
	
	public void remove(String ipPort) {
		map.remove(ipPort);
	}
	
	public Channel get(String ipPort) {		
		return map.get(ipPort);
	}

}
