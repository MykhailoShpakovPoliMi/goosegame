package com.nttdata.it.goosegame.web;

import com.nttdata.it.goosegame.service.controller.GameController;
import com.nttdata.it.goosegame.service.exceptions.DuplicateNameException;
import com.nttdata.it.goosegame.service.exceptions.GameNotStartedException;
import com.nttdata.it.goosegame.service.exceptions.NotYourTurnException;
import com.nttdata.it.goosegame.service.view.GameView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<?> addPlayer(@PathVariable String name){

        String message = "";
        try {
            controller.addPlayer(name);
        }
        catch (DuplicateNameException ex) {
            message = view.getMessage();
            view.resetMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        catch (UnsupportedOperationException ex1) {
            /*get this exception only in case the game was already started*/
            return new ResponseEntity<>(ex1.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        message = view.getMessage();
        view.resetMessage();
        //return message;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/start")
    public ResponseEntity<?> startGame(){
        try {
            controller.startGame();
            return new ResponseEntity<>("The game has been started successfully", HttpStatus.OK);
        }
        catch (UnsupportedOperationException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }


    @GetMapping("/move/{name}")
    public ResponseEntity<?> movePlayer(@PathVariable String name, @RequestParam Optional<int[]> rolls){

        try {
            rolls.ifPresentOrElse(
                    (r) -> controller.movePlayer(name, r),
                    () -> controller.movePlayer(name)
            );
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
