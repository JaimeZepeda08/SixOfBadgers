package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.EuchreGame;
import com.cs506group12.backend.models.Player;
import org.json.*;


/**
 * This class tests the correctness of the Player class.
 * 
 * @author kaldan
 */
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
		Player p = new Player("TEST");
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.HEARTS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.HEARTS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.SPADES, 9));
		assertEquals(5, p.getSuit(Card.SUIT.CLUBS).size());
		assertEquals(0, p.getSuit(Card.SUIT.DIAMONDS).size());
		assertEquals(2, p.getSuit(Card.SUIT.HEARTS).size());
		assertEquals(1, p.getSuit(Card.SUIT.SPADES).size());
	}

	/**
	 * Tests the high card of suit function
	 */
	@Test
	public void testGetHighCardofSuit() {
		Player p = new Player("TEST");
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 14));
		p.hand.add(new Card(Card.SUIT.HEARTS, 10));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.SPADES, 13));
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 11));

		assertEquals(new Card(Card.SUIT.DIAMONDS,14),p.getHighCardofSuit(Card.SUIT.DIAMONDS));
	}

	/**
	 * Tests the low card of suit function
	 */
	@Test
	public void testGetLowCardofSuit() {
		Player p = new Player("TEST");
		p.hand.add(new Card(Card.SUIT.SPADES, 13));
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 14));
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 11));
		p.hand.add(new Card(Card.SUIT.CLUBS, 9));
		p.hand.add(new Card(Card.SUIT.HEARTS, 10));
		
		assertEquals(new Card(Card.SUIT.CLUBS,9),p.getLowCardOfSuit(Card.SUIT.CLUBS));
	}


	@Test
	public void testGetPlayableCardsAllLeadingSuit()
	{
		Player test = new Player("TEST");
		test.hand.add(new Card(Card.SUIT.HEARTS,9));
		test.hand.add(new Card(Card.SUIT.HEARTS,10));
		test.hand.add(new Card(Card.SUIT.HEARTS,11));
		test.hand.add(new Card(Card.SUIT.HEARTS,12));
		test.hand.add(new Card(Card.SUIT.HEARTS,13));
		
		boolean[] playable = test.getPlayableCards(Card.SUIT.HEARTS, Card.SUIT.CLUBS);
		
		assertEquals(true,playable[0]);
		assertEquals(true,playable[1]);
		assertEquals(true,playable[2]);
		assertEquals(true,playable[3]);
		assertEquals(true,playable[4]);
	}
	
	@Test
	public void testGetPlayableCardsNoneLeadingSuit()
	{
		Player test = new Player("TEST");
		test.hand.add(new Card(Card.SUIT.HEARTS,9));
		test.hand.add(new Card(Card.SUIT.HEARTS,10));
		test.hand.add(new Card(Card.SUIT.HEARTS,11));
		test.hand.add(new Card(Card.SUIT.HEARTS,12));
		test.hand.add(new Card(Card.SUIT.HEARTS,13));
		
		boolean[] playable = test.getPlayableCards(Card.SUIT.DIAMONDS, Card.SUIT.CLUBS);
		
		assertEquals(true,playable[0]);
		assertEquals(true,playable[1]);
		assertEquals(true,playable[2]);
		assertEquals(true,playable[3]);
		assertEquals(true,playable[4]);
	}

	@Test
	public void testHasCardOfSuit() {
		Player p = new Player("TEST");
		p.hand.add(new Card(Card.SUIT.SPADES, 13));
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 14));
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 11));
		p.hand.add(new Card(Card.SUIT.SPADES, 9));
		p.hand.add(new Card(Card.SUIT.HEARTS, 10));
		
		assertEquals(true, p.hasSuitCard(Card.SUIT.DIAMONDS));
		assertEquals(true, p.hasSuitCard(Card.SUIT.HEARTS));
		assertEquals(true, p.hasSuitCard(Card.SUIT.SPADES));
		assertEquals(false, p.hasSuitCard(Card.SUIT.CLUBS));
	}

	/**
	 * 
	 * @param json string to check if json
	 * @return true if stirng is a valid json- false otherwise
	 */
	public boolean isValidJson(String json) {
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}
	/**
	 * test that the handAsJson works
	 */
	@Test
	public void testHandAsJson(){
		Player p = new Player("TEST");
		p.hand.add(new Card(Card.SUIT.SPADES, 13));
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 14));
		p.hand.add(new Card(Card.SUIT.DIAMONDS, 11));
		p.hand.add(new Card(Card.SUIT.SPADES, 9));
		p.hand.add(new Card(Card.SUIT.HEARTS, 10));
		System.out.println(p.handAsJson());
		String pHandAsJson = p.handAsJson();
		//assertEquals("" , p.handAsJson());
		assertTrue(isValidJson(pHandAsJson));  // tests that it is valid josn 


	}

	@Test
    void testFindPartner() {
        // Create players
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        Player player3 = new Player("Charlie");
        Player player4 = new Player("David");

        // Assume players 1 and 2 are in the same team, and players 3 and 4 are in the same team
		ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        // Test finding partners
        assertEquals(player2, player1.findPartner(players));
        assertEquals(player1, player2.findPartner(players));
        assertEquals(player4, player3.findPartner(players));
        assertEquals(player3, player4.findPartner(players));
    }

	@Test
    void testGameInitialization() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        Player player3 = new Player("Charlie");
        Player player4 = new Player("David");
        
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        
        EuchreGame game = new EuchreGame();
        
        assertNotNull(game);
        // Add more assertions to test game initialization
    }

	public class TeamTest {
		@Test
		void testTeamInitialization() {
			Player player1 = new Player("Alice");
			Player player2 = new Player("Bob");
			
			ArrayList<Player> players = new ArrayList<>();
			players.add(player1);
			players.add(player2);
			assertNotNull(players);
		}
	}
}



