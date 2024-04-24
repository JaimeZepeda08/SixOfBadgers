package com.cs506group12.backend;
import com.cs506group12.backend.models.User;

import org.junit.jupiter.api.Test;

/**
 * Test class for User
 * @author eknepper
 */
public class testUser {

    /**
     * Basic test for User getter methods
     */
    @Test
    public void testGetters(){
        User usr = new User(1, "test");
        assert(usr.getName().equals("test"));
        assert(usr.getUserUID() == 1);
    }
}
