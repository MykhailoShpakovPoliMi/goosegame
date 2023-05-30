package com.nttdata.it.goosegame.service.view;

import org.springframework.stereotype.Service;

@Service
public class GameView {
    private String message = "";

    public void appendToMessage(String toAppend){
        this.message += toAppend;
    }

    public void resetMessage(){
        this.message = "";
    }

    public String getMessage(){
        return this.message;
    }
}
