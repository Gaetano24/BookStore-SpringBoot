package it.psw.bookstore;

import it.psw.bookstore.configurations.CorsFilterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class BookStoreSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreSpringbootApplication.class, args);
	}

}
