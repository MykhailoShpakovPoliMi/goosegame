package com.nttdata.it.goosegame.service.controller;

import com.nttdata.it.goosegame.service.exceptions.*;
import com.nttdata.it.goosegame.service.model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**This class is the controller of the game, it manages the game logic*/
@Controller
public class GameController {

    private final Cell[] board;
    private final GameModel model;
    private int departurePosition;

    /**Initializes the board with cell types*/
    GameController(@Autowired GameModel model) {
        this.model = model;
        board = new Cell[model.getMaxCellNum() + 1 + 1];

        for (int i = 0; i <= model.getMaxCellNum(); i++) {
            Cell cell;

            if (i == 0)
                cell = StartingCell.getInstance();
            else if (i == 6)
                cell = BridgeCell.getInstance();
            else if (i == 5 || i == 9 || i == 14 || i == 18 || i == 23 || i == 27)
                cell = GooseCell.getInstance();
            else if (i == model.getMaxCellNum())
                cell = WinningCell.getInstance();
            else
                cell = Cell.getInstance();

            board[i] = cell;
        }

        board[model.getMaxCellNum() + 1] = BounceCell.getInstance();
    }

    protected int getDeparturePosition() {
        return departurePosition;
    }

    /** Calls {@link GameModel#addPlayer(String)}
     * @throws DuplicateNameException if the name supplied is already present in the list of players*/
    public void addPlayer(String name) throws DuplicateNameException {
        if (model.isGameOn())
            throw new GameAlreadyStartedException("Game already started, cannot add players");
        else
            model.addPlayer(name);
    }

    /**Starts the game by calling {@link GameModel#setGameOn(boolean)}
     * @throws UnsupportedOperationException if there are less than 2 players or the game has been already started*/
    public void startGame() {
        if (model.getPlayers().size() < 2)
            throw new NotEnoughPlayersException("Not enough players to start the game");
        else if (model.isGameOn())
            throw new GameAlreadyStartedException("Game already started");
        else
            model.setGameOn(true);
    }

    /**Ends the game by calling {@link GameModel#setGameOn(boolean)}*/
    protected void endGame() {
        model.setGameOn(false);
        model.reset();
    }

    /**Generates random rolls and calls {@link #movePlayer(String, int[])}*/
    public void movePlayer(String player){
        int[] rolls = new int[2];
        rolls[0] = (int) (Math.random() * 6) + 1;
        rolls[1] = (int) (Math.random() * 6) + 1;
        movePlayer(player, rolls);
    }

    /**
     * @param player the player to move
     * @param rolls the rolls to move the player
     * Moves the player to the next cell
     * <ul>
     *     <li>checks if the game is started</li>
     *     <li>checks if the player is in the list of players</li>
     *     <li>checks if it's the player's turn</li>
     *     <li>checks if the rolls are positive</li>
     * </ul>
     * Updates {@link #departurePosition} and calls {@link GameModel#setRolls(int[])}
     * Calls {@link #transit()}
     * @throws GameNotStartedException if the game is not started yet
     * @throws IllegalArgumentException if the player is not in the list of players or if the rolls are not positive and less or equal than 6
     * @throws NotYourTurnException if it's not the player's turn*/
    public void movePlayer(String player, int[] rolls) {

        if (!model.isGameOn())
            throw new GameNotStartedException("game has not been started yet");
        else if (!model.getPlayers().contains(player))
            throw new IllegalArgumentException(player + " is not a player");
        else if (rolls.length != 2)
            throw new IllegalArgumentException("Invalid number of rolls");
        else if (!model.getCurrentPlayer().equals(player))
            throw new NotYourTurnException(player + ", it's not your turn");
        else if (rolls[0] < 1 || rolls[1] < 1 || rolls[0] > 6 || rolls[1] > 6)
            throw new IllegalArgumentException("Rolls must be positive and less than 6");

        departurePosition = model.getCurrentPosition();
        model.setRolls(rolls);

        transit();
    }

    /**do set of operation in order to displace the current player to the next cell
     * <ul>
     *     <li>check out from the current cell by calling {@link GameModel#checkOut(Cell)}</li>
     *     <li>check in to the next cell by calling {@link GameModel#checkIn(Cell)}</li>
     *     <li>act on the next cell by calling {@link Cell#act(GameController)}</li>
     * </ul>*/
    protected void transit(){
        model.checkOut(board[model.getCurrentPosition()]);
        //model updates its position inside checkOut
        model.checkIn(board[model.getCurrentPosition()]);
        board[model.getCurrentPosition()].act(this);
    }

    protected GameModel getModel() {
        return model;
    }

    /**Calls {@link GameModel#nextTurn()}*/
    protected void finishTurn() {
        model.nextTurn();
    }
}
