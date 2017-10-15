package com.zwayam.appliespuller.mysql.models;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "auth_api_config")
public class AuthApiConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer id;

	@Column
	public String name;
	
	@Column
	public String apiBaseUrl;
	
	@Column
	public String bodyDataType;
	
	@Column
	public String httpMethodType;
	
	@Column(columnDefinition = "TEXT")
	public String headerParams;
	
	@Column
	public String urlParams;
	
	@Column(columnDefinition = "TEXT")
	public String bodyParams;
		
}
