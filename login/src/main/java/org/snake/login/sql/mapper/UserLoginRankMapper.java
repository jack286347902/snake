package org.snake.login.sql.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.snake.login.sql.domain.UserLoginRank;

@Mapper
public interface UserLoginRankMapper {

	@Insert("INSERT INTO user_login_rank "
			+ "(uuid) "
			+ "VALUES "
			+ "(#{uuid});")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void saveUserLoginRank(UserLoginRank userLoginRank);
	
	@Delete("DELETE FROM user_login_rank WHERE uuid = #{uuid}")
	public void deleteUserLoginRank(@Param("uuid") long uuid);
	
	@Select("SELECT COUNT(*) FROM user_login_rank WHERE id < "
			+ "(SELECT id FROM user_login_rank WHERE uuid = #{uuid})")	
	public int selectUserLoginRankOrder(@Param("uuid") long uuid);
}
