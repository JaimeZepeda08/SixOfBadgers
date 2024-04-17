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
    // clients connected to this session
    private ArrayList<Client> clients;
    // client that started the game session
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
        this.clients = new ArrayList<>();
        this.host = host;
        addClient(this.host);
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
     * Getter method for connected clients
     * 
     * @return an ArrayList object containing all the players in this game
     */
    public ArrayList<Client> getConnectedClients() {
        return clients;
    }

    /**
     * Gets a string representation of the players in the game
     * 
     * @return string representation of players in this game
     */
    public String getClientIdsString() {
        String ids = "[";
        for (Client client : clients) {
            ids += client.getClientId() + ",";
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
     * Checks if client is already in game
     * 
     * @param client Client object to be checked
     * @return true if player is in game, false otherwise
     */
    public boolean hasClient(Client client) {
        if (clients.contains(client)) {
            return true;
        }
        return false;
    }

    /**
     * Adds player to game if conditions are met
     * 1. Games cannot have more than 4 clients
     * 2. Games cannot have repeated clients
     * 
     * @param client Client to join game
     * @return true if successful, false otherwise
     */
    public boolean addClient(Client client) {
        if (clients.size() < 4) {
            if (!clients.contains(client)) {
                client.joinGame(this);
                clients.add(client);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a client from this game session
     * 
     * @param client the client that left the game
     */
    public void removeClient(Client client) {
        clients.remove(client);
        // notify other players in game
        sendSessionToAllClients();
    }

    /**
     * Checks if all the clients in the session are ready to start
     * 
     * @return true if all clients are ready, false otherwise
     */
    public boolean areClientsReady() {
        for (Client client : clients) {
            if (!client.isReady()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles the initialization of the game
     * To be overloaded by child classes
     * 
     * @return true if able to start game, false otherwise
     */
    protected boolean startGame() {
        // check if 4 clients are connected
        if (getConnectedClients().size() == 4) {
            System.out.println("Started game: " + getGameId()); // debug
            // alert clients that the game has started
            sendMessageToAllClients("started", "Game " + getGameId() + " has started");
            return true;
        } else {
            host.sendMessage("error", "Not enough players to start the game");
            return false;
        }
    }

    /**
     * Sends a WebSocket message to all the players connected to the game.
     * Used for simple messages and event alerts.
     * 
     * @param type    type of message
     * @param content content of message
     */
    protected void sendMessageToAllClients(String header, String content) {
        for (Client client : clients) {
            client.sendMessage(header, content);
        }
    }

    /**
     * Sends a WebSocket message to all the players connected to the game.
     * Used to send larger amounts of data.
     * 
     * @param formattedJSON a formatted JSON string
     */
    protected void sendMessageToAllClients(String formattedJSON) {
        for (Client client : clients) {
            client.sendMessage(formattedJSON);
        }
    }

    /**
     * Send a JSON representation of this game session to all the clients
     */
    public void sendSessionToAllClients() {
        sendMessageToAllClients(sessionToJSON());
    }

    /**
     * Processes messages sent in the chat.
     * 
     * @param client  the client that sent the message
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
     * Converts this game session into a JSON string
     * 
     * @return JSON representation of this game session
     */
    public String sessionToJSON() {
        return "{"
                + "\"header\" : \"session\", "
                + "\"gameId\" : " + "\"" + gameId + "\", "
                + "\"players\" : " + "\"" + getClientIdsString() + "\""
                + "}";
    }
}
