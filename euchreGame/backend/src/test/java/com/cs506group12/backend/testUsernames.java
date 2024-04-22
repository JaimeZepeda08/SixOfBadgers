package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Usernames;

/**
 * This class tests the correctness of the Usernames class.
 * 
 * @author jaime zepeda
 */
public class testUsernames {

    /*
     * Check that the return type of the username generator is a String
     */
    @Test
    public void testUsernameIsAString() {
        String username = Usernames.getRandomUsername();
        assertEquals(username.getClass(), String.class);
    }

    /**
     * Checks the naming convention of the username. A username can only consist of
     * letters and the first letter must be capitilized
     */
    @Test
    public void testUsernameNaming() {
        String username = Usernames.getRandomUsername();
        // username only contains letters (no numbers or symbols are allowed)
        assertTrue(Pattern.matches("[a-zA-Z]+", username));
        // username starts with a capital letter
        assertTrue(Pattern.matches("[A-Z]+", username.substring(0, 1)));
    }
}
