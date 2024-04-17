package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

import com.cs506group12.backend.config.Client;
import com.cs506group12.backend.config.GameSession;

/**
 * This class tests the correctness of the GameSession class
 * 
 * @author jaime zepeda
 */
public class testGameSession {

    /**
     * This class extends the GameSession class so that it can be tested.
     */
    protected class GameSessionImplementation extends GameSession {
        public GameSessionImplementation(Client host) {
            super(host);
        }

        public boolean startGame() {
            if (super.startGame()) {
                return true;
            }
            return false;
        }
    }

    WebSocketSession session;
    Client client1;
    Client client2;
    Client client3;
    Client client4;
    Client client5;
    GameSessionImplementation game;

    /**
     * Create necessary mock object to use for testing
     */
    @BeforeEach
    public void setUp() {
        // WebSocket session
        session = mock(WebSocketSession.class);

        // test client
        client1 = mock(Client.class);
        when(client1.getPlayerId()).thenReturn("player1");
        client2 = mock(Client.class);
        when(client2.getPlayerId()).thenReturn("player2");
        client3 = mock(Client.class);
        when(client3.getPlayerId()).thenReturn("player3");
        client4 = mock(Client.class);
        when(client4.getPlayerId()).thenReturn("player4");
        client5 = mock(Client.class);
        when(client5.getPlayerId()).thenReturn("player5");

        // test game
        game = new GameSessionImplementation(client1);
    }

    /**
     * Test that the game ID matches the expected format
     */
    @Test
    public void testGameID() {
        // check that the game ID only contains lowercase letters and numbers, and is 5
        // characters long
        assertTrue(Pattern.matches("[a-zA-Z0-9]{5}", game.getGameId()));
    }

    /**
     * Check that players can be added correctly
     */
    @Test
    public void testClientsJoining() {
        {
            // at first only the host should be in the game
            String players = game.getPlayerIdsString();
            String expected = "[" + client1.getPlayerId() + "]";
            assertEquals(expected, players);
        }

        {
            // more players join the game session
            assertTrue(game.addPlayer(client2));
            assertTrue(game.addPlayer(client3));
            assertTrue(game.addPlayer(client4));

            // check that the players have joined
            assertTrue(game.hasPlayer(client1));
            assertTrue(game.hasPlayer(client2));
            assertTrue(game.hasPlayer(client3));
            assertTrue(game.hasPlayer(client4));

            // check to see if players where added correclty
            String players = game.getPlayerIdsString();
            String expected = "[" + client1.getPlayerId() + "," + client2.getPlayerId() + "," + client3.getPlayerId()
                    + "," + client4.getPlayerId() + "]";
            assertEquals(expected, players);

            // check that only up to 4 players can join
            assertFalse(game.addPlayer(client5));
        }
    }

    /**
     * Check that players can leave the game
     */
    @Test
    public void testClientsLeaving() {
        // more players join the game session
        assertTrue(game.addPlayer(client2));
        assertTrue(game.addPlayer(client3));
        assertTrue(game.addPlayer(client4));

        // check that the players have joined
        assertTrue(game.hasPlayer(client1));
        assertTrue(game.hasPlayer(client2));
        assertTrue(game.hasPlayer(client3));
        assertTrue(game.hasPlayer(client4));

        // test players leaving
        game.removePlayer(client2);
        game.removePlayer(client4);
        assertFalse(game.hasPlayer(client2));
        assertFalse(game.hasPlayer(client4));
        assertTrue(game.hasPlayer(client1));
        assertTrue(game.hasPlayer(client3));

        // if a player leaves, a new player should be able to join
        assertTrue(game.addPlayer(client5));
    }

    /**
     * Test starting a game
     */
    @Test
    public void testStartGame() {
        // more players join the game session
        assertTrue(game.addPlayer(client2));

        // check that the players have joined
        assertTrue(game.hasPlayer(client1));
        assertTrue(game.hasPlayer(client2));

        // game should not be able to start if there are less than 4 players in the game
        assertFalse(game.startGame());

        // add the rest of the players
        assertTrue(game.addPlayer(client3));
        assertTrue(game.addPlayer(client4));
        // check that the players have joined
        assertTrue(game.hasPlayer(client3));
        assertTrue(game.hasPlayer(client4));

        // start game
        assertTrue(game.startGame());
    }
}
