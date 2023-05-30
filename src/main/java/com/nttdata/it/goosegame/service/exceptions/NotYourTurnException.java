package com.nttdata.it.goosegame.service.exceptions;

public class NotYourTurnException extends RuntimeException{

    public NotYourTurnException(){
        super();
    }

    public NotYourTurnException(String message){
        super(message);
    }
}
