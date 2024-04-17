package com.cs506group12.backend.models;

import java.util.ArrayList;

/**
 * Object representation of a message chat log
 * 
 * @author jaime zepeda
 */
public class Chat {
    /**
     * Object representation of a single message in a chat
     */
    private class ChatMessage {
        Client sender;
        String message;

        /**
         * Constructor for a single message
         * 
         * @param sender  client that sent the message
         * @param message content of the message
         */
        private ChatMessage(Client sender, String message) {
            this.sender = sender;
            this.message = message;
        }

        /**
         * Getter method for the content of the message
         * 
         * @return the content of the message
         */
        private String getMessage() {
            return message;
        }

        /**
         * Getter method for the sender of the message
         * 
         * @return the client that sent the message
         */
        private Client getSender() {
            return sender;
        }
    }

    // keeps track of messages in the chat
    private ArrayList<ChatMessage> chatMessages;

    /**
     * Constructor for a chat in a game
     */
    public Chat() {
        chatMessages = new ArrayList<>();
    }

    /**
     * Adds a message sent by a player to the chat log
     * 
     * @param sender  the client that sent the message
     * @param message content of the message
     */
    public void addMessage(Client sender, String message) {
        chatMessages.add(new ChatMessage(sender, message));
    }

    /**
     * Returns the messages in the chat log in a formated string including who sent
     * each.
     * 
     * @return a string representation of the chat log
     */
    public String getMessages() {
        String messageString = "";
        for (int i = 0; i < chatMessages.size(); i++) {
            ChatMessage message = chatMessages.get(i);
            messageString += "[" + message.getSender().getPlayerId() + ": " + message.getMessage() + "],";
        }
        return messageString.substring(0, messageString.length() - 1);
    }
}
