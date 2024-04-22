package com.cs506group12.backend.models;

/**
 * Represents a message object with a header and content.
 * 
 * @author jaime zepeda
 */
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
     * Generates a JSON string representation of the message.
     *
     * @return A JSON string representing the message.
     */
    @Override
    public String toString() {
        return "{\"header\" : " + "\"" + header + "\"" + ", \"content\" : " + "\"" + content + "\"" + "}";
    }
}
