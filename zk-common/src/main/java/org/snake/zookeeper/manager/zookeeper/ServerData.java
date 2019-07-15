package org.snake.zookeeper.manager.zookeeper;

import org.snake.message.publics.ServerType;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONObject;


@Data
@NoArgsConstructor
public class ServerData {

	private ServerType serverType;
	private String ip;
	private int commandPort;
	private int dataPort;
	private long time;
	private boolean isRemoved = false;
	
	public String getUniquePath() {
	
		return serverType.getValue() + "_" + ip.replace(".", "_") + "_" + commandPort + "_" + dataPort + time;
	}
	
	public String toJsonString() {
		return JSONObject.fromObject(this).toString();
	}
	

}
