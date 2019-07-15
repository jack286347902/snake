package org.snake.centerserver.command.server.manager.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.snake.message.publics.ServerType;

import io.netty.channel.Channel;


public class CommandConnectionManager {
	
	private Map<ServerType, List<SingleConnection>> conns = 
				new HashMap<ServerType, List<SingleConnection>>();
	
	private Map<Channel, SingleConnection> channelMap = 
				new HashMap<Channel, SingleConnection>();
	
	
	

	private ConnectionsComparator comparator = new ConnectionsComparator();
	
	private CommandConnectionManager() {
		
	}
	
    private static class CommandConnectionManagerInstance{
        private static final CommandConnectionManager instance = 
        						new CommandConnectionManager();
    }
    
    /*
     * singleton instance
     */
    public static CommandConnectionManager getInstance(){
        return CommandConnectionManagerInstance.instance;
    }
	
	public synchronized boolean addConnection(SingleConnection singleConnection) {
				
		List<SingleConnection> list = conns.get(singleConnection.getServerType());
		
		if(null == list) {
			
			list = new ArrayList<SingleConnection>();
			
			conns.put(singleConnection.getServerType(), list);
		}
		
		channelMap.put(singleConnection.getChannel(), singleConnection);
		list.add(singleConnection);
		
		Collections.sort(list, comparator);
		
		return true;
	}
	
	public synchronized void removeConnection(Channel channel) {
		
		SingleConnection conn = channelMap.get(channel);
		
		if(null != conn) {
			
			List<SingleConnection> list = conns.get(conn.getServerType());
			
			if(null != list)
				list.remove(conn);
			
			channelMap.remove(channel);
		}
		
	}
	
	public SingleConnection getSingleConnection(ServerType serverType, long sign) {
		
		List<SingleConnection> list = conns.get(serverType);
		
		if(list.isEmpty())
			return null;
		
		return list.get((int)(sign % list.size()));
	}
	
	public Channel getChannel(ServerType serverType, long sign) {
		
		List<SingleConnection> list = conns.get(serverType);
		
		if(list.isEmpty())
			return null;
		
		return list.get((int)(sign % list.size())).getChannel();
	}
	
	
	private class ConnectionsComparator implements Comparator<SingleConnection> {

		@Override
		public int compare(SingleConnection o1, SingleConnection o2) {
			// TODO Auto-generated method stub
			return o1.getTime() > o2.getTime() ? 1 : -1;
		}
		
	}

}
