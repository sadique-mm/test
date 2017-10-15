package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan("com")
public class Application {

	public static void main(String[] args) {
		System.out.println("Starting..!!");
		System.getProperties().put( "server.port","${applies.puller.server.port}");
		SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
		System.out.println("Done..!!");
	}
	
}