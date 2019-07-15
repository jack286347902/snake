package org.snake.login.manager.user;

import java.util.Date;
import java.util.UUID;

import org.snake.login.command.client.manager.connection.CommandConnectionManager;
import org.snake.login.controller.login.data.LoginRequest;
import org.snake.login.sql.domain.Country;
import org.snake.login.sql.domain.Language;
import org.snake.login.sql.domain.ThirdChannel;
import org.snake.login.sql.domain.User;
import org.snake.login.sql.domain.UserLoginRank;
import org.snake.login.sql.domain.UserState;
import org.snake.login.sql.service.CountryService;
import org.snake.login.sql.service.LanguageService;
import org.snake.login.sql.service.ThirdChannelService;
import org.snake.login.sql.service.UserLoginRankService;
import org.snake.login.sql.service.UserService;
import org.snake.login.sql.service.UserStateService;
import org.snake.login.sql.util.CountryUtil;
import org.snake.login.sql.util.LanguageUtil;
import org.snake.login.sql.util.ThirdChannelUtil;
import org.snake.message.command.login.CreateUser;
import org.snake.message.command.login.LoadUser;
import org.snake.message.pool.MessagePool;
import org.snake.message.publics.UserLoginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.netty.channel.Channel;

@Service
public class UserManager {

	private final CountryService countryService;
	private final LanguageService languageService;
	private final ThirdChannelService thirdChannelService;
	private final UserService userService;
	
	private final UserStateService userStateService;
	
	private final CountryUtil countryUtil;
	private final LanguageUtil languageUtil;
	private final ThirdChannelUtil thirdChannelUtil;
	
	private final UserLoginRankService userLoginRankService;
	
	private final CommandConnectionManager commandConnectionManager = CommandConnectionManager.getInstance();
	
	@Autowired
	public UserManager(CountryService countryService, 
						LanguageService languageService, 
						ThirdChannelService thirdChannelService, 
						UserService userService, 
						CountryUtil countryUtil, 
						LanguageUtil languageUtil, 
						ThirdChannelUtil thirdChannelUtil, 
						UserStateService userStateService, 
						UserLoginRankService userLoginRankService) {
		
		this.countryService = countryService;
		this.languageService  = languageService;
		this.thirdChannelService = thirdChannelService;
		this.userService = userService;
		
		this.userStateService = userStateService;
		
		this.countryUtil = countryUtil;
		this.languageUtil = languageUtil;
		this.thirdChannelUtil = thirdChannelUtil;
		
		this.userLoginRankService = userLoginRankService;
	}
	
	public UserState login(String token) {
		
		UserState userState =  userStateService.selectUserState(token);
		
//		UserLoginState userLoginState = UserLoginState.values()[userState.getState()];
		
//		if(!userState.isValidateIPAndPort()
//				&& (UserLoginState.CREATING != userLoginState
//					&& UserLoginState.LOADING != userLoginState)) {
//			
//			Channel channel = commandConnectionManager.getCenterServerChannel(userState.getId());
//			
//			if(null != channel) {
//				
//				LoadUser createUser = (LoadUser)MessagePool.borrowMessage(LoadUser.CMD_INTEGER);
//				
//				createUser.setToken(token);
//				createUser.setUuid(userState.getUuid());
//				createUser.setSign(userState.getId());
//				
//				CommandMessageEvent event = new CommandMessageEvent();
//				
//				event.setMessage(createUser);
//				
//				channel.writeAndFlush(event);
//				
//				
//			} else {
//				return null;
//			}
//		}
		
		setRank(userState.getUuid(), userState);

		return userState;
		
	}
	
	public UserState login(LoginRequest loginRequest) {
		
		return login(loginRequest.getChannel(), 
					 loginRequest.getSubChannel(), 
					 loginRequest.getChannelUuid(), 
					 loginRequest.getPassword(), 
					 loginRequest.getCountry(), 
					 loginRequest.getLanguage(), 
					 new Date());
	}
	
	private UserState login(String channelName, 
						  String subChannelName, 
						  String channelUuid,
						  String password, 
						  String countryName, 
						  String languageName,
						  Date now) {
		
		ThirdChannel thirdChannel;
		ThirdChannel subThirdChannel;
		Language language;
		Country country;
		
		synchronized (this) {
			
			thirdChannel = getThirdChannel(channelName, now);
			
			subThirdChannel = getThirdChannel(subChannelName, now);
			
			language = getLanguage(languageName, now);
	
			country = getCountry(countryName, now);
		}
		
		return login(thirdChannel.getId(), 
					 subThirdChannel.getId(), 
					 channelUuid, 
					 password, 
					 country.getId(), 
					 language.getId(), 
					 now);
	}
	
	@Transactional
	private UserState login(int channelId, 
						   int subChannelId, 
						   String channelUuid,
						   String password, 
						   int countryId, 
						   int languageId,
						   Date now) {

				
		User user = getUser(channelId, subChannelId, channelUuid);
		
		UserState userState = null;
		String token = UUID.randomUUID().toString();
		
		if(!commandConnectionManager.hasCenterServerConnection())
			return null;
		
		if(null == user) {
			
			user = saveUser(channelId, 
					subChannelId, 
					 channelUuid, 
					 password, 
					 countryId, 
					 languageId, 
					 now);
			
			userState = createUserStateCreating(user.getId(), token, now);
			
			Channel channel = commandConnectionManager.getCenterServerChannel(userState.getId());
			
			if(null != channel) {
				
				CreateUser createUser = (CreateUser)MessagePool.borrowMessage(CreateUser.CMD_INTEGER);
				
				createUser.setToken(token);
				createUser.setUuid(user.getId());
				createUser.setSign(userState.getId());
				
				channel.writeAndFlush(createUser);
				
				
			} else {
				return null;
			}
		
		} else {
			
			// validate user
			
			
			// ip 不可用检测
			
			userState = userStateService.selectUserStateByUuid(user.getId());

			if(null != userState) {
				
				userState.setQuery_time(now);
				
				UserLoginState userLoginState = UserLoginState.values()[userState.getState()];

				if(UserLoginState.CREATING != userLoginState
						&& UserLoginState.LOADING != userLoginState) {
					
					userState.setState(UserLoginState.RELOGIN.getValue());
					
					userStateService.updateState(userState);
					
				} else {
				
					userStateService.updateQueryTime(userState);
				}
				
			} else {
				
				userState = createUserStateLoading(user.getId(), token, now);
				
				Channel channel = commandConnectionManager.getCenterServerChannel(userState.getId());
				
				if(null != channel) {
					
					LoadUser createUser = (LoadUser)MessagePool.borrowMessage(LoadUser.CMD_INTEGER);
					
					createUser.setToken(token);
					createUser.setUuid(user.getId());
					createUser.setSign(userState.getId());
					
					channel.writeAndFlush(createUser);
					
				} else {
					return null;
				}
				
			}
			
			
		}

		setRank(user.getId(), userState);

		userStateService.updateState(userState);
		
		return userState;
	}
	
	private void setRank(long uuid, UserState userState) {
		
		if(userState.getState() == UserLoginState.LOADING.getValue()
				|| userState.getState() == UserLoginState.CREATING.getValue())
			userState.setRank(userLoginRankService.selectUserLoginRankOrder(uuid));
		else
			userState.setRank(0);
		
	}
	
	private User getUser(long channelId, 
						long subChannelId, 
					    String channelUuid) {
		
		return userService.selectUser(channelId, subChannelId, channelUuid);
		
	}
	
	
	
	private User saveUser(int channelId, 
						  int subChannelId, 
						 String channelUuid,
						 String password, 
						 int countryId, 
						 int languageId,
						 Date now) {

			
		User user = createUser(channelId, 
							   subChannelId, 
							   channelUuid, 
							   password, 
							   countryId, 
							   languageId, 
							   now);
		
		userService.saveUser(user);
			
		return user;
	}
	
	private User createUser(int channelId, 
							int subChannelId, 
						   String channelUuid, 
						   String password, 
						   int countryId, 
						   int languageId, 
						   Date now) {
		
		User user = new User();
		
		user.setChannel_id(channelId);
		user.setSub_channel_id(subChannelId);
		user.setChannel_uuid(channelUuid);
		user.setPassword(password);
		user.setCountry_id(countryId);
		user.setLanguage_id(languageId);
		user.setCreate_time(now);
		
		return user;
		
	}
	
	private UserState createUserState(long uuid, String token, Date now) {
		
		UserState userState = new UserState();
		
		userState.setUuid(uuid);
		userState.setToken(token);
		userState.setIp("");
		userState.setQuery_time(now);
		userState.setCreate_time(now);
		
		
		return userState;
	}
	
	private UserState createUserStateCreating(long uuid, String token, Date now) {
		
		UserState userState = createUserState(uuid, token, now);

		userState.setState(UserLoginState.CREATING.getValue());
		
		userStateService.saveUserState(userState);
		
		UserLoginRank userLoginRank = new UserLoginRank();
		userLoginRank.setUuid(uuid);
		
		userLoginRankService.saveUserLoginRank(userLoginRank);
		
		return userState;
	}
	
	private UserState createUserStateLoading(long uuid, String token, Date now) {
		
		
		UserState userState = createUserState(uuid, token, now);

		userState.setState(UserLoginState.LOADING.getValue());
		
		userStateService.saveUserState(userState);
		
		UserLoginRank userLoginRank = new UserLoginRank();
		userLoginRank.setUuid(uuid);
		
		userLoginRankService.saveUserLoginRank(userLoginRank);
		
		return userState;
	}
	
	
	private Language getLanguage(String languageName,  Date now) {
		
		Language language = languageUtil.get(languageName);
		
		if(null == language) {
			
			language = languageService.selectLanguage(languageName);
			
			if(null == language)
				language = createLanguage(languageName, now);

			languageUtil.add(language.getLanguage_name(), language);
		}
	
		return language;
	}
	
	private Language createLanguage(String languageName,  Date now) {
		
		Language language = new Language();
		
		language.setLanguage_name(languageName);
		language.setCreate_time(now);
		
		languageService.saveLanguage(language);
		
		return language;
		
	}
	
	private Country getCountry(String countryName,  Date now) {
		
		Country country = countryUtil.get(countryName);
		
		if(null == country) {
			
			country = countryService.selectCountry(countryName);
			
			if(null == country)
				country = createCountry(countryName, now);
			
			countryUtil.add(country.getCountry_name(), country);
		}
		
		return country;
	}
	
	private Country createCountry(String countryName,  Date now) {
		
		Country country = new Country();
		
		country.setCountry_name(countryName);
		country.setCreate_time(now);
		
		countryService.saveCountry(country);
		
		return country;
		
	}
	
	private ThirdChannel getThirdChannel(String channelName,  Date now) {
		
		ThirdChannel thirdChannel = thirdChannelUtil.get(channelName);
		
		if(null == thirdChannel) {
			
			thirdChannel = thirdChannelService.selectThirdChannel(channelName);
			
			if(null == thirdChannel) 
				thirdChannel = createThirdChannel(channelName, now);

			thirdChannelUtil.add(thirdChannel.getChannel_name(), thirdChannel);
		}
		
		return thirdChannel;
	}
	
	private ThirdChannel createThirdChannel(String channelName,  Date now) {
		
		ThirdChannel thirdChannel = new ThirdChannel();
		
		thirdChannel.setChannel_name(channelName);
		thirdChannel.setCreate_time(now);
		
		thirdChannelService.saveThirdChannel(thirdChannel);
		
		return thirdChannel;
		
	}
}
