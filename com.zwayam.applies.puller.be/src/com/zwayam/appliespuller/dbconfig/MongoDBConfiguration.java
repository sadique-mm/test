package com.zwayam.appliespuller.dbconfig;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages = "com.zwayam.appliespuller.mongo")
public class MongoDBConfiguration extends AbstractMongoConfiguration {

	static String host;
	static Integer port;
	static String dbName;

	@Value("${mongodb.host}")
	public void setHost(String host) {
		MongoDBConfiguration.host = host;
	}

	@Value("${mongodb.port}")
	public void setPort(Integer port) {
		MongoDBConfiguration.port = port;
	}

	@Value("${applies.puller.mongodb.dbname}")
	public void setDbName(String dbName) {
		MongoDBConfiguration.dbName = dbName;
	}

	protected String getMappingBasePackage() {
		return "com.zwayam.appliespuller.mongo";
	}

	@Override
	protected String getDatabaseName() {

		return dbName;
	}

	@Override
	public Mongo mongo() throws Exception {

		return new MongoClient(host, port);
	}

}
