package com.cs506group12.backend.config;

import java.util.ArrayList;
import java.util.UUID;

public class GameSession {

    private String gameId;
    private ArrayList<Client> players;

    public GameSession(Client host) {
        this.gameId = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 5);
        this.players = new ArrayList<>();
        this.players.add(host);
    }

    public String getGameId() {
        return gameId;
    }

    public ArrayList<Client> getPlayers() {
        return players;
    }

    public String getPlayerIdsString() {
        String ids = "[";
        for (Client player : players) {
            ids += player.getPlayerId() + ",";
        }
        ids = ids.substring(0, ids.length() - 1) + "]";
        return ids;
    }

    public void addPlayer(Client player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }
}
