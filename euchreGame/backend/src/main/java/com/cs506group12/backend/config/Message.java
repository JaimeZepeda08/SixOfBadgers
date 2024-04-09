package com.cs506group12.backend.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a message object with a header and content.
 * 
 * @author jaime zepeda
 */
@SuppressWarnings("unused")
public class Message {

    private String header; // The header of the message
    private String content; // The content of the message

    /**
     * Constructs a new Message object with the given header and content.
     *
     * @param header  The header of the message.
     * @param content The content of the message.
     */
    public Message(String header, String content) {
        this.header = header;
        this.content = content;
    }

    /**
     * Sets the header of the message.
     *
     * @param header The header of the message.
     */
    public void setheader(String header) {
        this.header = header;
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
        return "{\"header\" : " + "\"" + header + "\"" + ", \"content\" : " + "\"" + content + "\"" + "}";
    }
}
