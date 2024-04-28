package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cs506group12.backend.interfaces.Player;
import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Client;
import com.cs506group12.backend.models.EuchreGame;
import com.cs506group12.backend.models.GameState;

public class EuchreGameTest {

    private EuchreGame game;
    private GameState stateMock;
    private Card.SUIT trumpSuitMock;
    private Player activePlayerMock;

    @BeforeEach
    public void setUp() {
        stateMock = mock(GameState.class);
        trumpSuitMock = mock(Card.SUIT.class);
        activePlayerMock = mock(Player.class);
        game = new EuchreGame(mock(Client.class), stateMock);
    }

    @Test
    public void testStartGame() {
        // Arrange
        when(game.startGame()).thenReturn(true);

        // Act
        boolean result = game.startGame();

        // Assert
        assertTrue(result);
        verify(stateMock, times(1)).setUUID(any());
        // Add more verification as needed
    }

    @Test
    public void testStartRound() {
        // Arrange
        when(stateMock.getPlayerGoingAlone()).thenReturn(-1);
        when(stateMock.getDealerPosition()).thenReturn(-1);

        // Act
        game.startRound();

        // Assert
        verify(stateMock, times(1)).clearHand(anyInt());
        verify(stateMock, times(1)).setDealerPosition(anyInt());
    }

    @Test
    public void testPickTrump_Phase1_PlayerChoosesSuit() {
        // Arrange
        when(stateMock.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        when(activePlayerMock.chooseTrump(stateMock)).thenReturn(Card.SUIT.HEARTS);

        // Act
        boolean result = game.pickTrump(null);

        // Assert
        assertTrue(result);
        verify(stateMock, times(1)).setTrump(Card.SUIT.HEARTS);
    }

    @Test
    public void testPickTrump_Phase1_PlayerPasses() {
        // Arrange
        when(stateMock.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        when(activePlayerMock.chooseTrump(stateMock)).thenReturn(Card.SUIT.NONE);
        when(stateMock.getActivePlayer()).thenReturn(activePlayerMock);

        // Act
        boolean result = game.pickTrump(null);

        // Assert
        assertFalse(result);
        verify(stateMock, times(1)).setActivePlayer(anyInt());
    }

    @Test
    public void testPickTrump_Phase1_AllPlayersPass() {
        // Arrange
        when(stateMock.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        when(activePlayerMock.chooseTrump(stateMock)).thenReturn(Card.SUIT.NONE);
        when(stateMock.getActivePlayer()).thenReturn(activePlayerMock);

        // Act
        boolean result = game.pickTrump(null);
        assertFalse(result); // First player passes

        result = game.pickTrump(null);
        assertFalse(result); // Second player passes

        result = game.pickTrump(null);
        assertFalse(result); // Third player passes

        // Now all players passed, should proceed to phase 2
        result = game.pickTrump(null);

        // Assert
        assertNull(result);
        verify(stateMock, times(1)).setPhase(GameState.PHASE.PICKTRUMP2);
    }

    // Similarly, write tests for other methods and scenarios

}
