package com.nttdata.it.goosegame.service.controller;

import com.nttdata.it.goosegame.service.model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.transform.sax.SAXResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CellTest {
    private Cell cell;
    @Mock
    private TestController controller;
    @Mock
    private GameModel model;

    @Test
    public void test_emptyCell_act(){
        this.cell = new Cell();

        cell.act(controller);

        verify(controller, times(1)).finishTurn();
    }

    @Test
    public void test_emptyCell_getIn(){
        this.cell = new Cell();
        assertEquals(cell.getInMsg(1, "mike"), Integer.toString(1));
    }

    @Test
    public void test_emptyCell_getOut(){
        this.cell = new Cell();
        assertEquals(cell.getOutMsg(2, "mike"), "mike moves from " + Integer.toString(2) + " to ");
    }

    @Test
    public void test_startCell_act(){
        this.cell = new StartingCell();

        cell.act(controller);

        verify(controller, times(1)).finishTurn();
    }

    @Test
    public void test_startCell_getOut(){
        this.cell = new StartingCell();
        assertEquals(cell.getOutMsg(2, "mike"), "mike moves from Start to ");
    }

    @Test
    public void test_bridgeCell_act(){
        this.cell = new BridgeCell();

        when(controller.getModel()).thenReturn(model);

        cell.act(controller);

        verify(controller, times(1)).getModel();
        verify(model, times(1)).setSteps(6);
        verify(controller, times(1)).transit();
    }

    @Test
    public void test_bridgeCell_getIn(){
        this.cell = new BridgeCell();
        assertEquals(cell.getInMsg(1, "mike"), " The Bridge");
    }

    @Test
    public void test_bridgeCell_getOut(){
        this.cell = new BridgeCell();
        assertEquals(cell.getOutMsg(2, "mike"), "mike jumps to");
    }

    @Test
    public void test_gooseCell_act(){
        this.cell = new GooseCell();

        cell.act(controller);

        verify(controller, times(1)).transit();
    }

    @Test
    public void test_gooseCell_getIn(){
        this.cell = new GooseCell();
        assertEquals(cell.getInMsg(2, "mike"), Integer.toString(2) + ", The Goose. ");
    }

    @Test
    public void test_gooseCell_getOut(){
        this.cell = new GooseCell();
        assertEquals(cell.getOutMsg(2, "mike"), "mike moves again and goes to ");
    }

    @Test
    public void test_bounceCell_act(){
        this.cell = new BounceCell();

        when(controller.getModel()).thenReturn(model);
        when(controller.getDeparturePosition()).thenReturn(62);
        when(model.getMaxCellNum()).thenReturn(63);
        when(model.getRolls()).thenReturn(new int[]{1,1});

        cell.act(controller);

        verify(controller, times(1)).getModel();
        verify(controller, times(1)).getDeparturePosition();
        verify(model, times(1)).setSteps((63-62) - (1+1) - 1);
        verify(controller, times(1)).transit();
    }

    @Test
    public void test_bounceCell_getIn(){
        this.cell = new BounceCell();
        assertEquals(cell.getInMsg(64, "mike"), Integer.toString(64- 1) + ". ");
    }

    @Test
    public void test_bounceCell_getOut(){
        this.cell = new BounceCell();
        assertEquals(cell.getOutMsg(2, "mike"), "mike bounces!mike returns to ");
    }

    @Test
    public void test_winCell_act(){
        this.cell = new WinningCell();

        cell.act(controller);

        verify(controller, times(1)).endGame();
    }

    @Test
    public void test_winCell_getIn(){
        this.cell = new WinningCell();
        assertEquals(cell.getInMsg(63, "mike"), Integer.toString(63) + ". mike wins!!!");
    }
}
