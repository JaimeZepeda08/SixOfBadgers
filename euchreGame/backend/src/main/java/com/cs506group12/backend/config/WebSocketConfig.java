package com.cs506group12.backend.config;

import java.io.IOException;
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

                Object payload = message.getPayload();

                // Assuming the message is in JSON format
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(payload.toString());

                // manages different types of messages from the client
                // message has to be in the format: {"header" : header, "content" : content}
                if (jsonNode.has("header")) {
                    String messageHeader = jsonNode.get("header").asText();

                    switch (messageHeader) {
                        case "create":
                            handleCreateMessage(session);
                            break;
                        case "join":
                            handleJoinMessage(session, jsonNode);
                            break;
                        case "start":
                            startGame(games.get(jsonNode.get("gameID").asText()));
                        default:
                            break;
                    }
                }
            }

            @SuppressWarnings("null")
            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                System.out.println("Error in WebSocket session with session id: " + session.getId()); // debug
            }

            @SuppressWarnings("null")
            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("Connection closed with session id: " + session.getId()); // debug

                // Remove session from the sessions map
                sessions.remove(session);

                // TODO close games started by this client
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
    }

    /**
     * Handles the creation of a new game session.
     *
     * @param session The WebSocketSession sending the message.
     * @throws IOException If an I/O error occurs.
     */
    private void handleCreateMessage(WebSocketSession session) throws IOException {
        // get client from connected sessions
        Client client = sessions.get(session);
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
     * @param session  The WebSocketSession sending the message.
     * @param jsonNode The JsonNode containing the message data.
     * @throws IOException If an I/O error occurs.
     */
    private void handleJoinMessage(WebSocketSession session, JsonNode jsonNode) throws IOException {
        // get id from JSON
        String id = jsonNode.get("gameID").asText();
        // get client object of session sending the message
        Client client = sessions.get(session);

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
                    client.sendMessage("error", "Game " + id + " is already full");
                }
            } else {
                client.sendMessage("error", "You are already in game " + id);
            }
        } else {
            client.sendMessage("error", "Game " + id + " does not exist");
        }
    }

    /**
     * Handles starting a game session.
     *
     * @param game The GameSession to start.
     * @throws IOException If an I/O error occurs.
     */
    private void startGame(GameSession game) throws IOException {
        System.out.println("Started game: " + game.getGameId()); // debug

        // TODO start game

        // let players know that the game has started
        game.sendMessageToAllClients("started", "Game " + game.getGameId() + " has started");
    }
}