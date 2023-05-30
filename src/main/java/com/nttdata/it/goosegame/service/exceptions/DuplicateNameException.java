package com.nttdata.it.goosegame.service.exceptions;

public class DuplicateNameException extends RuntimeException{

    public DuplicateNameException(){
        super();
    }

    public DuplicateNameException(String message){
        super(message);
    }
}
