package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.EuchrePlayer;
import com.cs506group12.backend.models.HumanPlayerDecorator;

public class testHumanPlayerDecorator {
    

    @Test
    public void testToJSON(){
        EuchrePlayer ep = new EuchrePlayer("PlayerName", 1);
        HumanPlayerDecorator hp = new HumanPlayerDecorator(ep, null);
        String expected = "{\"header\" : \"player\", \"name\" : \"PlayerName\", \"position\" : \"1\"}";
        assertEquals(expected, hp.playerToJSON());
    }
}
