package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.json.*;

import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Card.SUIT;

/**
 * This class tests the correctness of the Card class
 * 
 * @author jaime zepeda & kaldan kopp
 */
class testCard {

	/**
	 * Tests the Card constructor.
	 */
	@Test
	void testCardConstructor() {
		Card c = new Card(Card.SUIT.CLUBS, 9);
		assertNotNull(c);

	}

	/**
	 * Tests the quality operator.
	 */
	@Test
	void testEqualsObject() {
		Card c1 = new Card(Card.SUIT.DIAMONDS, 10);
		Card c2 = new Card(Card.SUIT.DIAMONDS, 10);
		assertEquals(c1, c2);
	}

	/**
	 * Tests the equals() method for inequality in value.
	 */
	@Test
	public void testEqualsObjectInequalityValue() {
		Card c1 = new Card(Card.SUIT.DIAMONDS, 10);
		Card c2 = new Card(Card.SUIT.DIAMONDS, 11);
		assertFalse(c1.equals(c2));
	}

	/**
	 * Tests the equals() method for inequality in suit.
	 */
	@Test
	public void testEqualsObjectInequalitySuit() {
		Card c1 = new Card(Card.SUIT.DIAMONDS, 10);
		Card c2 = new Card(Card.SUIT.CLUBS, 10);
		assertFalse(c1.equals(c2));
	}

	/**
	 * Tests the toString() method.
	 */
	@Test
	public void testToString() {
		Card c = new Card(Card.SUIT.HEARTS, 14);
		assertEquals("Ace of Hearts", c.toString());
	}

	/**
	 * Tests the value() method for trump card.
	 */
	@Test
	public void testValueTrump() {
		Card trumpCard = new Card(Card.SUIT.CLUBS, 10);
		int value = trumpCard.value(new Card(Card.SUIT.CLUBS, 1), Card.SUIT.DIAMONDS);
		assertEquals(38, value); // 10 + 28 = 38
	}

	/**
	 * Tests the value() method for jack of trump suit.
	 */
	@Test
	public void testValueJackOfTrumpSuit() {
		Card trumpJackCard = new Card(Card.SUIT.HEARTS, 11);
		int value = trumpJackCard.value(new Card(Card.SUIT.HEARTS, 1), Card.SUIT.CLUBS);
		assertEquals(44, value); // Jack of trump suit
	}

	/**
	 * Tests the value() method for jack of same color as trump.
	 */
	@Test
	public void testValueJackOfSameColor() {
		Card sameColorJackCard = new Card(Card.SUIT.CLUBS, 11);
		int value = sameColorJackCard.value(new Card(Card.SUIT.SPADES, 1), Card.SUIT.DIAMONDS);
		assertEquals(43, value); // Jack of same color
	}

	@Test
	public void testValueTrumpJack() {
		Card trumpJackCard = new Card(Card.SUIT.SPADES, 11);
		int value = trumpJackCard.value(new Card(Card.SUIT.SPADES, 1), Card.SUIT.DIAMONDS);
		assertEquals(44, value); // Jack of same color
	}

	/**
	 * Tests the value() method for leading card.
	 */
	@Test
	public void testValueLeading() {
		Card leadingCard = new Card(Card.SUIT.HEARTS, 9);
		int value = leadingCard.value(new Card(Card.SUIT.CLUBS, 1), Card.SUIT.HEARTS);
		assertEquals(23, value); // 9 + 14 = 23
	}

	/**
	 * Tests the twinColor() method with different trump suits.
	 */
	@Test
	public void testTwinColor() {
		assertEquals(Card.SUIT.SPADES, Card.twinColorSuit(Card.SUIT.CLUBS));
		assertEquals(Card.SUIT.CLUBS, Card.twinColorSuit(Card.SUIT.SPADES));
		assertEquals(Card.SUIT.DIAMONDS, Card.twinColorSuit(Card.SUIT.HEARTS));
		assertEquals(Card.SUIT.HEARTS, Card.twinColorSuit(Card.SUIT.DIAMONDS));
	}

	/**
	 * Tests the getSuit() method.
	 */
	@Test
	public void testGetSuit() {
		Card card = new Card(Card.SUIT.CLUBS, 9);
		assertEquals(Card.SUIT.CLUBS, card.getSuit());
	}

	/**
	 * Tests the getValue() method.
	 */
	@Test
	public void testGetValue() {
		Card card = new Card(Card.SUIT.CLUBS, 9);
		assertEquals(9, card.getValue());
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

	@Test
	public void testSqlString(){
		Card c = new Card(Card.SUIT.CLUBS, 14);
		String sqlString = c.toSqlString();
		assertEquals("14C", sqlString);
	}

	@Test
	public void testFromSqlString(){
		Card c = Card.fromSqlString("9D");
		assertEquals(new Card(Card.SUIT.DIAMONDS,9), c);

		c = Card.fromSqlString("14S");
		assertEquals(new Card(Card.SUIT.SPADES,14), c);
	}

	@Test
	public void testFromJSON(){
		Card c = new Card(SUIT.SPADES,13);
		String json = c.cardToJSON();
		Card testCard = Card.fromJSON(json);

		assertEquals(c, testCard);

		c = new Card(SUIT.DIAMONDS,11);
		json = c.cardToJSON();
		testCard = Card.fromJSON(json);

		assertEquals(c, testCard);
		
		c = new Card(SUIT.HEARTS,9);
		json = c.cardToJSON();
		testCard = Card.fromJSON(json);

		assertEquals(c, testCard);

		c = new Card(SUIT.CLUBS,14);
		json = c.cardToJSON();
		testCard = Card.fromJSON(json);

		assertEquals(c, testCard);
	}
}
