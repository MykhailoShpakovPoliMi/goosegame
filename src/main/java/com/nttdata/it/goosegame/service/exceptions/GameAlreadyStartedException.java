package com.nttdata.it.goosegame.service.exceptions;

public class GameAlreadyStartedException extends RuntimeException{

    public GameAlreadyStartedException(){

    }

    public GameAlreadyStartedException(String message){
        super(message);
    }
}
