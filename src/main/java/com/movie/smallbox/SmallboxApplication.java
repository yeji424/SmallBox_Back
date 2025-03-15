package com.movie.smallbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:config/secu.properties")

public class SmallboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallboxApplication.class, args);
	}
}
