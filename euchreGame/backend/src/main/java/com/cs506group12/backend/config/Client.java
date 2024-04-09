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

    /**
     * Creates a new instance of Client
     * 
     * @param session WebSocket session
     */
    public Client(WebSocketSession session) {
        this.session = session;
        this.playerId = "Anonymous" + Usernames.getRandomUsername();
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
     * Sends a message to client through its WebSocket connection
     * 
     * @param type    type of message to be sent
     * @param content content of message
     * @throws IOException If an I/O error occurs.
     */
    @SuppressWarnings("null")
    public void sendMessage(String type, String content) throws IOException {
        Message message = new Message(type, content);
        this.getSession().sendMessage(new TextMessage(message.toString()));
    }
}
