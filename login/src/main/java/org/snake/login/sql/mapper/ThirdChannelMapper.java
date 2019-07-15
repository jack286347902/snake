package org.snake.login.sql.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.snake.login.sql.domain.ThirdChannel;

@Mapper
public interface ThirdChannelMapper {
	
	@Insert("INSERT INTO third_channel "
			+ "(channel_name, create_time) "
			+ "VALUES "
			+ "(#{channel_name}, #{create_time});")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void saveThirdChannel(ThirdChannel thirdChannel);

	@Select("SELECT * FROM third_channel WHERE channel_name = #{channel_name}")	
	public ThirdChannel selectThirdChannel(@Param("channel_name") String channel_name);

}
