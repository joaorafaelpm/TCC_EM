package com.pendezzapizza.pendezzapizza_api;

import com.pendezzapizza.pendezzapizza_api.infrastructure.repository.CustomJPARepositoryImpl;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJPARepositoryImpl.class)
public class PendezzapizzaApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.load();

		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("DATABASE_NAME", dotenv.get("DATABASE_NAME"));
		System.setProperty("DATABASE_PORT", dotenv.get("DATABASE_PORT"));
		System.setProperty("API_PORT", dotenv.get("API_PORT"));

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(PendezzapizzaApiApplication.class, args);
	}

}
