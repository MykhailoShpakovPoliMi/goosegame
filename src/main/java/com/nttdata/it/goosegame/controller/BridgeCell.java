package com.nttdata.it.goosegame.controller;

import com.nttdata.it.goosegame.model.GameModel;

public class BridgeCell extends Cell{

    private static BridgeCell instance = null;

    protected static BridgeCell getInstance(){
        if (instance == null){
            instance = new BridgeCell();
        }
        return instance;
    }

    @Override
    protected void act(GameController controller) {
        GameModel model = controller.getModel();

        //set the steps to go forward
        model.setSteps(6);
        controller.transit();
    }

    @Override
    public String getOutMsg(int position, String name){
        return name+" jumps to";
    }

    @Override
    public String getInMsg(int position, String name){
        return " The Bridge";
    }
}
