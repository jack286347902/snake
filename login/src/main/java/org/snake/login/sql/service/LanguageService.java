package org.snake.login.sql.service;

import org.snake.login.sql.domain.Language;
import org.snake.login.sql.mapper.LanguageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageService {
	
	private final LanguageMapper languageMapper;
	
	@Autowired
	public LanguageService(LanguageMapper languageMapper) {
		this.languageMapper = languageMapper;
	}

	public void saveLanguage(Language language) {
		languageMapper.saveLanguage(language);
	}
	
	public Language selectLanguage(String language_name) {
		return languageMapper.selectLanguage(language_name);		
	}
}
