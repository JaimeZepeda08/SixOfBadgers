package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs506group12.backend.interfaces.Player;
import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Card.SUIT;
import com.cs506group12.backend.models.Client;
import com.cs506group12.backend.models.EuchreGame;
import com.cs506group12.backend.models.GameState;
import com.cs506group12.backend.models.GameState.PHASE;
import com.cs506group12.backend.models.Hand;

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


    @Test
    public void testPlayerRotation(){
        MockitoAnnotations.openMocks(this);
        EuchreGame game = new EuchreGame(host, state);

        // Simulate player rotation after a trick
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
        
        assertTrue(game.playTrick(null));
    }   

    @Test
    public void testScoreRoundDifferentTrickCounts(){
        MockitoAnnotations.openMocks(this);
        // Scenario 1: Team 1 - 3 tricks, Team 2 - 2 tricks
        when(state.getTeamTricks(1)).thenReturn(3);
        when(state.getTeamTricks(2)).thenReturn(2);
        when(state.getAttackingTeam()).thenReturn(1);
        when(state.getPlayerGoingAlone()).thenReturn(-1);
        when(state.getActivePlayer()).thenReturn(p1);
        when(state.getDealerPosition()).thenReturn(1);
        when(state.getPlayer(anyInt())).thenReturn(p1);
        when(p1.chooseTrump(any())).thenReturn(null);

        EuchreGame game1 = new EuchreGame(host, state);
        game1.scoreRound();

        verify(state, times(1)).addScore(1, 1);

        // Scenario 2: Team 1 - 5 tricks, Team 2 - 0 tricks
        when(state.getTeamTricks(1)).thenReturn(5);
        when(state.getTeamTricks(2)).thenReturn(0);

        EuchreGame game2 = new EuchreGame(host, state);
        game2.scoreRound();

        verify(state, times(1)).addScore(1, 2);

        // Scenario 3: Team 1 - 2 tricks, Team 2 - 3 tricks
        when(state.getTeamTricks(1)).thenReturn(2);
        when(state.getTeamTricks(2)).thenReturn(3);

        EuchreGame game3 = new EuchreGame(host, state);
        game3.scoreRound();

        verify(state, times(1)).addScore(2, 2);

        // Scenario 4: Team 1 - 5 tricks, Team 2 - 0 tricks, Player going alone
        when(state.getTeamTricks(1)).thenReturn(5);
        when(state.getTeamTricks(2)).thenReturn(0);
        when(state.getPlayerGoingAlone()).thenReturn(1);

        EuchreGame game4 = new EuchreGame(host, state);
        game4.scoreRound();

        verify(state, times(1)).addScore(1, 4);

        // Scenario 5: Team 1 - 4 tricks, Team 2 - 1 trick, Player going alone
        when(state.getTeamTricks(1)).thenReturn(4);
        when(state.getTeamTricks(2)).thenReturn(1);

        EuchreGame game5 = new EuchreGame(host, state);
        game5.scoreRound();

        verify(state, times(2)).addScore(1, 1);
    }

    @Test
    public void testPlayerRotationDuringTrickPlay(){
        MockitoAnnotations.openMocks(this);
        EuchreGame game = new EuchreGame(host, state);

        // Simulate player rotation after each trick
        when(state.getActivePlayer()).thenReturn(p1).thenReturn(p2).thenReturn(p3).thenReturn(p4);
        when(state.getDealerPosition()).thenReturn(2);
        when(state.getHand(anyInt())).thenReturn(hand);
        when(state.getPlayedCards()).thenReturn(playedCards);
        // Indicate the number of cards played in each trick
        when(playedCards.size()).thenReturn(1).thenReturn(1)
            .thenReturn(2).thenReturn(2)
            .thenReturn(3).thenReturn(3)
            .thenReturn(4).thenReturn(4);
        when(state.getPlayerGoingAlone()).thenReturn(-1);
        // Mock card choices for each player
        when(p1.chooseCard(any())).thenReturn(new Card(SUIT.SPADES,12));
        when(p2.chooseCard(any())).thenReturn(new Card(SUIT.HEARTS,11));
        when(p3.chooseCard(any())).thenReturn(new Card(SUIT.DIAMONDS,9));
        when(p4.chooseCard(any())).thenReturn(new Card(SUIT.CLUBS,14));
        
        assertTrue(game.playTrick(null));
    }   

    @Test
    public void testScoreRoundWithTrumpSuit(){
        MockitoAnnotations.openMocks(this);
        // Scenario 1: Team 1 - 3 tricks, Team 2 - 2 tricks, Trump: Spades
        when(state.getTeamTricks(1)).thenReturn(3);
        when(state.getTeamTricks(2)).thenReturn(2);
        when(state.getAttackingTeam()).thenReturn(1);
        when(state.getPlayerGoingAlone()).thenReturn(-1);
        when(state.getActivePlayer()).thenReturn(p1);
        when(state.getDealerPosition()).thenReturn(1);
        when(state.getPlayer(anyInt())).thenReturn(p1);
        // Mock player choosing Trump suit
        when(p1.chooseTrump(any())).thenReturn(SUIT.SPADES);

        EuchreGame game1 = new EuchreGame(host, state);
        game1.scoreRound();

        // Scenario 2: Team 1 - 5 tricks, Team 2 - 0 tricks, Trump: Hearts
        when(state.getTeamTricks(1)).thenReturn(5);
        when(state.getTeamTricks(2)).thenReturn(0);
        // Mock player choosing Trump suit
        when(p1.chooseTrump(any())).thenReturn(SUIT.HEARTS);

        EuchreGame game2 = new EuchreGame(host, state);
        game2.scoreRound();

        verify(state, times(1)).addScore(1, 2); // Team 1 wins with a clean sweep and Hearts as Trump
    }

    @Test
    public void testPlayerRotationWithChangingDealerPosition(){
        MockitoAnnotations.openMocks(this);
        EuchreGame game = new EuchreGame(host, state);

        // Simulate player rotation with changing dealer position
        when(state.getActivePlayer()).thenReturn(p1).thenReturn(p2).thenReturn(p3).thenReturn(p4);
        // Dealer position changes after each trick
        when(state.getDealerPosition()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4);
        when(state.getHand(anyInt())).thenReturn(hand);
        when(state.getPlayedCards()).thenReturn(playedCards);
        // Indicate the number of cards played in each trick
        when(playedCards.size()).thenReturn(1).thenReturn(1)
            .thenReturn(2).thenReturn(2)
            .thenReturn(3).thenReturn(3)
            .thenReturn(4).thenReturn(4);
        when(state.getPlayerGoingAlone()).thenReturn(-1);
        // Mock card choices for each player
        when(p1.chooseCard(any())).thenReturn(new Card(SUIT.SPADES,12));
        when(p2.chooseCard(any())).thenReturn(new Card(SUIT.HEARTS,11));
        when(p3.chooseCard(any())).thenReturn(new Card(SUIT.DIAMONDS,9));
        when(p4.chooseCard(any())).thenReturn(new Card(SUIT.CLUBS,14));
        
        assertTrue(game.playTrick(null));
    }   


}
