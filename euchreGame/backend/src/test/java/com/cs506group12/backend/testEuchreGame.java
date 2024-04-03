package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.EuchreGame;
import com.cs506group12.backend.models.Player;


/**
 * Tests the euchreGame class
 * @author Casey
 */
public class testEuchreGame {
	/**
	 * Tests the constructor.
	 */
	@Test
	public void testEuchreGameConstructor() {
		EuchreGame game = new EuchreGame();
		assertNotNull(game);

	}


    /**
     * tests the score method
     */
    @Test
	public void testEuchreGameScore() {
        ArrayList<Card> testPlayedCards = new ArrayList<Card>();
        testPlayedCards.add(new Card(Card.SUIT.CLUBS, 14));
        testPlayedCards.add(new Card(Card.SUIT.CLUBS, 11));
        testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 12));

		EuchreGame game = new EuchreGame();
        game.setTrumpSuitCard(new Card(Card.SUIT.DIAMONDS, 10));
        game.setPlayedCards(testPlayedCards);
		assertEquals(2,game.score(testPlayedCards)); //  tests if lower card with trump beats higher card without trump
        game.setTrumpSuitCard(new Card(Card.SUIT.CLUBS, 10));
        assertEquals(1, game.score(testPlayedCards));  // tests if trump jack beats higher value of same suit
        game.setTrumpSuitCard(new Card(Card.SUIT.HEARTS, 0));
        assertEquals(0, game.score(testPlayedCards)); // tests highest card wins when no trump card played

        testPlayedCards.add(new Card(Card.SUIT.HEARTS, 14));
        testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 11)); // tests if same color jack is second highest card
        assertEquals(4, game.score(testPlayedCards));

        testPlayedCards.add(new Card(Card.SUIT.HEARTS, 11)); // jack of trump suit higher
        assertEquals(5, game.score(testPlayedCards));


	}

}
