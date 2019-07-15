package org.snake.login.sql.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.snake.login.sql.domain.User;

@Mapper
public interface UserMapper {

	@Insert("INSERT INTO user "
			+ "(channel_id, sub_channel_id, channel_uuid, password, "
			+ "country_id, language_id, create_time) "
			+ "VALUES "
			+ "(#{channel_id}, #{sub_channel_id}, #{channel_uuid}, #{password}, "
			+ "#{country_id}, #{language_id}, #{create_time});")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int saveUser(User user);

	@Select("SELECT * FROM user WHERE channel_id = #{channel_id}"
			+ " AND sub_channel_id = #{sub_channel_id}"
			+ " AND channel_uuid = #{channel_uuid}")	
	public User selectUser(@Param("channel_id") long channel_id, 
						   @Param("sub_channel_id") long sub_channel_id, 
						   @Param("channel_uuid") String channel_uuid);


}
