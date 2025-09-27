package com.pendezzafood.pendezzapizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.pendezzafood.pendezzapizza.infrastructure.repository.CustomJPARepositoryImpl;

import io.github.cdimascio.dotenv.Dotenv;


// Configuração para usar a implementação personalizada do repositório
// Graças as novas atualizações do Spring, a gente precisa especificar se estivermos usando uma implementação diferente do JPARepository caso contrário o Spring não reconhece os métodos personalizados
// Então nós usamos a anotação @EnableJpaRepositories para configurar o Spring Data JPA para usar a implementação personalizada do repositório
// Vale notar que ela não substitui a anotação @SpringBootApplication, mas reconhece como uma implementação adicional do JPARepository
@EnableJpaRepositories(
    basePackages = "com.pendezzafood.pendezzapizza.domain.repositories",
    repositoryBaseClass = CustomJPARepositoryImpl.class,
    considerNestedRepositories = true
)

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
