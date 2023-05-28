package com.nttdata.it.goosegame.controller;

public class StartingCell extends Cell{

    private static StartingCell instance = null;

    protected static StartingCell getInstance(){
        if (instance == null){
            instance = new StartingCell();
        }
        return instance;
    }

    @Override
    public String getOutMsg(int position, String name){

        return name+" moves from Start to ";
    }

}
