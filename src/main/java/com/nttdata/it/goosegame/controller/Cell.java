package com.nttdata.it.goosegame.controller;

/**
 * Singleton cell class*/
public class  Cell {

    private static Cell instance = null;

    protected static Cell getInstance(){
        if (instance == null){
            instance = new Cell();
        }
        return instance;
    }

    /**
     * @param controller the {@link GameController} instance that cell uses to execute certain actions
     * Makes an appropriate action based on the cell type ( Subclass of {@link Cell} )
     * <ul>
     *     <li>{@link Cell}, {@link StartingCell} - calls {@link GameController#finishTurn()}</li>
     *     <li>{@link BridgeCell}, {@link BounceCell} set the necessary number of steps by calling {@link com.nttdata.it.goosegame.model.GameModel#setSteps(int)}
     *     and then call {@link GameController#transit()}</li>
     *     <li>{@link GooseCell} just calls {@link GameController#transit()} since steps must correspond to the last player`s rolls</li>
     *     <li>{@link WinningCell} calls {@link GameController#endGame()}</li>
     * </ul>
     * The only ways a player can stop moving are only by arriving at {@link Cell} or {@link WinningCell}
     * */
    protected void act(GameController controller){
        controller.finishTurn();
    }

    /**@return message of the check in when player ends up on this cell*/
    public String getInMsg(int position, String name){
        return ""+position;
    }

    /**@return message of the check in when player leaves this cell*/
    public String getOutMsg(int position, String name){

        return name+" moves from "+position +" to ";
    }
}
