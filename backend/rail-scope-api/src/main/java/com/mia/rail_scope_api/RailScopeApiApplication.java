package com.mia.rail_scope_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RailScopeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RailScopeApiApplication.class, args);
	}

}
