package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.cs506group12.backend.models.Client;
import com.cs506group12.backend.models.GameSession;
import com.cs506group12.backend.models.Message;

/**
 * This class test the correctness of the Client class
 * 
 * @author jaime zepeda
 */
public class testClient {

    /**
     * Since Client is an abstract class, it must be extended in order to be
     * instantiated. This class extends client and implements a basic constructor
     */
    protected class ClientImplementation extends Client {

        public ClientImplementation(WebSocketSession session) {
            super(session);
        }
    }

    WebSocketSession session;
    Client client;
    GameSession game1;
    GameSession game2;

    /**
     * Create necessary mock object to use for testing
     */
    @BeforeEach
    public void setUp() {
        // WebSocket session
        session = mock(WebSocketSession.class);

        // test client
        client = new ClientImplementation(session);

        // test games
        game1 = mock(GameSession.class);
        game2 = mock(GameSession.class);
    }

    /**
     * Tests the naming convention of the client's id
     */
    @Test
    public void testClientID() {
        // check that the client ID is a String
        assertEquals(String.class, client.getClientId().getClass());

        // check that the client ID starts with "Anonymous"
        assertEquals("Anonymous", client.getClientId().substring(0, 9));
    }

    /**
     * Checks that the client is not in a game when it is first created
     */
    @Test
    public void testInitialGame() {
        // make sure that the client is not initially in a game
        assertNull(client.getGame());
    }

    /**
     * Tests joining games
     */
    @Test
    public void testJoiningGame() {
        // join a game
        client.joinGame(game1);

        // check that the player has joined a game
        assertTrue(client.isInGame());

        // check that player joined the specified game
        assertEquals(game1, client.getGame());
        assertNotEquals(game2, client.getGame());
    }

    /**
     * Tests leaving games, and joining new games while the client is already in a
     * game
     */
    @Test
    public void testLeavingGame() {
        // join a game
        client.joinGame(game1);
        assertTrue(client.isInGame());
        assertEquals(game1, client.getGame());

        // leave game
        client.leaveGame();

        // check that the client is not in a game anymore
        assertFalse(client.isInGame());

        // rejoin the game
        client.joinGame(game1);
        assertTrue(client.isInGame());
        assertEquals(game1, client.getGame());

        // make sure that if the client joins a different game, the other game is left
        client.joinGame(game2);
        assertTrue(client.isInGame());
        assertEquals(game2, client.getGame());
    }

    /**
     * Tests sending messages to WebSocket client connections
     * 
     * @throws IOException if an error occurs
     */
    @SuppressWarnings("null")
    @Test
    void testSendMessage() throws IOException {
        // Call the method to be tested
        client.sendMessage("header", "content");

        // Verify that sendMessage method of WebSocketSession is called once
        Message message = new Message("header", "content");
        TextMessage textMessage = new TextMessage(message.toString());
        verify(session, times(1)).sendMessage(textMessage);
    }

    /**
     * Tests join game, again
     */
    @Test
    public void testJoinGame() {
        client.joinGame(game1);
        assertTrue(client.isInGame());
        assertEquals(game1, client.getGame());
    }

    /**
     * Tests leave game
     */
    @Test
    public void testLeaveGame() {
        client.joinGame(game1);
        assertTrue(client.isInGame());
        assertEquals(game1, client.getGame());

        client.leaveGame();
        assertFalse(client.isInGame());

        client.joinGame(game1);
        assertTrue(client.isInGame());
        assertEquals(game1, client.getGame());

        client.joinGame(game2);
        assertTrue(client.isInGame());
        assertEquals(game2, client.getGame());
    }


    /**
     * Tests the send client
     * 
     * @throws IOException if input not mocked
     */
    @Test
    void testSendClient() throws IOException {
        String expectedJSON = "{\"header\" : \"client\", \"id\" : \"" + client.getClientId() + "\"}";
        assertEquals(expectedJSON, client.clientToJSON());
    }


    /**
     * Tests equals, but its not actual equal
     */
    @Test
    void testEqualsWhileNot() {
        Client sameClientIdClient = new Client(session) {
            @Override
            public String clientToJSON() {
                // Mock JSON representation for testing
                return "{\"header\": \"client\",\"id\": \"" + client.getClientId() + "\"}";
            }
        };
        assertTrue(!client.equals(sameClientIdClient));
    }

    
}
