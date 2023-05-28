package com.nttdata.it.goosegame.exceptions;

public class GameNotStartedException extends RuntimeException{

    public GameNotStartedException(){
        super();
    }

    public GameNotStartedException(String message){
        super(message);
    }
}
