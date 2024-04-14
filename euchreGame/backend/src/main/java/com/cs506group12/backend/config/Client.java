package com.cs506group12.backend.config;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import java.io.IOException;

/**
 * This class represents a WebSocket client that can join games
 * 
 * @author jaime zepeda
 */
public class Client {

    private WebSocketSession session;
    private String playerId;
    private GameSession game;

    /**
     * Creates a new instance of Client
     * 
     * @param session WebSocket session
     */
    public Client(WebSocketSession session) {
        this.session = session;
        this.playerId = "Anonymous" + Usernames.getRandomUsername();
        this.game = null;
    }

    /**
     * Getter method for the client's session
     * 
     * @return client's WebSocket session
     */
    public WebSocketSession getSession() {
        return session;
    }

    /**
     * Getter method for the id of the player
     * 
     * @return player's id
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Getter method for this client's game
     * 
     * @return the game the client is currently in
     */
    public GameSession getGame() {
        return game;
    }

    public boolean isInGame() {
        if (game != null) {
            return true;
        }
        return false;
    }

    /**
     * Handles client leaving a game session
     * 
     * @throws IOException if an error occurs
     */
    public void leaveGame() throws IOException {
        game.removePlayer(this);
        game = null;
    }

    /**
     * Handles client joining game sessions
     * 
     * @param newGame the game to be joined
     * @throws IOException if an error occurs
     */
    public void joinGame(GameSession newGame) throws IOException {
        if (game != null) {
            // leave previous game
            leaveGame();
        }
        // join new game
        game = newGame;
    }

    /**
     * Sends a message to client through its WebSocket connection
     * 
     * @param type    type of message to be sent
     * @param content content of message
     * @throws IOException If an I/O error occurs.
     */
    @SuppressWarnings("null")
    public void sendMessage(String header, String content) throws IOException {
        Message message = new Message(header, content);
        this.getSession().sendMessage(new TextMessage(message.toString()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Client) {
            Client other = (Client) obj;
            if (playerId.equals(other.getPlayerId())) {
                return true;
            }
        }
        return false;
    }
}
