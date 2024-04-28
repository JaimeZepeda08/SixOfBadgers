package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.EuchrePlayer;

public class testEuchrePlayer {
    @Test
    public void testPlayer(){
        EuchrePlayer player = new EuchrePlayer("testName",1);
        assertEquals("testName", player.getName());
        assertEquals(1, player.getPosition());
        assertNull(player.chooseCard(null));
        assertNull(player.chooseReplacement(null));
        assertNull(player.chooseTrump(null));
    }
}
