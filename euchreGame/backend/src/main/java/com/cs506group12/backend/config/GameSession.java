package com.cs506group12.backend.config;

import java.util.ArrayList;
import java.util.UUID;

import com.cs506group12.backend.models.EuchreGame;

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
    // client that started the game sessio
    private Client host;
    // keeps track of messages sent during this game session
    private Chat chat;
    // holds the euchre game object
    private EuchreGame euchreGame;

    /**
     * Creates a new instance of GameSession
     * 
     * @param host client that created the game
     */
    public GameSession(Client host) {
        this.gameId = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 5);
        this.players = new ArrayList<>();
        this.host = host;
        addPlayer(this.host);
        this.chat = new Chat();
        this.euchreGame = new EuchreGame(this);
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
    private String getPlayerIdsString() {
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
        sendPlayerIdsToClients();
    }

    /**
     * Sends a WebSocket message to all the players connected to the game
     * 
     * @param type    type of message
     * @param content content of message
     */
    public void sendMessageToAllClients(String header, String content) {
        for (Client player : players) {
            player.sendMessage(header, content);
        }
    }

    /**
     * Sends the IDs of players in the game to all clients. Used to initialize game
     */
    public void sendPlayerIdsToClients() {
        // send ids of connected players to all clients in the game
        sendMessageToAllClients("players", getPlayerIdsString());
    }

    /**
     * Sends the ID of the game to all clients. Used to initialize game
     */
    public void sendGameIdToClients() {
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
        sendPlayerIdsToClients();
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
        // get n number of messages to display
        String messages = chat.getNMessages(10);
        // send messages to clients
        sendMessageToAllClients("chat", messages);
    }

    /**
     * Handles the initialization of the game
     */
    public void startGame() {
        if (getPlayers().size() == 4) {
            System.out.println("Started game: " + getGameId()); // debug
            sendMessageToAllClients("started", "Game " + getGameId() + " has started");

            // initialize euchre game
            // euchreGame.euchreGameLoop();
        } else {
            host.sendMessage("error", "Not enough players to start the game");
        }
    }
}
