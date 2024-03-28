package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Player;

public class testCard {

	/**
	 * Tests Rank getter/setter
	 */
   @Test
	public void testCard() {
		Card c = new Card(Card.SUIT.CLUBS, 9);
		assertNotNull(c);
        
        Card test = new Card(Card.SUIT.HEARTS,14);

    }

    /**
     * Tests the quality operator.
     */
    @Test
	public void testEqualsObject() {
		Card c1 = new Card(Card.SUIT.DIAMONDS, 10);
		Card c2 = new Card(Card.SUIT.DIAMONDS, 10);
		assertEquals(c1,c2);
		Card c3 = new Card(Card.SUIT.HEARTS, 9);
		Card c4 = new Card(Card.SUIT.HEARTS, 9);
		assertEquals(c3,c4);
		Card c5 = new Card(Card.SUIT.SPADES, 13);
		Card c6 = new Card(Card.SUIT.SPADES, 13);
		assertEquals(c5,c6);

        // Tests a not equal card value
		Card c7 = new Card(Card.SUIT.DIAMONDS, 14);
		Card c8 = new Card(Card.SUIT.DIAMONDS, 13);
		assertFalse(c7.equals(c8));

        //Tests a not equal suit but same value
        Card c9 = new Card(Card.SUIT.DIAMONDS, 14);
        Card c10 = new Card(Card.SUIT.SPADES, 14);
    }

}
