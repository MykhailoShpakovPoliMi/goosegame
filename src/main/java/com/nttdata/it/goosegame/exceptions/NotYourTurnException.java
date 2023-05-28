package com.nttdata.it.goosegame.exceptions;

public class NotYourTurnException extends RuntimeException{

    public NotYourTurnException(){
        super();
    }

    public NotYourTurnException(String message){
        super(message);
    }
}
