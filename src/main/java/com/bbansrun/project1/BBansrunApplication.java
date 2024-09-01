package com.bbansrun.project1;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BBansrunApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});


		SpringApplication.run(BBansrunApplication.class, args);
	}

}
