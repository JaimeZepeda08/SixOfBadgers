package com.cs506group12.backend;
import com.cs506group12.backend.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for User
 * @author eknepper
 */
public class testUser {
    private User usr;

    @BeforeEach
    public void setUp(){
        usr = new User(1, "test", null);
    }

    /**
     * Basic test for User getter methods
     */
    @Test
    public void testGetters(){
        assert(usr.getName().equals("test"));
        assert(usr.getUserUID() == 1);
    }
}
