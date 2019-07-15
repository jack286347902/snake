package org.snake.login.sql.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.snake.login.sql.domain.Country;

@Mapper
public interface CountryMapper {

	
	@Insert("INSERT INTO country "
			+ "(country_name, create_time) "
			+ "VALUES "
			+ "(#{country_name}, #{create_time});")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void saveCountry(Country county);

	@Select("SELECT * FROM country WHERE country_name = #{country_name}")	
	public Country selectCountry(@Param("country_name") String country_name);

}
