package com.nttdata.it.goosegame.web;

import com.nttdata.it.goosegame.service.controller.GameController;
import com.nttdata.it.goosegame.service.exceptions.*;
import com.nttdata.it.goosegame.service.view.GameView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
@RestController
@RequestMapping("/goose")
public class RequestHandler {
    private final GameController controller;
    private final GameView view;

    public RequestHandler(@Autowired GameController controller, @Autowired GameView view) {
        this.controller = controller;
        this.view = view;
    }

    @GetMapping("/add/{name}")
    @Operation(
            tags={"Add Player"},
            operationId = "addPlayer",
            summary = "Add not existent player to the game",
            description = "Add a new player providing a name",
            parameters = @Parameter(
                    name = "name",
                    description = "a name to be added to the list of players",
                    example = "John"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "List of all added players"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The name provided is already present in the list of players"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The game has been already started. Can't add players any more"
                    )
            }
    )
    public ResponseEntity<?> addPlayer(@PathVariable String name){

        String message;
        try {
            controller.addPlayer(name);
        }
        catch (DuplicateNameException ex) {
            message = view.getMessage();
            view.resetMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        catch (GameAlreadyStartedException ex1) {
            return new ResponseEntity<>(ex1.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        message = view.getMessage();
        view.resetMessage();
        //return message;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/start")
    @Operation(
            tags={"Start the game"},
            operationId = "startGame",
            summary = "Bring the game into the ready state",
            description = "After calling this endpoint you will no longer be able to add a new player",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The game was started successfully"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The are not enough players to start the game ( at least 2 players are required)"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The game has been already started."
                    )
            }
    )
    public ResponseEntity<?> startGame(){
        try {
            controller.startGame();
            return new ResponseEntity<>("The game has been started successfully", HttpStatus.OK);
        }
        catch (GameAlreadyStartedException ex1){
            return new ResponseEntity<>(ex1.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch (NotEnoughPlayersException ex2){
            return new ResponseEntity<>(ex2.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/move/{name}")
    @Operation(
            tags={"Make a move"},
            operationId = "movePlayer",
            summary = "Move existent player on the game board",
            description = "Move an existent player providing it's name and 2 values of dice rolls (optional)",
            parameters = {
                    @Parameter(name = "name", description = "name of an existent player to move", example = "John"),
                    @Parameter(
                            name = "rolls",
                            description = "2 dice rolls to make a custom move with.\nIf not provided, the game will generate 2 random numbers",
                            example = "1,6"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The move has been done successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "Bad request parameters. See the message for details"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The player is trying to make a move while it's not his turn"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "text/plain"),
                            description = "The game has not been started yet."
                    )
            }
    )
    public ResponseEntity<?> movePlayer(@PathVariable String name, @RequestParam Optional<String[]> rolls){

        try {
            rolls.ifPresentOrElse(
                    (r) -> controller.movePlayer(name, Arrays.stream(r).mapToInt(Integer::parseInt).toArray()),
                    () -> controller.movePlayer(name)
            );
        }
        catch (NumberFormatException ex){
            return new ResponseEntity<>("Invalid rolls parameter", HttpStatus.BAD_REQUEST);
        }
        catch (GameNotStartedException ex1){
            return new ResponseEntity<>(ex1.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch (IllegalArgumentException ex2){
            return new ResponseEntity<>(ex2.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (NotYourTurnException ex3){
            return new ResponseEntity<>(ex3.getMessage(), HttpStatus.FORBIDDEN);
        }

        String message = view.getMessage();
        view.resetMessage();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
