package com.tomachocolate.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TomaChocolateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TomaChocolateApplication.class, args);
	}

}
