package com.pendezzafood.pendezzapizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PendezzapizzaApplication {

	
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
		.directory("./backend") // opcional
		.load();

		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("DATABASE_NAME", dotenv.get("DATABASE_NAME"));
		System.setProperty("DATABASE_PORT", dotenv.get("DATABASE_PORT"));

		SpringApplication.run(PendezzapizzaApplication.class, args);
	}

}
