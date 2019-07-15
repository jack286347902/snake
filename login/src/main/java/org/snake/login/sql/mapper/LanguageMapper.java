package org.snake.login.sql.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.snake.login.sql.domain.Language;

@Mapper
public interface LanguageMapper {

	@Insert("INSERT INTO language "
			+ "(language_name, create_time) "
			+ "VALUES "
			+ "(#{language_name}, #{create_time});")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void saveLanguage(Language region);

	@Select("SELECT * FROM language WHERE language_name = #{language_name}")	
	public Language selectLanguage(@Param("language_name") String language_name);

}
