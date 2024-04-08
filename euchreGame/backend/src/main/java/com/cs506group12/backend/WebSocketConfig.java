package com.cs506group12.backend;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // Map to keep track of WebSocket sessions
    private final Map<WebSocketSession, String> sessions = new ConcurrentHashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                System.out.println("Connection established with session id: " + session.getId());
                // update users
                sendSessionIdsToClients();
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                System.out.println("Received message: " + message.getPayload());
                // Add session to the sessions map
                if (!sessions.keySet().contains(session)) {
                    sessions.put(session, message.getPayload().toString());
                    // update users
                    sendSessionIdsToClients();
                }
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                System.out.println("Error in WebSocket session with session id: " + session.getId());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("Connection closed with session id: " + session.getId());
                // Remove session from the sessions map
                sessions.remove(session.getId());
                // update users
                sendSessionIdsToClients();
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }

            // Method to send session IDs to all clients
            private void sendSessionIdsToClients() throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                String sessionIdsJson = mapper.writeValueAsString(sessions.values());
                for (WebSocketSession session : sessions.keySet()) {
                    session.sendMessage(new TextMessage(sessionIdsJson));
                }
            }
        };
    }
}