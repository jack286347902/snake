package org.snake.login.sql.service;

import org.snake.login.sql.domain.Country;
import org.snake.login.sql.mapper.CountryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

	private final CountryMapper countryMapper;
	
	@Autowired
	public CountryService(CountryMapper countryMapper) {
		this.countryMapper = countryMapper;
	}

	public void saveCountry(Country Country) {
		countryMapper.saveCountry(Country);
	}
	
	public Country selectCountry(String Country_name) {
		return countryMapper.selectCountry(Country_name);		
	}
}
