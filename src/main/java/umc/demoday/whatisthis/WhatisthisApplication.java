package umc.demoday.whatisthis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling // postToVectorDB scheduling 위해서
public class WhatisthisApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhatisthisApplication.class, args);
	}

}
