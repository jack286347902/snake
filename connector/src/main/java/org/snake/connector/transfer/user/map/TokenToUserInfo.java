package org.snake.connector.transfer.user.map;

public class TokenToUserInfo extends UserInfoMap<String> {

	private TokenToUserInfo() {
		
	}
	
    private static class TokenToUserInfoInstance{
        private static final TokenToUserInfo instance = 
        						new TokenToUserInfo();
    }
    
    /*
     * singleton instance
     */
    public static TokenToUserInfo getInstance(){
        return TokenToUserInfoInstance.instance;
    }
}
