package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Chat;
import com.cs506group12.backend.models.Client;

/**
 * This class tests the correctness of the Chat class
 * 
 * @author jaime zepeda
 */
public class testChat {

    // clients that will be used to send messages
    Client user1;
    Client user2;
    Client user3;

    /**
     * Inintialize test clients using Mockito
     */
    @BeforeEach
    public void createClient() {
        // initialize user 1
        user1 = mock(Client.class);
        when(user1.getPlayerId()).thenReturn("user1");

        // initialize user 2
        user2 = mock(Client.class);
        when(user2.getPlayerId()).thenReturn("user2");

        // initialize user 3
        user3 = mock(Client.class);
        when(user3.getPlayerId()).thenReturn("user3");
    }

    /**
     * Checks that messages can be added to the chat without errors
     */
    @Test
    public void testAddMessagesToChat() {
        Chat chat = new Chat();
        chat.addMessage(user1, "hello");
        chat.addMessage(user2, "hi");
        chat.addMessage(user3, "bye");
    }

    /**
     * Checks that the returned string is in the expected format. The string must be
     * formatted as a list of messages with each one starting with the sender's id
     * followed by the message
     */
    @Test
    public void testGetChatLog() {
        Chat chat = new Chat();
        chat.addMessage(user1, "hello");
        chat.addMessage(user2, "hi");
        chat.addMessage(user3, "bye");
        chat.addMessage(user1, "bye");

        String chatLog = chat.getMessages();
        String expectedString = "[user1: hello],[user2: hi],[user3: bye],[user1: bye]";
        assertEquals(expectedString, chatLog);
    }
}
