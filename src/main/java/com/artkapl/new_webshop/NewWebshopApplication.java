package com.artkapl.new_webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // for periodic cleaning of orphaned images
public class NewWebshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewWebshopApplication.class, args);
	}

}
