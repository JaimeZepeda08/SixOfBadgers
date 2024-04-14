package com.cs506group12.backend.config;

import java.io.IOException;
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

    /**
     * Creates a new instance of GameSession
     * 
     * @param host client that created the game
     * @throws IOException
     */
    public GameSession(Client host) throws IOException {
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
    private ArrayList<Client> getPlayers() {
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
     * @throws IOException
     */
    public boolean addPlayer(Client player) throws IOException {
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
     * @throws IOException if an error occurs
     */
    public void removePlayer(Client player) throws IOException {
        players.remove(player);
        // notify other players in game
        sendPlayerIdsToClients();
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
     * Sends the IDs of players in the game to all clients. Used to initialize game
     * 
     * @throws IOException if an error occurs
     */
    public void sendPlayerIdsToClients() throws IOException {
        // send ids of connected players to all clients in the game
        sendMessageToAllClients("players", getPlayerIdsString());
    }

    /**
     * Sends the ID of the game to all clients. Used to initialize game
     * 
     * @throws IOException
     */
    public void sendGameIdToClients() throws IOException {
        sendMessageToAllClients("gameId", gameId);
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
        sendPlayerIdsToClients();
    }

    /**
     * Processes messages sent in the chat.
     * 
     * @param client  the player that sent the message
     * @param message the content of the message
     * @throws IOException if an error occurs
     */
    public void processMessage(Client client, String message) throws IOException {
        // add message to chat log
        chat.addMessage(client, message);
        // get n number of messages to display
        String messages = chat.getNMessages(10);
        // send messages to clients
        sendMessageToAllClients("chat", messages);
    }

    /**
     * Handles the initialization of the game
     * 
     * @throws IOException if an error occurs
     */
    public void startGame() throws IOException {
        if (getPlayers().size() == 4) {
            System.out.println("Started game: " + getGameId()); // debug

            // let players know that the game has started
            sendMessageToAllClients("started", "Game " + getGameId() + " has started");
        } else {
            host.sendMessage("error", "Not enough players to start the game");
        }
    }
}
