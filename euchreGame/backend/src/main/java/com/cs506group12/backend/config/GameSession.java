package com.cs506group12.backend.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * This class represents a euchre game session that clients can join
 * 
 * @author jaime zepeda
 */
public class GameSession {

    // unique 5 charachter long id
    private String gameId;
    // players connected to this game
    private ArrayList<Client> players;

    /**
     * Creates a new instance of GameSession
     * 
     * @param host client that created the game
     */
    public GameSession(Client host) {
        this.gameId = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 5);
        this.players = new ArrayList<>();
        this.players.add(host);
    }

    /**
     * Getter method for this game's id
     * 
     * @return this game's id
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Getter method for connected players
     * 
     * @return an ArrayList object containing all the players in this game
     */
    public ArrayList<Client> getPlayers() {
        return players;
    }

    /**
     * Checks if player is already in game
     * 
     * @param player Client object to be checked
     * @return true if player is in game, false otherwise
     */
    public boolean hasPlayer(Client player) {
        if (players.contains(player)) {
            return true;
        }
        return false;
    }

    /**
     * Gets a string representation of the players in the game
     * 
     * @return string representation of players in this game
     */
    public String getPlayerIdsString() {
        String ids = "[";
        for (Client player : players) {
            ids += player.getPlayerId() + ",";
        }
        ids = ids.substring(0, ids.length() - 1) + "]";
        return ids;
    }

    /**
     * Adds player to game if conditions are met
     * 1. Games cannot have more than 4 players
     * 2. Games cannot have repeated players
     * 
     * @param player Client to join game
     * @return true if successful, false otherwise
     */
    public boolean addPlayer(Client player) {
        if (players.size() < 4) {
            if (!players.contains(player)) {
                players.add(player);
                return true;
            }
        }
        return false;
    }

    /**
     * Sends a WebSocket message to all the players connected to the game
     * 
     * @param type    type of message
     * @param content content of message
     * @throws IOException If an I/O error occurs.
     */
    public void sendMessageToAllClients(String header, String content) throws IOException {
        for (Client player : players) {
            player.sendMessage(header, content);
        }
    }

    /**
     * Notifies all players that a new client has joined
     * 
     * @param newClient client joining game
     * @throws IOException If an I/O error occurs.
     */
    public void notifyPlayersNewClient(Client newClient) throws IOException {
        // send game id back to client
        newClient.sendMessage("id", getGameId());
        // send ids of connected players to all clients in the game
        sendMessageToAllClients("players", getPlayerIdsString());
    }
}
