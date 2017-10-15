package com.zwayam.appliespuller.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

	public static String URL;
	public static String USERNAME;
	public static String PASSWORD;
	public static String MAINDOMAIN;

	
	
	
	@Value("${spring.datasource.url}")
	public void setURL(String url) {
		AppConstants.URL = url;
	}
	
	@Value("${spring.datasource.username}")
	public void setUSERNAME(String username) {
		AppConstants.USERNAME = username;
	}
	
	@Value("${spring.datasource.password}")
	public void setPASSWORD(String password) {
		AppConstants.PASSWORD = password;
	}
	
	@Value("${careersite.maindomain}")
	public void setMainDomain(String domain) {
		AppConstants.MAINDOMAIN = domain;
	}
	
}