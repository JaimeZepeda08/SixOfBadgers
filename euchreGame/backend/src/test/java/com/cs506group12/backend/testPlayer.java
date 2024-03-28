package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Player;

import net.bytebuddy.description.type.TypeDescription.Generic.Visitor.TypeErasing;

@SuppressWarnings("unused")
public class testPlayer {

	public void testName() {
		String name = "TestPlayer";
		Player p = new Player("TestPlayer");
		assert (name.equals(p.userName));
	}

	public void testGetHand() {
		Player p = new Player("TestPlayer");

		ArrayList<Card> cards = new ArrayList<Card>();
		Card card1 = new Card(Card.SUIT.CLUBS, 10);
		Card card2 = new Card(Card.SUIT.CLUBS, 11);
		Card card3 = new Card(Card.SUIT.CLUBS, 12);

		cards.add(card1);
		cards.add(card2);
		cards.add(card3);

		p.setHand(cards);

		ArrayList<Card> hand = p.getHand();

		for (int i = 0; i < hand.size(); i++) {
			assert (hand.get(i).getSuit().equals(cards.get(i).getSuit()));
			assert (hand.get(i).getValue() == cards.get(i).getValue());
		}

	}

	/**
	 * Tests the getSuit function.
	 */
	@Test
	public void testGetSuit() {
		Player p = new Player("TestPlayer");
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.HEARTS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.HEARTS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.SPADES, 9));
		assertEquals(5,p.getSuit(Card.SUIT.CLUBS).size());
		assertEquals(0,p.getSuit(Card.SUIT.DIAMONDS).size());
		assertEquals(2,p.getSuit(Card.SUIT.HEARTS).size());
		assertEquals(1,p.getSuit(Card.SUIT.SPADES).size());
	}


}
