package com.tuplazamimeta.policiaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PoliciaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoliciaApiApplication.class, args);
	}

}
