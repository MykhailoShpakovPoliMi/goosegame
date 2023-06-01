package com.nttdata.it.goosegame.web;

import com.nttdata.it.goosegame.service.controller.GameController;
import com.nttdata.it.goosegame.service.exceptions.*;
import com.nttdata.it.goosegame.service.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = RequestHandler.class)
public class RequestHandlerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private GameController controller;
    @MockBean
    private GameView view;

    private RequestHandler restHandler;

    @BeforeEach
    void setUp(){
        this.restHandler = new RequestHandler(this.controller, this.view);
    }

    @Test
    public void test_addPlayer_duplicateNameException() throws Exception{
        doThrow(new DuplicateNameException()).when(controller).addPlayer(anyString());
        when(view.getMessage()).thenReturn("testDuplicateNameException");

        this.mvc.perform(get("/goose/add/mike"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("testDuplicateNameException"));

        verify(view, times(1)).getMessage();
        verify(view, times(1)).resetMessage();
    }

    @Test
    public void test_addPlayer_gameStartedException() throws Exception {
        doThrow(new GameAlreadyStartedException("testGameAlreadyStartedException"))
                .when(controller).addPlayer(anyString());

        this.mvc.perform(get("/goose/add/mike"))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(content().string("testGameAlreadyStartedException"));

    }

    @Test
    public void test_addPlayer() throws Exception {
        when(view.getMessage()).thenReturn("player added successfully");

        this.mvc.perform(get("/goose/add/mike"))
                .andExpect(status().isOk())
                .andExpect(content().string("player added successfully"));

        verify(view, times(1)).getMessage();
        verify(view, times(1)).resetMessage();
    }

    @Test
    public void test_startGame_gameStartedException() throws Exception {
        doThrow(new GameAlreadyStartedException("testGameAlreadyStartedException"))
                .when(controller).startGame();

        this.mvc.perform(get("/goose/start"))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(content().string("testGameAlreadyStartedException"));
    }

    @Test
    public void test_startGame_noPlayersException() throws Exception {
        doThrow(new NotEnoughPlayersException("testNotEnoughPlayersException"))
                .when(controller).startGame();

        this.mvc.perform(get("/goose/start"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(content().string("testNotEnoughPlayersException"));
    }

    @Test
    public void test_startGame() throws Exception {
        this.mvc.perform(get("/goose/start"))
                .andExpect(status().isOk())
                .andExpect(content().string("The game has been started successfully"));

        verify(controller, times(1)).startGame();
    }

    @Test
    public void test_movePlayer_numberFormatException() throws Exception{
        this.mvc.perform(get("/goose/move/mike?rolls=abc"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("Invalid rolls parameter"));

    }

    @Test
    public void test_movePlayer_gameNotStartedException() throws Exception {
        doThrow(new GameNotStartedException("testGameNotStartedException1"))
                .when(controller).movePlayer(anyString(), any());
        doThrow(new GameNotStartedException("testGameNotStartedException2"))
                .when(controller).movePlayer(anyString());

        this.mvc.perform(get("/goose/move/mike?rolls=1,1"))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(content().string("testGameNotStartedException1"));

        this.mvc.perform(get("/goose/move/mike"))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(content().string("testGameNotStartedException2"));
    }

    @Test
    public void test_movePlayer_illegalArgException() throws Exception {
        doThrow(new IllegalArgumentException("testIllegalArgumentException1"))
                .when(controller).movePlayer(anyString(), any());
        doThrow(new IllegalArgumentException("testIllegalArgumentException2"))
                .when(controller).movePlayer(anyString());

        this.mvc.perform(get("/goose/move/mike?rolls=1,1"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("testIllegalArgumentException1"));

        this.mvc.perform(get("/goose/move/mike"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("testIllegalArgumentException2"));
    }

    @Test
    public void test_movePlayer_notYourTurnException() throws Exception {
        doThrow(new NotYourTurnException("testNotYourTurnException1"))
                .when(controller).movePlayer(anyString(), any());
        doThrow(new NotYourTurnException("testNotYourTurnException2"))
                .when(controller).movePlayer(anyString());

        this.mvc.perform(get("/goose/move/mike?rolls=1,1"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(content().string("testNotYourTurnException1"));

        this.mvc.perform(get("/goose/move/mike"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(content().string("testNotYourTurnException2"));
    }

    @Test
    public void test_movePlayer() throws Exception {
        when(view.getMessage()).thenReturn("test1 movePlayer");

        this.mvc.perform(get("/goose/move/mike?rolls=1,1"))
                .andExpect(status().isOk())
                .andExpect(content().string("test1 movePlayer"));

        verify(controller).movePlayer("mike", new int[]{1, 1});
        verify(view, times(1)).getMessage();
        verify(view, times(1)).resetMessage();

        reset(controller, view);
        when(view.getMessage()).thenReturn("test2 movePlayer");

        this.mvc.perform(get("/goose/move/mike"))
                .andExpect(status().isOk())
                .andExpect(content().string("test2 movePlayer"));

        verify(controller).movePlayer("mike");
        verify(view, times(1)).getMessage();
        verify(view, times(1)).resetMessage();
    }
}
