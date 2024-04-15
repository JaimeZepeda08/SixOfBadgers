package com.cs506group12.backend.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class for WebSocket communication between frontent and backend
 * 
 * @author jaime zepeda
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // Concurrent HashMap to store active WebSocket sessions clients
    private final Map<WebSocketSession, Client> sessions = new ConcurrentHashMap<>();
    // Concurrent HashMap to store active game sessions
    private final Map<String, GameSession> games = new ConcurrentHashMap<>();

    /**
     * Registers WebSocket handlers.
     *
     * @param registry The WebSocketHandlerRegistry to register handlers.
     */
    @SuppressWarnings("null")
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }

    /**
     * Creates a WebSocketHandler bean.
     *
     * @return WebSocketHandler bean.
     */
    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketHandler() {
            @SuppressWarnings("null")
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                System.out.println("Connection established with session id: " + session.getId()); // debug

                // add client to hashmap
                sessions.put(session, new Client(session));
            }

            @SuppressWarnings("null")
            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                System.out.println("Received message: " + message.getPayload()); // debug

                // get client sending the message
                Client client = sessions.get(session);
                GameSession game = client.getGame();

                // Assuming the message is in JSON format
                ObjectMapper mapper = new ObjectMapper();
                Object payload = message.getPayload();
                JsonNode jsonNode = mapper.readTree(payload.toString());

                // manages different types of messages from clients
                if (jsonNode.has("header")) {
                    String messageHeader = jsonNode.get("header").asText();

                    switch (messageHeader) {
                        /************** Multiplayer Game Lobby Messages **************/

                        // called when a player creates a new game
                        case "create":
                            handleCreateMessage(client);
                            break;
                        // called when a players joins a game
                        case "join":
                            handleJoinMessage(client, jsonNode.get("gameID").asText());
                            break;
                        // called when a player leaves a game
                        case "leave":
                            handleLeaveMessage(client);
                            break;
                        // called when a player attempts to start a game
                        case "start":
                            handleStartGame(game, client);
                            break;

                        /************** In Game Messages **************/

                        // called by each client at the start of a game
                        case "setup":
                            handleGameSetUp(client);
                            break;
                        // called whenever a players sends a message in the chat
                        case "message":
                            handleChatMessages(game, client, jsonNode.get("message").asText());
                            break;
                        default:
                            break;
                    }
                }
            }

            @SuppressWarnings("null")
            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                System.out.println("Error in WebSocket session with session id: " + session.getId()); // debug

                // some error happened in a client session
                handleErrorInSession(session);
            }

            @SuppressWarnings("null")
            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("Connection closed with session id: " + session.getId()); // debug

                // a client closes the browser
                handleErrorInSession(session);
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
    }

    /*
     * The following functions correspond the the handling of messages or events in
     * the multiplayer game lobby. Most of the data will be sent to all clients in a
     * game session
     */

    /**
     * This function handles clients closing sessions. It will remove them from
     * their current game and alert other players
     * 
     * @param session the client session that was lost
     */
    private void handleErrorInSession(WebSocketSession session) {
        // Remove session from the sessions map
        Client client = sessions.get(session);
        sessions.remove(session);

        // remove from current game
        GameSession game = client.getGame();
        game.removePlayer(client);

        // alert players
        game.sendPlayerIdsToAllClients();
    }

    /**
     * Handles the creation of a new game session.
     *
     * @param client The client creating the game.
     */
    private void handleCreateMessage(Client client) {
        // create a new game session with the client as the host
        GameSession game = new GameSession(client);
        // store game session
        games.put(game.getGameId(), game);
        // send message to all players in game that a new client has joined
        game.notifyPlayersNewClient(client);
    }

    /**
     * Handles new clients joining an existing game session.
     *
     * @param client client that is trying to join a game
     */
    private void handleJoinMessage(Client client, String id) {
        // check that the game with the id sent by the client exists
        if (games.containsKey(id)) {
            // get game with corresponding id
            GameSession game = games.get(id);
            // check that the client is not already in this game
            if (!game.hasPlayer(client)) {
                // add player to game
                if (game.addPlayer(client)) {
                    // send message to all players in game that a new client has joined
                    game.notifyPlayersNewClient(client);
                } else {
                    client.reportError("Game " + id + " is already full");
                }
            } else {
                client.reportError("You are already in game " + id);
            }
        } else {
            client.reportError("Game " + id + " does not exist");
        }
    }

    /**
     * Handles players leaving a game session
     * 
     * @param client the client leaving the game
     */
    private void handleLeaveMessage(Client client) {
        if (client.isInGame()) {
            // remove from current game
            GameSession game = client.getGame();
            game.removePlayer(client);

            // alert players
            game.sendPlayerIdsToAllClients();

            // alert client that they can leave the session
            client.sendMessage("leave", "You can now leave the game");
        } else {
            client.reportError("You are currently not in a game");
        }
    }

    /**
     * Handles starting a game session.
     *
     * @param game   The GameSession to start.
     * @param client the client that started the game
     */
    private void handleStartGame(GameSession game, Client client) {
        if (game != null) {
            if (game.isHost(client)) {
                game.startGame();
            } else {
                client.reportError("Only the host can start the game");
            }
        } else {
            client.reportError("Please create or join a game first");
        }
    }

    /*
     * The following functions will handle in-game events such as the set up of
     * client side variables, and other game loop related events.
     */

    /**
     * Handles client-side set up of the game. This will be done individually by
     * each client at the very start of the game
     * 
     * @param client the client requesting the data
     */
    private void handleGameSetUp(Client client) {
        client.sendPlayerID();
        client.sendPlayersInGame();
    }

    /**
     * Handles the processing of a in-game chat message
     * 
     * @param game    the game session that the message was sent in
     * @param sender  the client that sent the message
     * @param message the string representation of the message
     */
    private void handleChatMessages(GameSession game, Client sender, String message) {
        game.processMessage(sender, message);
    }
}