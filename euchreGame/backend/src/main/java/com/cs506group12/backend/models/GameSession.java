package com.cs506group12.backend.models;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class represents a euchre game session that clients can join
 * 
 * @author jaime zepeda
 */
public abstract class GameSession {

    // unique 5 charachter long id
    private String gameId;
    // players connected to this game
    private ArrayList<Client> players;
    // client that started the game sessio
    private Client host;
    // keeps track of messages sent during this game session
    private Chat chat;

    /**
     * Creates a new instance of GameSession
     * 
     * @param host client that created the game
     */
    protected GameSession(Client host) {
        this.gameId = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 5);
        this.players = new ArrayList<>();
        this.host = host;
        addPlayer(this.host);
        this.chat = new Chat();
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
     * Check if the given client is the host of this game session
     * 
     * @param client the client to be checked
     * @return true if the client is the host, false otherwise
     */
    public boolean isHost(Client client) {
        if (this.host.equals(client)) {
            return true;
        }
        return false;
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
                player.joinGame(this);
                players.add(player);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a player from this game
     * 
     * @param player the game that left the game
     */
    public void removePlayer(Client player) {
        players.remove(player);
        // notify other players in game
        sendPlayerIdsToAllClients();
    }

    /**
     * Sends a WebSocket message to all the players connected to the game
     * 
     * @param type    type of message
     * @param content content of message
     */
    private void sendMessageToAllClients(String header, String content) {
        for (Client player : players) {
            player.sendMessage(header, content);
        }
    }

    /**
     * Sends the IDs of players in the game to all clients. Used to initialize game
     */
    public void sendPlayerIdsToAllClients() {
        // send ids of connected players to all clients in the game
        sendMessageToAllClients("players", getPlayerIdsString());
    }

    /**
     * Sends the ID of the game to all clients. Used to initialize game
     */
    public void sendGameIdToAllClients() {
        sendMessageToAllClients("gameId", gameId);
    }

    /**
     * Notifies all players that a new client has joined
     * 
     * @param newClient client joining game
     */
    public void notifyPlayersNewClient(Client newClient) {
        // send game id back to client
        newClient.sendMessage("id", getGameId());
        // send ids of connected players to all clients in the game
        sendPlayerIdsToAllClients();
    }

    /**
     * Processes messages sent in the chat.
     * 
     * @param client  the player that sent the message
     * @param message the content of the message
     */
    public void processMessage(Client client, String message) {
        // add message to chat log
        chat.addMessage(client, message);
        // get messages in chat log
        String messages = chat.getMessages();
        // send messages to all clients in the game
        sendMessageToAllClients("chat", messages);
    }

    /**
     * Handles the initialization of the game
     * To be overloaded by child classes
     * 
     * @return true if able to start game, false otherwise
     */
    protected boolean startGame() {
        // check if 4 players are connected
        if (getPlayers().size() == 4) {
            System.out.println("Started game: " + getGameId()); // debug
            // alert players that the game has started
            sendMessageToAllClients("started", "Game " + getGameId() + " has started");
            return true;
        } else {
            host.sendMessage("error", "Not enough players to start the game");
            return false;
        }
    }
}
