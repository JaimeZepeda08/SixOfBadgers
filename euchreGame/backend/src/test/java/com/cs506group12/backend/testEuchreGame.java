package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.cs506group12.backend.interfaces.*;
import com.cs506group12.backend.models.*;
import com.cs506group12.backend.models.Card.SUIT;
import com.cs506group12.backend.models.GameState.PHASE;

public class testEuchreGame {

    @Mock GameState state;
    @Mock Client host;
    @Mock Player p1;
    @Mock Player p2;
    @Mock Player p3;
    @Mock Player p4;
    @Mock Hand hand;
    @Mock ArrayList<Card> playedCards;

    @Test
    public void testStartGame(){
        MockitoAnnotations.openMocks(this);
        EuchreGame game = new EuchreGame(host);

        assertTrue(game.startGame());
    }

    @Test
    public void testPickTrumpPhase1(){
        MockitoAnnotations.openMocks(this);
        when(state.getActivePlayer()).thenReturn(p1).thenReturn(p2);
        when(state.getCurrentPhase()).thenReturn(PHASE.PICKTRUMP1);
        when(state.getDealerPosition()).thenReturn(2);
        when(state.getPlayer(1)).thenReturn(p1);
        when(state.getPlayer(2)).thenReturn(p2);
        when(p2.chooseTrump(any())).thenReturn(SUIT.SPADES);
        when(p2.chooseReplacement(any())).thenReturn(new Card(SUIT.DIAMONDS,9));
        when(state.getHand(anyInt())).thenReturn(hand);

        EuchreGame game = new EuchreGame(host, state);

        assertTrue(game.pickTrump(Card.SUIT.NONE));
    }

    @Test
    public void testPickTrumpPhase2(){
        MockitoAnnotations.openMocks(this);
        when(state.getActivePlayer()).thenReturn(p1).thenReturn(p2);
        when(state.getCurrentPhase()).thenReturn(PHASE.PICKTRUMP2);
        when(state.getDealerPosition()).thenReturn(2);
        when(state.getPlayer(1)).thenReturn(p1);
        when(state.getPlayer(2)).thenReturn(p2);
        when(p2.chooseTrump(any())).thenReturn(SUIT.SPADES);
        when(p2.chooseReplacement(any())).thenReturn(new Card(SUIT.DIAMONDS,9));
        when(state.getHand(anyInt())).thenReturn(hand);

        EuchreGame game = new EuchreGame(host, state);

        assertTrue(game.pickTrump(Card.SUIT.NONE));
    }

    @Test
    public void testPlayTrick(){
        MockitoAnnotations.openMocks(this);

        when(state.getActivePlayer()).thenReturn(p1).thenReturn(p2).thenReturn(p3).thenReturn(p4);
        when(state.getDealerPosition()).thenReturn(2);
        when(state.getHand(anyInt())).thenReturn(hand);
        when(state.getPlayedCards()).thenReturn(playedCards);
        when(playedCards.size()).thenReturn(1).thenReturn(1)
            .thenReturn(2).thenReturn(2)
            .thenReturn(3).thenReturn(3)
            .thenReturn(4).thenReturn(4);
        when(state.getPlayerGoingAlone()).thenReturn(-1);
        when(p1.chooseCard(any())).thenReturn(new Card(SUIT.SPADES,12));
        when(p2.chooseCard(any())).thenReturn(new Card(SUIT.HEARTS,11));
        when(p3.chooseCard(any())).thenReturn(new Card(SUIT.DIAMONDS,9));
        when(p4.chooseCard(any())).thenReturn(new Card(SUIT.CLUBS,14));
        
        EuchreGame game = new EuchreGame(host, state);

        assert(game.playTrick(null));
    }

    @Test
    public void testPlayTrickGoingAlone(){
        MockitoAnnotations.openMocks(this);

        when(state.getActivePlayer()).thenReturn(p1).thenReturn(p2).thenReturn(p3);
        when(state.getDealerPosition()).thenReturn(2);
        when(state.getHand(anyInt())).thenReturn(hand);
        when(state.getPlayedCards()).thenReturn(playedCards);
        when(playedCards.size()).thenReturn(1).thenReturn(1)
            .thenReturn(2).thenReturn(2)
            .thenReturn(3).thenReturn(3);
        when(state.getPlayerGoingAlone()).thenReturn(1);
        when(p1.chooseCard(any())).thenReturn(new Card(SUIT.SPADES,12));
        when(p2.chooseCard(any())).thenReturn(new Card(SUIT.HEARTS,11));
        when(p3.chooseCard(any())).thenReturn(new Card(SUIT.DIAMONDS,9));
        
        EuchreGame game = new EuchreGame(host, state);

        assert(game.playTrick(null));
    }


    @Test
    public void testScoreTrick(){
        MockitoAnnotations.openMocks(this);
        when(p3.chooseCard(any())).thenReturn(null);
        when(p1.getPosition()).thenReturn(1);
        when(p2.getPosition()).thenReturn(2);
        when(p3.getPosition()).thenReturn(3);
        when(p4.getPosition()).thenReturn(4);
    
        GameState unmockedState = new GameState();
        EuchreGame game = new EuchreGame(host, unmockedState);
        unmockedState.addPlayer(p1, 1);
        unmockedState.addPlayer(p2, 2);
        unmockedState.addPlayer(p3, 3);
        unmockedState.addPlayer(p4, 4);
        unmockedState.dealCards();
        unmockedState.setTrump(SUIT.HEARTS);
        unmockedState.setLeadingSuit(SUIT.SPADES);
        unmockedState.setLeadingPlayer(1);
        unmockedState.setActivePlayer(1);
        unmockedState.addPlayedCard(new Card(SUIT.SPADES,12));
        unmockedState.addPlayedCard(new Card(SUIT.DIAMONDS,11));
        unmockedState.addPlayedCard(new Card(SUIT.HEARTS,11));
        unmockedState.addPlayedCard(new Card(SUIT.SPADES,9));
        unmockedState.setLeadingPlayer(1);

        game.scoreTrick();

        assert(unmockedState.getTeamTricks(1) == 1);
        assert(unmockedState.getPlayedCards().size() == 0);
        assert(unmockedState.getTrump() == null);
        assert(unmockedState.getLeadingSuit() == null);
        assert(unmockedState.getActivePlayer().equals(p3));
        assert(unmockedState.getCurrentPhase() == PHASE.PLAYTRICK);

    }

    @Test
    public void testScoreRound(){
        MockitoAnnotations.openMocks(this);
        when(state.getTeamTricks(1)).thenReturn(3);
        when(state.getTeamTricks(2)).thenReturn(2);
        when(state.getAttackingTeam()).thenReturn(1);
        when(state.getPlayerGoingAlone()).thenReturn(-1);
        when(state.getActivePlayer()).thenReturn(p1);
        when(state.getDealerPosition()).thenReturn(1);
        when(state.getPlayer(anyInt())).thenReturn(p1);
        when(p1.chooseTrump(any())).thenReturn(null);

        EuchreGame game = new EuchreGame(host, state);

        game.scoreRound();

        verify(state, times(1)).addScore(1, 1);

        when(state.getTeamTricks(1)).thenReturn(5);
        when(state.getTeamTricks(2)).thenReturn(0);
        when(state.getAttackingTeam()).thenReturn(1);
        when(state.getPlayerGoingAlone()).thenReturn(-1);

        game.scoreRound();

        verify(state, times(1)).addScore(1, 2);

        when(state.getTeamTricks(1)).thenReturn(2);
        when(state.getTeamTricks(2)).thenReturn(3);
        when(state.getAttackingTeam()).thenReturn(1);
        when(state.getPlayerGoingAlone()).thenReturn(-1);

        game.scoreRound();

        verify(state, times(1)).addScore(2, 2);

        when(state.getTeamTricks(1)).thenReturn(5);
        when(state.getTeamTricks(2)).thenReturn(0);
        when(state.getAttackingTeam()).thenReturn(1);
        when(state.getPlayerGoingAlone()).thenReturn(1);

        game.scoreRound();

        verify(state, times(1)).addScore(1, 4);

        when(state.getTeamTricks(1)).thenReturn(4);
        when(state.getTeamTricks(2)).thenReturn(1);
        when(state.getAttackingTeam()).thenReturn(1);
        when(state.getPlayerGoingAlone()).thenReturn(1);

        game.scoreRound();

        verify(state, times(2)).addScore(1, 1);


    }
}
