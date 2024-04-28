package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs506group12.backend.interfaces.*;
import com.cs506group12.backend.models.*;
import com.cs506group12.backend.models.GameState.PHASE;

public class testGameState {

    @Mock private Player p1;
    @Mock private Player p2;
    @Mock private Player p3;
    @Mock private Player p4;
    @Mock private Card c;

    

    @Test
    public void testActivePlayer(){
        GameState state = new GameState();
        state.addPlayer(p1, 1);
        state.addPlayer(p2, 2);
        state.addPlayer(p3, 3);
        state.addPlayer(p4, 4);

        state.setActivePlayer(1);
        assertEquals(p1, state.getActivePlayer());

        state.setActivePlayer(2);
        assertEquals(p2, state.getActivePlayer());

        state.setActivePlayer(3);
        assertEquals(p3, state.getActivePlayer());

        state.setActivePlayer(4);
        assertEquals(p4, state.getActivePlayer());
    }

    @Test
    public void testScoring(){
        GameState state = new GameState();
        state.addScore(1, 2);
        state.addScore(2, 4);

        assertEquals(2, state.getTeamScore(1));
        assertEquals(4, state.getTeamScore(2));

        state.addTrick(1);
        state.addTrick(2);
        state.addTrick(2);
        state.addTrick(4);

        assertEquals(1, state.getTeamTricks(1));
        assertEquals(3, state.getTeamTricks(2));
    }

    @Test
    public void testPlayCards(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        state.addPlayer(p1, 1);
        state.setActivePlayer(1);
        state.addPlayedCard(c);
        state.addPlayedCard(c);
        assertEquals(2,state.getPlayedCards().size());

        state.clearPlayedCards();
        assertEquals(0, state.getPlayedCards().size());
    }

    @Test
    public void testDealCards(){
        GameState state = new GameState();
        state.dealCards();

        assertEquals(5, state.getHand(1).getSize());
        assertNotNull(state.getFaceUpCard());
        assertEquals(PHASE.PICKTRUMP1, state.getCurrentPhase());
    }

    @Test
    public void testSetStartRound(){
        GameState state = new GameState();
        state.setPlayerGoingAlone(1);

        state.setPhase(PHASE.STARTROUND);

        assertEquals(0, state.getTeamTricks(1));
        assertEquals(0, state.getTeamTricks(2));
        assertEquals(-1, state.getAttackingTeam());
        assertEquals(-1, state.getPlayerGoingAlone());
        assertEquals(0, state.getHand(1).getSize());
        assertEquals(0, state.getHand(2).getSize());
        assertNull(state.getFaceUpCard());
        assertEquals(-1, state.getLeadingPlayerPosition());
        assertNull(state.getLeadingSuit());
        assertNull(state.getTrump());
        assertEquals(0, state.getPlayedCards().size());

    }
}
