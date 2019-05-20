package org.snake.user.map;

// no sync, every user processes in its connect thread

public class UuidToUserInfo extends UserInfoMap<Long> {
	
	private UuidToUserInfo() {
		
	}
	
    private static class UuidToUserInfoInstance{
        private static final UuidToUserInfo instance = 
        						new UuidToUserInfo();
    }
    
    /*
     * singleton instance
     */
    public static UuidToUserInfo getInstance(){
        return UuidToUserInfoInstance.instance;
    }
}
