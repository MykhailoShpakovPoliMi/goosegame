package com.nttdata.it.goosegame.service.exceptions;

public class NotEnoughPlayersException extends RuntimeException {
    public NotEnoughPlayersException(){
        super();
    }

    public NotEnoughPlayersException(String message){
        super(message);
    }
}
