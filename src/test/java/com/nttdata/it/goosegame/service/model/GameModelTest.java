package com.nttdata.it.goosegame.service.model;


import com.nttdata.it.goosegame.service.controller.Cell;
import com.nttdata.it.goosegame.service.controller.GameController;
import com.nttdata.it.goosegame.service.exceptions.DuplicateNameException;
import com.nttdata.it.goosegame.service.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameModelTest {

    @Mock
    private GameView view;
    @Mock
    private Cell cell;
    private GameModel model;

    @BeforeEach
    void setUp(){
        this.model = new GameModel(this.view);
    }

    @Test
    public void test_addPlayer(){
        model.addPlayer("pl1");

        assertTrue(model.getPlayers().contains("pl1") && model.getPlayers().size() == 1 );
        verify(view, times(1)).appendToMessage("players: " + model.getPlayers());

        assertThrows(DuplicateNameException.class, () -> model.addPlayer("pl1"));
        verify(view, times(1)).appendToMessage("pl1: already existing player");
    }

    @Test
    public void test_getCurrentPosition(){
        model.addPlayer("pl1");

        int prevPosition = model.getCurrentPosition();
        assertEquals(prevPosition, 0);

        model.setSteps(1);
        model.checkOut(cell);

        assertEquals(model.getCurrentPosition(), prevPosition + 1);
    }



    @Test
    public void test_setAndGetRolls(){
        model.addPlayer("pl1");
        model.setRolls(new int[]{1,1});

        verify(view, times(1)).appendToMessage(model.getCurrentPlayer() + " rolls: " + 1 + ", " + 1 + ". ");

        assertTrue(model.getRolls()[0] == 1 && model.getRolls()[1] == 1);
    }

    @Test
    public void test_checkInCheckOut(){
        model.addPlayer("pl1");

        when(cell.getInMsg(anyInt(), anyString())).thenReturn("you are in");
        when(cell.getOutMsg(anyInt(), anyString())).thenReturn("you are out");
        model.checkIn(cell);

        verify(cell, times(1)).getInMsg(anyInt(), anyString());
        verify(view, times(1)).appendToMessage("you are in");

        model.checkOut(cell);

        verify(cell, times(1)).getInMsg(anyInt(), anyString());
        verify(view, times(1)).appendToMessage("you are out");
    }

    @Test
    public void test_updatePosition(){
        int prevPos;

        model.addPlayer("pl1");
        prevPos = model.getCurrentPosition();

        model.setSteps(1);
        model.checkOut(new Cell());

        assertEquals(model.getCurrentPosition(), prevPos + 1);

        model.setSteps(model.getMaxCellNum() - model.getCurrentPosition() + 10);
        model.checkOut(new Cell());

        assertEquals(model.getCurrentPosition(), model.getMaxCellNum() + 1);
    }

    @Test
    public void test_reset(){
        model.addPlayer("pl1");
        model.setRolls(new int[]{1,6});
        model.checkOut(new Cell());
        model.nextTurn();

        model.reset();

        assertEquals(model.getPlayers().size(), 0);
        assertTrue(model.getRolls()[0] == 0 && model.getRolls()[1] == 0);
    }

}
