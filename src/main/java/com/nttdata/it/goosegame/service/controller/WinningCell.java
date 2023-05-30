package com.nttdata.it.goosegame.service.controller;

public class WinningCell extends Cell{

    private static WinningCell instance = null;

    protected static WinningCell getInstance(){
        if (instance == null){
            instance = new WinningCell();
        }
        return instance;
    }

    @Override
    protected void act(GameController controller){
        controller.endGame();
    }

    @Override
    public String getInMsg(int position, String name){
        return "" + position+". " + name + " wins!!!";
    }
}
