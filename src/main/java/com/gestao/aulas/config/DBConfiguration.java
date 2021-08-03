package com.gestao.aulas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties("spring.datasource")
@Getter
@Setter
public class DBConfiguration {

	private String url;
	private String username;
	private String password;
	private String driverClassName;

	@Profile("dev")
	@Bean
	public String testDatabaseConn() {
		System.out.println("Configurações de Desenvolvimento");
		System.out.println(url);
		return "DB Connection to mysql Dev";
		
	}

	@Profile("prod")
	@Bean
	public String prodDatabaseConn() {
		System.out.println("Configurações de Produção");
		System.out.println(url);
		return "DB Connection to mysql Production";
		
	}
}
