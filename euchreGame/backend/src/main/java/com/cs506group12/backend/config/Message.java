package com.cs506group12.backend.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a message object with a type and content.
 * 
 * @author jaime zepeda
 */
@SuppressWarnings("unused")
public class Message {
    private String type; // The type of the message
    private String content; // The content of the message

    /**
     * Constructs a new Message object with the given type and content.
     *
     * @param type    The type of the message.
     * @param content The content of the message.
     */
    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    /**
     * Sets the type of the message.
     *
     * @param type The type of the message.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the content of the message.
     *
     * @param content The content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Generates a JSON string representation of the message.
     *
     * @return A JSON string representing the message.
     */
    @Override
    public String toString() {
        return "{\"type\" : " + "\"" + type + "\"" + ", \"content\" : " + "\"" + content + "\"" + "}";
    }
}
