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
@Table(name = "pull_applies_config")
public class PullAppliesApiConfig {

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
	
	@Column
	public String authParams;
	
	@Column
	public Date lastTime;
	
	@Column
	public Date nextTime;
	
	@Column
	public String frequency;
	
	@Column(columnDefinition = "TEXT")
	public String queries;
	
	@Column(columnDefinition = "TEXT")
	public String jobAppMapTemplate;
	
	
}
