package com.nttdata.it.goosegame.exceptions;

public class DuplicateNameException extends RuntimeException{

    public DuplicateNameException(){
        super();
    }

    public DuplicateNameException(String message){
        super(message);
    }
}
