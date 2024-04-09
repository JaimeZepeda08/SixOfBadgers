package com.cs506group12.backend.config;

import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

public class Client {
    private WebSocketSession session;
    private String playerId;

    public Client(WebSocketSession session) {
        this.session = session;
        this.playerId = "Player_" + UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 3);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Client) {
            Client other = (Client) obj;
            if (this.getPlayerId().equals(other.getPlayerId())) {
                return true;
            }
        }
        return false;
    }
}
