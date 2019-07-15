package org.jack.messagecreator.message;

import java.util.HashMap;
import java.util.Map;


public class AllMessage {
	
	private Map<String, MessageGroup> mgMap = new HashMap<String, MessageGroup>();

	private AllMessage() {
		
	}
	
    private static class AllMessageInstance{
        private static final AllMessage instance = 
        						new AllMessage();
    }
    
    /*
     * singleton instance
     */
    public static AllMessage getInstance(){
        return AllMessageInstance.instance;
    }
    
    public void addMessageGroup(MessageGroup mGroup) {
    	mgMap.put(mGroup.getFileName(), mGroup);
    }
    
    public Map<String, MessageGroup> getMgMap() {
    	return mgMap;
    }

}
