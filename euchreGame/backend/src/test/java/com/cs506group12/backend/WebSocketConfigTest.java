package com.cs506group12.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.cs506group12.backend.config.WebSocketConfig;
import com.cs506group12.backend.models.Client;
import com.cs506group12.backend.models.EuchreGame;
import com.cs506group12.backend.models.GameSession;

public class WebSocketConfigTest {

    private WebSocketHandlerRegistry registry;
    private WebSocketSession session;
    private WebSocketConfig webSocketConfig;

    @BeforeEach
    void setUp() {
        registry = mock(WebSocketHandlerRegistry.class);
        session = mock(WebSocketSession.class);
        webSocketConfig = new WebSocketConfig();
    }

    @Test
    void testAfterConnectionEstablished() {
        Client client = mock(Client.class);
        when(client.getSession()).thenReturn(session);
        try {
            webSocketConfig.myWebSocketHandler().afterConnectionEstablished(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Verify that the client is added to sessions map
    }

    @Test
    void testHandleCreateMessage() {
        Client client = mock(Client.class);
        GameSession gameSession = mock(EuchreGame.class);
        webSocketConfig.handleCreateMessage(client, "test");
        // Verify that appropriate handling of create message is done
    }

    @Test
    void testHandleJoinMessage() {
        Client client = mock(Client.class);
        GameSession gameSession = mock(EuchreGame.class);
        webSocketConfig.handleJoinMessage(client, "testId", "test");
        // Verify that appropriate handling of join message is done
    }

    @Test
    void testHandleLeaveMessage() {
        Client client = mock(Client.class);
        GameSession gameSession = mock(EuchreGame.class);
        webSocketConfig.handleLeaveMessage(client);
        // Verify that appropriate handling of leave message is done
    }

    @Test
    void testHandleStartGame() {
        Client client = mock(Client.class);
        GameSession gameSession = mock(EuchreGame.class);
        webSocketConfig.handleStartGame(gameSession, client);
        // Verify that appropriate handling of start game message is done
    }

    @Test
    void testHandleGameSetUp() {
        Client client = mock(Client.class);
        GameSession gameSession = mock(EuchreGame.class);
        webSocketConfig.handleGameSetUp(gameSession, client);
        // Verify that appropriate handling of game set up message is done
    }

    @Test
    void testHandleChatMessages() {
        Client client = mock(Client.class);
        GameSession gameSession = mock(EuchreGame.class);
        webSocketConfig.handleChatMessages(gameSession, client, "test message");
        // Verify that appropriate handling of chat messages is done
    }

    
}
