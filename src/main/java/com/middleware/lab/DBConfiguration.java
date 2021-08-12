package com.middleware.lab;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
@SuppressWarnings("unused")
public class DBConfiguration {
	
	private String driverClassName;
	private String url;
	private String username;
	private String password;
	
	@Profile("dev")
	@Bean
	public String devDatabaseConnection() {
		System.out.println("Conexion a dev");
		System.out.println(url);
		return ("dev");
		
	}
	
	@Profile("production")
	@Bean
	public String productionDatabaseConnection() {
		System.out.println("Conexion a production");
		System.out.println(url);
		return ("production");
		
	}
	
	@Profile("test")
	@Bean
	public String testDatabaseConnection() {
		System.out.println("Conexion a test");
		System.out.println(url);
		return ("test");
		
	}
	
}
