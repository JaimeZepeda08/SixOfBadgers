package com.cs506group12.backend;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Player;

public class testCard {

    public void testRank() {
        Card test = new Card("Clubs", 10);
        assert (test.getRank() == 10);
    }

    public void testSuit() {
        Card test = new Card("Clubs", 10);
        assert (test.getSuit().equals("Clubs"));
    }

    public void testAll() {
        Card test = new Card("Diamonds", 14);
        assert (test.getSuit().equals("Diamonds"));
        assert (test.getRank() == 14);
    }
}
