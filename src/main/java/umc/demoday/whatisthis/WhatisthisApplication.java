package umc.demoday.whatisthis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class WhatisthisApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhatisthisApplication.class, args);
	}

}
