package com.cs506group12.backend.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // Map to keep track of WebSocket sessions
    private final Map<WebSocketSession, Client> sessions = new ConcurrentHashMap<>();
    private final Map<String, GameSession> games = new ConcurrentHashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                System.out.println("Connection established with session id: " + session.getId()); // debug

                // add client to hashmap
                sessions.put(session, new Client(session));
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                System.out.println("Received message: " + message.getPayload()); // debug

                Object payload = message.getPayload();

                // Assuming the message is in JSON format
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(payload.toString());

                if (jsonNode.has("type")) {
                    String messageType = jsonNode.get("type").asText();

                    switch (messageType) {
                        case "create":
                            handleCreateMessage(session);
                            break;
                        case "join":
                            handleJoinMessage(session, jsonNode);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                System.out.println("Error in WebSocket session with session id: " + session.getId()); // debug
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("Connection closed with session id: " + session.getId()); // debug

                // Remove session from the sessions map
                sessions.remove(session);

                // close games started by this client
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
    }

    @SuppressWarnings("null")
    protected void handleCreateMessage(WebSocketSession session) throws IOException {
        Client client = sessions.get(session);
        GameSession game = new GameSession(client);
        games.put(game.getGameId(), game);
        client.getSession().sendMessage(new TextMessage(game.getGameId()));
    }

    private void handleJoinMessage(WebSocketSession session, JsonNode jsonNode) {
        String id = jsonNode.get("gameID").asText();
        if (games.containsKey(id)) {
            GameSession game = games.get(id);
            if (!game.addPlayer(sessions.get(session))) {
            } else {
                // report error
            }
        } else {
            // report error
        }
    }

    // Method to send session IDs to all clients
    private void sendSessionIdsToClients() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String sessionIdsJson = mapper.writeValueAsString(sessions.values());
        for (WebSocketSession session : sessions.keySet()) {
            session.sendMessage(new TextMessage(sessionIdsJson));
        }
    }
}