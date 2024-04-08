package com.cs506group12.backend.config;

import org.springframework.web.socket.WebSocketSession;

public class Client {
    private WebSocketSession session;
    private String ip;
    private GameSession game;

    @SuppressWarnings("null")
    public Client(WebSocketSession session) {
        this.session = session;
        this.ip = session.getRemoteAddress().getAddress().getHostAddress();
        this.game = null;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getIp() {
        return ip;
    }

    public GameSession getGame() {
        return game;
    }

    public void setGame(GameSession game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Client) {
            Client other = (Client) obj;
            if (this.getIp().equals(other.getIp())) {
                return true;
            }
        }
        return false;
    }
}
