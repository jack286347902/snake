package org.snake.login.sql.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.snake.login.sql.domain.UserState;

@Mapper
public interface UserStateMapper {

	@Insert("INSERT INTO user_state "
			+ "(uuid, token, state, query_time, create_time) "
			+ "VALUES "
			+ "(#{uuid}, #{token}, #{state}, #{query_time}, #{create_time});")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void saveUserState(UserState userState);

	@Select("SELECT * FROM user_state WHERE token = #{token}")	
	public UserState selectUserState(@Param("token") String token);

	
	@Select("SELECT * FROM user_state WHERE uuid = #{uuid}")	
	public UserState selectUserStateByUuid(@Param("uuid") long uuid);
	
	@Delete("DELETE FROM user_state WHERE uuid = #{uuid}")	
	public void deleteUserState(@Param("uuid") long uuid);
	
	@Update("UPDATE user_state SET state=#{state}, query_time=#{query_time} WHERE id = #{id}")
	public void updateState(UserState userState);
	
	@Update("UPDATE user_state SET query_time=#{query_time} WHERE id = #{id}")
	public void updateQueryTime(UserState userState);
	
	@Update("UPDATE user_state SET state=#{state}, ip=#{ip}, port=#{port} WHERE id = #{id}")
	public void updateIpAndPort(UserState userState);
	
	@Update("UPDATE user_state SET state=#{state} WHERE uuid = #{uuid}")
	public void updateRemoveUser(@Param("state") int state, @Param("uuid") long uuid);

	
	@Update("UPDATE user_state SET ip='', port=-1 WHERE ip=#{ip} AND port=#{port}")
	public void updateConnectorShutdown(@Param("ip") String ip, @Param("port") int port);
}
