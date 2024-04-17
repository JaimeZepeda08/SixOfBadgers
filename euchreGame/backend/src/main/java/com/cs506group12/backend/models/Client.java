package com.cs506group12.backend.models;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import java.io.IOException;

/**
 * This class represents a WebSocket client that can join games
 * 
 * @author jaime zepeda
 */
public abstract class Client {

    private WebSocketSession session;
    private String clientId;
    private GameSession game;

    /**
     * Creates a new instance of Client
     * 
     * @param session WebSocket session
     */
    protected Client(WebSocketSession session) {
        this.session = session;
        this.clientId = "Anonymous" + Usernames.getRandomUsername();
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
    public String getClientId() {
        return clientId;
    }

    /**
     * Getter method for this client's game
     * 
     * @return the game the client is currently in
     */
    public GameSession getGame() {
        return game;
    }

    /**
     * Checks if this client is in a game
     * 
     * @return true if the client is in a game, false otherwise
     */
    public boolean isInGame() {
        if (game != null) {
            return true;
        }
        return false;
    }

    /**
     * Handles client joining game sessions
     * 
     * @param newGame the game to be joined
     */
    public void joinGame(GameSession newGame) {
        if (game != null) {
            // leave previous game
            leaveGame();
        }
        // join new game
        game = newGame;
    }

    /**
     * Handles client leaving a game session
     */
    public void leaveGame() {
        if (game != null) {
            game.removeClient(this);
        }
        game = null;
    }

    /**
     * Sends a message to client through its WebSocket connection
     * 
     * @param type    type of message to be sent
     * @param content content of message
     */
    @SuppressWarnings("null")
    public void sendMessage(String header, String content) {
        Message message = new Message(header, content);
        try {
            this.getSession().sendMessage(new TextMessage(message.toString()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends an error socket message to the client
     * 
     * @param error the message to be sent
     */
    public void reportError(String error) {
        sendMessage("error", error);
    }

    /**
     * Sends this client's id
     */
    public void sendPlayerID() {
        sendMessage("username", getClientId());
    }

    /**
     * Sends the IDs of the players in this client's game
     */
    public void sendPlayersInGame() {
        sendMessage("players", getGame().getClientIdsString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Client) {
            Client other = (Client) obj;
            if (clientId.equals(other.getClientId())) {
                return true;
            }
        }
        return false;
    }
}
