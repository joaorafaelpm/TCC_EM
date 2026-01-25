package com.pendezzapizza.pendezzapizza_api;

import com.pendezzapizza.pendezzapizza_api.infrastructure.repository.CustomJPARepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJPARepositoryImpl.class)
public class PendezzapizzaApiApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(PendezzapizzaApiApplication.class, args);
	}

}
