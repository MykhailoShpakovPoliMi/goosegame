package com.nttdata.it.goosegame;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Goose game API", version = "1.0"))
public class GoosegameApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoosegameApplication.class, args);
	}

}
