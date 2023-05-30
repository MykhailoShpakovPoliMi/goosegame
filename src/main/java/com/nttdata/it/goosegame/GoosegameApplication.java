package com.nttdata.it.goosegame;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Goose game API", version = "1.0"),
		tags = {
				@Tag(name="Add Player", description = "add at least 2 players to the list of game players before starting the game"),
				@Tag(name="Start the game", description = "start the game in order to be able to move a player"),
				@Tag(name="Make a move", description = "displace a player on the game board by rolling the dice")
		},
		externalDocs = @ExternalDocumentation(
				description = "find out more about the game here",
				url = "https://www.slideshare.net/pierodibello/il-dilettevole-giuoco-delloca-coding-dojo"
		)
)
public class GoosegameApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoosegameApplication.class, args);
	}

}
