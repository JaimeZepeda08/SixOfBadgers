package com.cs506group12.backend.config;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameSession {

    private String gameId;
    private Client host;
    private Set<Client> players;

    public GameSession(Client host) {
        this.gameId = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 5);
        this.host = host;
        this.players = new HashSet<>();
        this.players.add(this.host);
    }

    public String getGameId() {
        return gameId;
    }

    public Client getHost() {
        return host;
    }

    public Set<Client> getPlayers() {
        return players;
    }

    public boolean addPlayer(Client player) {
        if (players.size() < 4) {
            players.add(player);
            return true;
        }
        return false;
    }
}
