package com.github.macgarcia.cast_challenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(enableDefaultTransactions = false)
public class CastChallengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CastChallengerApplication.class, args);
	}

}
