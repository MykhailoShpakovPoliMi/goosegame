package com.nttdata.it.goosegame.service.model;

import com.nttdata.it.goosegame.service.controller.Cell;
import com.nttdata.it.goosegame.service.exceptions.DuplicateNameException;
import com.nttdata.it.goosegame.service.view.GameView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class GameModel {

    private final int MAX_CELL_NUM = 63;
    private final Map<String, Integer> playersPositions = new HashMap<String, Integer>();
    private int currentTurn = 0;
    private int[] playerRolls = new int[2];
    private int steps = 0;
    private boolean gameOn = false;
    private final GameView view;

    public GameModel(@Autowired GameView view) {
        this.view = view;
    }

    public int getMaxCellNum() {
        return MAX_CELL_NUM;
    }

    /**@return the current position of the current player*/
    public int getCurrentPosition(){
        String curPlayer = (String) playersPositions
                .keySet()
                .toArray()[(currentTurn)%(playersPositions.keySet().size())];

        return playersPositions.get(curPlayer);
    }

    /**@return the current player`s name*/
    public String getCurrentPlayer(){
        return (String) playersPositions
                .keySet()
                .toArray()[(currentTurn)%(playersPositions.keySet().size())];
    }

    public Set<String> getPlayers(){
        return playersPositions.keySet();
    }

    /**Adds a player to the game and puts it on the starting cell. Appends a message appropriate message to the view
     * in case exception was throw or not.
     * @param name the name of the player to add to the game
     * @throws DuplicateNameException if the name is a duplicate. Appends a message to the view
     * */
    public void addPlayer(String name){

        if (playersPositions.keySet().contains(name)) {
            view.appendToMessage(name + ": already existing player");
            throw new DuplicateNameException();
        }else {
            playersPositions.put(name, 0);
            view.appendToMessage("players: " + playersPositions.keySet());
        }
    }

    /**@param rolls roll 1 and roll 2 thrown by the current player
     * <ul>
     * <li>sets the rolls of the current player</li>
     * <li>appends a message to the view</li>
     * <li>calls setSteps() to set the steps of the current player. steps will be used in updatePosition() method</li>
     * </ul>
     * */
    public void setRolls(int[] rolls){
        playerRolls[0] = rolls[0];
        playerRolls[1] = rolls[1];
        view.appendToMessage(getCurrentPlayer() + " rolls: " + playerRolls[0] + ", " + playerRolls[1] + ". ");

        setSteps(playerRolls[0] + playerRolls[1]);
    }

    public int[] getRolls(){
        int[] retRolls = new int[2];
        retRolls[0] = playerRolls[0];
        retRolls[1] = playerRolls[1];
        return retRolls;
    }

    /**sets the number of steps that user needs to make for displacement*/
    public void setSteps(int steps){
        this.steps = steps;
    }

    /**@param cell the cell that user needs to make a checkout.
     * Appends the check-out message, returned by the cell, to the view.
     * updates current player`s position by calling updatePosition()*/
    public void checkOut(Cell cell){
        view.appendToMessage(cell.getOutMsg(getCurrentPosition(), getCurrentPlayer()));
        updatePosition();
    }

    /**Increases current player`s position by the sum of 2 rolls or by the number of steps that has been set.
     * If the player exceeds all the cells, it ends up on the bounce cell which is MAX_CELL_NUM + 1*/
    private void updatePosition(){
        if (getCurrentPosition() + steps > getMaxCellNum())
            playersPositions.put(getCurrentPlayer(), getMaxCellNum() + 1);
        else
            playersPositions.put(getCurrentPlayer(), getCurrentPosition() + steps);
    }

    /**@param cell the cell that user needs to make a checkin
     * Appends the check in message ,returned from by the cell, to the view*/
    public void checkIn(Cell cell){
        view.appendToMessage(cell.getInMsg(getCurrentPosition(), getCurrentPlayer()));
    }

    /**Increments currentTurn*/
    public void nextTurn(){
        currentTurn++;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public void reset(){
        playersPositions.clear();
        currentTurn = 0;
        playerRolls[0] = 0;
        playerRolls[1] = 0;
        steps = 0;
    }

    public boolean isGameOn() {
        return gameOn;
    }

}
