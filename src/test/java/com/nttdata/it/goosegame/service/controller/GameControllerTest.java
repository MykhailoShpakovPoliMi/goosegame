package com.nttdata.it.goosegame.service.controller;

import com.nttdata.it.goosegame.service.exceptions.*;
import com.nttdata.it.goosegame.service.model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
    @Mock
    private GameModel model;
    private GameController controller;

    @BeforeEach
    void setUp(){
        this.controller = new GameController(this.model);
    }

    @Test
    public void test_addPlayer() {
        //test that controller invokes appropriate methods on model
        controller.addPlayer("player");
        verify(model).isGameOn();
        verify(model).addPlayer("player");
    }

    @Test
    public void test_addPlayer_duplicateException(){
        doThrow(new DuplicateNameException()).when(model).addPlayer(anyString());
        assertThrows(DuplicateNameException.class ,() -> controller.addPlayer("player"));
    }

    @Test
    public void test_addPlayer_gameStartedException(){
        when(model.isGameOn()).thenReturn(true);
        assertThrows(GameAlreadyStartedException.class ,() -> controller.addPlayer("player2"));
    }

    @Test
    public void test_startGame_notEnoughPlException(){

        when(model.getPlayers()).thenReturn(Set.of());
        assertThrows(NotEnoughPlayersException.class, () -> controller.startGame());
        when(model.getPlayers()).thenReturn(Set.of("1pl"));
        assertThrows(NotEnoughPlayersException.class, () -> controller.startGame());
    }

    @Test
    public void test_startGame_gameStartedException(){
        when(model.getPlayers()).thenReturn(Set.of("1pl", "2pl"));
        when(model.isGameOn()).thenReturn(true);
        assertThrows(GameAlreadyStartedException.class, () -> controller.startGame());
    }

    @Test
    public void test_startGame(){
        when(model.getPlayers()).thenReturn(Set.of("1pl", "2pl"));
        when(model.isGameOn()).thenReturn(false);

        controller.startGame();

        verify(model, times(1)).getPlayers();
        verify(model, times(1)).isGameOn();
        verify(model, times(1)).setGameOn(true);
    }

    @Test
    public void test_movePlayer_gameNotStartedException(){
        when(model.isGameOn()).thenReturn(false);
        assertThrows(GameNotStartedException.class, () -> controller.movePlayer("test"));
    }

    @Test
    public void test_movePlayer_fakePlayer(){
        when(model.isGameOn()).thenReturn(true);
        when(model.getPlayers()).thenReturn(Set.of("pl1", "pl2"));
        assertThrows(IllegalArgumentException.class, () -> controller.movePlayer("test"));
    }

    @Test
    public void test_movePlayer_invalidRollsLen(){
        when(model.isGameOn()).thenReturn(true);
        when(model.getPlayers()).thenReturn(Set.of("pl1", "pl2"));
        assertThrows(IllegalArgumentException.class, () -> controller.movePlayer("pl1", new int[]{1}));
    }

    @Test
    public void test_movePlayer_notYourTurnException(){
        when(model.isGameOn()).thenReturn(true);
        when(model.getPlayers()).thenReturn(Set.of("pl1", "pl2"));
        when(model.getCurrentPlayer()).thenReturn("pl1");
        assertThrows(NotYourTurnException.class, () -> controller.movePlayer("pl2", new int[]{1,1}));
    }

    @Test
    public void test_movePlayer_invalidRollsValues(){
        when(model.isGameOn()).thenReturn(true);
        when(model.getPlayers()).thenReturn(Set.of("pl1", "pl2"));
        when(model.getCurrentPlayer()).thenReturn("pl1");
        assertThrows(IllegalArgumentException.class, () -> controller.movePlayer("pl1", new int[]{0,1}));
        assertThrows(IllegalArgumentException.class, () -> controller.movePlayer("pl1", new int[]{1,0}));
        assertThrows(IllegalArgumentException.class, () -> controller.movePlayer("pl1", new int[]{7,1}));
        assertThrows(IllegalArgumentException.class, () -> controller.movePlayer("pl1", new int[]{1,7}));
    }

    @Test
    public void test_movePlayer(){

        when(model.isGameOn()).thenReturn(true);
        when(model.getPlayers()).thenReturn(Set.of("pl1", "pl2"));
        when(model.getCurrentPlayer()).thenReturn("pl1");
        controller.movePlayer("pl1", new int[]{1,1});

        verify(model, atLeast(1)).getCurrentPosition();
        verify(model).setRolls(new int[]{1,1});
    }

    @Test
    public void test_transit(){
        when(model.getCurrentPosition()).thenReturn(0);
        controller.transit();

        verify(model, times(3)).getCurrentPosition();
        verify(model, times(1)).checkOut(StartingCell.getInstance());
        verify(model, times(1)).checkIn(StartingCell.getInstance());
    }

    @Test
    public void test_finishTurn(){
        controller.finishTurn();

        verify(model, times(1)).nextTurn();
    }

    @Test
    public void test_endGame(){
        controller.endGame();

        verify(model, times(1)).setGameOn(false);
        verify(model, times(1)).reset();
    }


}
