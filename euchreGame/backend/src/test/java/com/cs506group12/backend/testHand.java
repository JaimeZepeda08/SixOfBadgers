package com.cs506group12.backend;

import com.cs506group12.backend.models.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class testHand {
    
    @Test
    public void testPlayableCards(){
        Hand hand = new Hand();

        hand.addCard(new Card(Card.SUIT.CLUBS, 9));
        hand.addCard(new Card(Card.SUIT.DIAMONDS, 10));
        hand.addCard(new Card(Card.SUIT.HEARTS, 11));
        hand.addCard(new Card(Card.SUIT.CLUBS, 11));
        hand.addCard(new Card(Card.SUIT.SPADES, 11));

        boolean[] playableCards = hand.getPlayableCards(Card.SUIT.CLUBS, Card.SUIT.CLUBS);

        assertTrue(playableCards[0]);
        assertFalse(playableCards[1]);
        assertFalse(playableCards[2]);
        assertTrue(playableCards[3]);
        assertTrue(playableCards[4]);


        playableCards = hand.getPlayableCards(Card.SUIT.SPADES, Card.SUIT.CLUBS);
        
        assertTrue(playableCards[0]);
        assertTrue(playableCards[1]);
        assertTrue(playableCards[2]);
        assertTrue(playableCards[3]);
        assertTrue(playableCards[4]);

        playableCards = hand.getPlayableCards(Card.SUIT.SPADES, Card.SUIT.SPADES);
        
        assertFalse(playableCards[0]);
        assertFalse(playableCards[1]);
        assertFalse(playableCards[2]);
        assertTrue(playableCards[3]);
        assertTrue(playableCards[4]);

        playableCards = hand.getPlayableCards(Card.SUIT.DIAMONDS, Card.SUIT.DIAMONDS);
        
        assertFalse(playableCards[0]);
        assertTrue(playableCards[1]);
        assertTrue(playableCards[2]);
        assertFalse(playableCards[3]);
        assertFalse(playableCards[4]);

        playableCards = hand.getPlayableCards(Card.SUIT.HEARTS, Card.SUIT.DIAMONDS);
        
        assertTrue(playableCards[0]);
        assertTrue(playableCards[1]);
        assertTrue(playableCards[2]);
        assertTrue(playableCards[3]);
        assertTrue(playableCards[4]);
    }

    @Test
    public void testAddGetRemoveCard(){
        Hand hand = new Hand();
        Card c = new Card(Card.SUIT.CLUBS, 9);
        hand.addCard(c);

        assertTrue(hand.getCard(0).equals(c));
        
        hand.removeCard(c);

        try {
            hand.getCard(0);
            assert(false);
        } catch (IndexOutOfBoundsException e) {
            assert(true);
        }

        hand.addCard(c);
        hand.clearHand();
        
        try {
            hand.getCard(0);
            assert(false);
        } catch (IndexOutOfBoundsException e) {
            assert(true);
        }
    }

    @Test
    public void testSqlString(){
        Hand hand = new Hand();

        hand.addCard(new Card(Card.SUIT.CLUBS, 9));
        hand.addCard(new Card(Card.SUIT.DIAMONDS, 10));
        hand.addCard(new Card(Card.SUIT.HEARTS, 11));
        hand.addCard(new Card(Card.SUIT.CLUBS, 11));
        hand.addCard(new Card(Card.SUIT.SPADES, 11));

        String sqlString = hand.toSqlString();

        assertEquals("9C,10D,11H,11C,11S", sqlString);

    }

    @Test
    public void testSize(){
        Hand hand = new Hand();
        
        assertTrue(hand.getSize() == 0);

        hand.addCard(new Card(Card.SUIT.CLUBS, 9));
        hand.addCard(new Card(Card.SUIT.DIAMONDS, 10));
        hand.addCard(new Card(Card.SUIT.HEARTS, 11));

        assertTrue(hand.getSize() == 3);
    }
}
