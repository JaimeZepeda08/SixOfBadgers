package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

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
		Card card1 = new Card("Clubs", 10);
		Card card2 = new Card("Clubs", 11);
		Card card3 = new Card("Clubs", 12);

		cards.add(card1);
		cards.add(card2);
		cards.add(card3);

		p.setHand(cards);

		ArrayList<Card> hand = p.getHand();

		for (int i = 0; i < hand.size(); i++) {
			assert (hand.get(i).getSuit().equals(cards.get(i).getSuit()));
			assert (hand.get(i).getRank() == cards.get(i).getRank());
		}

	}

}
