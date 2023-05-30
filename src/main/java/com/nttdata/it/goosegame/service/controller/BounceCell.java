package com.nttdata.it.goosegame.service.controller;

import com.nttdata.it.goosegame.service.model.GameModel;

public class BounceCell extends Cell{

    private static BounceCell instance = null;

    protected static BounceCell getInstance(){
        if (instance == null){
            instance = new BounceCell();
        }
        return instance;
    }

    @Override
    protected void act(GameController controller) {
        GameModel model = controller.getModel();
        int departurePosition = controller.getDeparturePosition();

        //set the steps to go back
        model.setSteps((model.getMaxCellNum() - departurePosition) - (model.getRolls()[0] + model.getRolls()[1]) - 1);
        controller.transit();
    }

    @Override
    public String getOutMsg(int position, String name){
        return name + " bounces!" + name + " returns to ";
    }

    @Override
    public String getInMsg(int position, String name){
        return (position-1) + ". ";
    }
}
