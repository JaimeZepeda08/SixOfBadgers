package com.cs506group12.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import com.cs506group12.backend.models.GameRecord;

/**
 * Test class for GameRecord
 * @author eknepper
 */
public class testGameRecord {

    private GameRecord gr;

    /**
     * Basic setup for game record tests
     */
    @BeforeEach
    public void setUp(){
        String[] players = new String[]{"a","b","c","d"};
        int[] score = new int[]{1,10};
        gr = new GameRecord(1, new Timestamp(1), new Timestamp(2), players, score);
    }

    /**
     * Test for team number logic in getTeamNumber
     */
    @Test
    public void testGetTeamNumber(){
        assert(gr.getTeamNumber("a") == 1);
        assert(gr.getTeamNumber("b") == 2);
        assert(gr.getTeamNumber("c") == 1);
        assert(gr.getTeamNumber("d") == 2);
        assert(gr.getTeamNumber("z") == -1);
    }

    /**
     * Tests getTeam
     */
    @Test
    public void testGetTeam(){
        String[] t1 = gr.getTeam(1);
        String[] t2 = gr.getTeam(2);

        assert(t1[0] == "a");
        assert(t1[1] == "c");
        assert(t2[0] == "b");
        assert(t2[1] == "d");
    }

    /**
     * tests getScore
     */
    @Test
    public void testGetScore(){
        assert(gr.getScore(1) == 1);
        assert(gr.getScore(2) == 10);
    }

    /**
     * tests getStartTime and getEndTime
     */
    @Test
    public void testGetTimes(){
        assert(gr.getStartTime().equals(new Timestamp(1)));
        assert(gr.getEndTime().equals(new Timestamp(2)));
    }

    /**
     * tests getUID
     */
    @Test
    public void testGetUID(){
        assert(gr.getGameUID() == 1);
    }

}
