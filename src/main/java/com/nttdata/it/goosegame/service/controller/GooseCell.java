package com.nttdata.it.goosegame.service.controller;

public class GooseCell extends Cell{
    private static GooseCell instance = null;

    protected static GooseCell getInstance(){
        if (instance == null){
            instance = new GooseCell();
        }
        return instance;
    }

    @Override
    protected void act(GameController controller){
        controller.transit();
    }

    @Override
    public String getOutMsg(int position, String name){
        return name+" moves again and goes to ";
    }

    @Override
    public String getInMsg(int position, String name){
        return position+", The Goose. ";
    }

}
