package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.config.Message;

/**
 * This class tests the correctness of the Message class
 */
public class testMessage {

    /*
     * Check that the returned message is in the correct JSON format
     */
    @Test
    public void testMessageSetters() {
        Message testMessage = new Message("test", "This is a test message");

        String expeString = "{\"header\" : \"test\", \"content\" : \"This is a test message\"}";
        assertEquals(testMessage.toString(), expeString);
    }
}
