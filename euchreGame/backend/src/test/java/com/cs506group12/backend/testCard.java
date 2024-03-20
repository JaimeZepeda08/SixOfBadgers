package com.cs506group12.backend;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Player;

public class testCard {

	/**
	 * Tests Rank getter/setter
	 */
    public void testRank() {
        Card test = new Card("Clubs", 10);
        assert (test.getRank() == 10);
    }

    /**
     * Tests suit getter/setter
     */
    public void testSuit() {
        Card test = new Card("Clubs", 10);
        assert (test.getSuit().equals("Clubs"));
    }

    /**
     * Tests rank and suit together
     */
    public void testAll() {
        Card test = new Card("Diamonds", 14);
        assert (test.getSuit().equals("Diamonds"));
        assert (test.getRank() == 14);
    }

    /**
     *
     *
     */
    public void testCompareTo() {

    }

}
