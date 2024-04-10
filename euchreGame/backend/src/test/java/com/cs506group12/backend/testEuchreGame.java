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

    /**
     * tests is points correctly assigned after turn end
     */
    @Test
    public void testHandlePoints(){
        EuchreGame game = new EuchreGame();
        game.setAttackingTeam(1);
        assertEquals(2, game.handlePoints(0)); // defending team wins

        int[] testNumTricks = {2,3};
        game.setSoloPlayer(false);
        game.setNumTricks(testNumTricks);
        assertEquals(1, game.handlePoints(1));   // attacking team wins less than 5 tricks
        testNumTricks[0] = 1;
        testNumTricks[1] = 4;
        game.setNumTricks(testNumTricks);
        assertEquals(1, game.handlePoints(1));   // attacking team wins less than 5 tricks

        testNumTricks[0] = 0;
        testNumTricks[1] = 5;
        game.setNumTricks(testNumTricks);
        assertEquals(2, game.handlePoints(1));   // attacking team wins less than 5 tricks

        game.setSoloPlayer(true);
        assertEquals(4, game.handlePoints(1));   // solo player wins 5 tricks

    }

    /*
     * tests that other variables are correctly set after trick end
     */
    @Test
    public void testHandleTrick(){
        ArrayList<Card> testPlayedCards = new ArrayList<Card>();
        testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 12));
        testPlayedCards.add(new Card(Card.SUIT.CLUBS, 11));
        testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 13));
        testPlayedCards.add(new Card(Card.SUIT.HEARTS, 10));

        EuchreGame game = new EuchreGame();
        game.setAttackingTeam(0);
        game.setTrumpSuitCard(new Card(Card.SUIT.CLUBS, 0));
        game.setPlayedCards(testPlayedCards);
        
        assertEquals(1, game.score(testPlayedCards));
        // game.handleTrick();
        assertEquals(4,game.getPlayedCards().size());  // played cards is emptied after trick 


        // leading player starts at 1
        assertEquals(1, game.getLeadingPlayer());  // tests that the leading player for the next round is equal to the one who won the last trick

        assertEquals(0, game.getNumTricks()[1]);  // test trick winner is correctly assigned 
        assertEquals(0, game.getNumTricks()[0]);


    }

    // game loop is currently untested - will add once more finctionality 

}
