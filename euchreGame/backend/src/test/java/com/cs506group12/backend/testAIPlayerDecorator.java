package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;

import com.cs506group12.backend.models.*;
import com.cs506group12.backend.models.Card.SUIT;

public class testAIPlayerDecorator {

    @Spy
    GameState state;
    
    @Test
    public void testGettersName(){
        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);
        assertEquals("EuchreBot 1", aiPlayer.getName());

        assert(aiPlayer.getPosition() == 1);
    }

    @Test
    public void testChooseTrumpGoingAlone(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getFaceUpCard()).thenReturn(new Card(Card.SUIT.HEARTS,9));
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.HEARTS,14));
        hand.addCard(new Card(Card.SUIT.HEARTS,13));
        hand.addCard(new Card(Card.SUIT.HEARTS,12));
        hand.addCard(new Card(Card.SUIT.HEARTS,11));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,11));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getDealerPosition()).thenReturn(1);

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card.SUIT suit = aiPlayer.chooseTrump(spyState);

        assertEquals(Card.SUIT.HEARTS, suit);
        verify(spyState, times(1)).setPlayerGoingAlone(1);

    }

    @Test
    public void testChooseTrumpBadHand(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getFaceUpCard()).thenReturn(new Card(Card.SUIT.CLUBS,9));
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.HEARTS,14));
        hand.addCard(new Card(Card.SUIT.HEARTS,13));
        hand.addCard(new Card(Card.SUIT.HEARTS,12));
        hand.addCard(new Card(Card.SUIT.HEARTS,11));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,11));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getDealerPosition()).thenReturn(1);

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card.SUIT suit = aiPlayer.chooseTrump(spyState);

        assertEquals(Card.SUIT.NONE, suit);
    }

    @Test
    public void testChooseTrumpOkHand(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getFaceUpCard()).thenReturn(new Card(Card.SUIT.SPADES,9));
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.SPADES,14));
        hand.addCard(new Card(Card.SUIT.SPADES,13));
        hand.addCard(new Card(Card.SUIT.HEARTS,12));
        hand.addCard(new Card(Card.SUIT.HEARTS,11));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,11));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getDealerPosition()).thenReturn(3);

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card.SUIT suit = aiPlayer.chooseTrump(spyState);

        assertEquals(Card.SUIT.NONE, suit);
    }

    @Test
    public void testChooseTrumpGoodHand(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getFaceUpCard()).thenReturn(new Card(Card.SUIT.SPADES,9));
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.SPADES,11));
        hand.addCard(new Card(Card.SUIT.CLUBS,11));
        hand.addCard(new Card(Card.SUIT.HEARTS,12));
        hand.addCard(new Card(Card.SUIT.HEARTS,11));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,11));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getDealerPosition()).thenReturn(3);

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card.SUIT suit = aiPlayer.chooseTrump(spyState);

        assertEquals(Card.SUIT.SPADES, suit);
    }

    @Test
    public void testChooseTrumpStickDealer(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getFaceUpCard()).thenReturn(new Card(Card.SUIT.SPADES,10));
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP2);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.SPADES,9));
        hand.addCard(new Card(Card.SUIT.CLUBS,9));
        hand.addCard(new Card(Card.SUIT.HEARTS,9));
        hand.addCard(new Card(Card.SUIT.HEARTS,10));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,9));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getDealerPosition()).thenReturn(1);

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card.SUIT suit = aiPlayer.chooseTrump(spyState);

        assertEquals(Card.SUIT.HEARTS, suit);
    }

    @Test
    public void testChooseCardLeadingPlayer(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getTrump()).thenReturn(SUIT.SPADES);
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP2);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.SPADES,11));
        hand.addCard(new Card(Card.SUIT.SPADES,14));
        hand.addCard(new Card(Card.SUIT.HEARTS,9));
        hand.addCard(new Card(Card.SUIT.HEARTS,10));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,9));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getLeadingPlayerPosition()).thenReturn(1);
        when(spyState.getLeadingSuit()).thenReturn(null);

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card c = aiPlayer.chooseCard(spyState);
        assertEquals(new Card(SUIT.SPADES, 11), c);
    }

    @Test
    public void testChooseCardFollowingPlayerWorse(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getTrump()).thenReturn(SUIT.HEARTS);
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP2);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.SPADES,11));
        hand.addCard(new Card(Card.SUIT.SPADES,14));
        hand.addCard(new Card(Card.SUIT.HEARTS,9));
        hand.addCard(new Card(Card.SUIT.HEARTS,10));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,9));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getLeadingPlayerPosition()).thenReturn(2);
        when(spyState.getLeadingSuit()).thenReturn(SUIT.HEARTS);
        when(spyState.highestPlayedCard()).thenReturn(new Card(SUIT.DIAMONDS,11));

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card c = aiPlayer.chooseCard(spyState);
        assertEquals(new Card(SUIT.HEARTS, 9), c);
    }

    @Test
    public void testChooseCardFollowingPlayerBetter(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getTrump()).thenReturn(SUIT.HEARTS);
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP2);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.SPADES,11));
        hand.addCard(new Card(Card.SUIT.SPADES,14));
        hand.addCard(new Card(Card.SUIT.HEARTS,9));
        hand.addCard(new Card(Card.SUIT.HEARTS,11));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,9));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getLeadingPlayerPosition()).thenReturn(2);
        when(spyState.getLeadingSuit()).thenReturn(SUIT.HEARTS);
        when(spyState.highestPlayedCard()).thenReturn(new Card(SUIT.DIAMONDS,11));

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card c = aiPlayer.chooseCard(spyState);
        assertEquals(new Card(SUIT.HEARTS, 11), c);
    }

    @Test
    public void testChooseReplacement(){
        MockitoAnnotations.openMocks(this);
        GameState state = new GameState();
        GameState spyState = spy(state);

        when(spyState.getTrump()).thenReturn(SUIT.HEARTS);
        when(spyState.getCurrentPhase()).thenReturn(GameState.PHASE.PICKTRUMP1);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.SPADES,11));
        hand.addCard(new Card(Card.SUIT.SPADES,14));
        hand.addCard(new Card(Card.SUIT.HEARTS,9));
        hand.addCard(new Card(Card.SUIT.HEARTS,11));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,9));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,14));
        when(spyState.getHand(1)).thenReturn(hand);
        when(spyState.getFaceUpCard()).thenReturn(new Card(SUIT.CLUBS, 11));

        EuchrePlayer player = new EuchrePlayer(null, 1);
        AIPlayerDecorator aiPlayer = new AIPlayerDecorator(player);

        Card c = aiPlayer.chooseReplacement(spyState);

        assertEquals(new Card(SUIT.DIAMONDS,9), c);
    }

}
