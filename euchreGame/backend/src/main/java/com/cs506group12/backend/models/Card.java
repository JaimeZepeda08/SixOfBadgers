package com.cs506group12.backend.models;

/**
 * Card Class to represent the playable cards in the game
 *
 * @author Kaldan
 *
 */
public class Card {
	private final String suit;
	private final int rank;

	/**
	 * Constructor to create a card with a String represent suit, and a rank
	 * represented card
	 */
	public Card(String suit, int rank) {
		this.suit = suit;
		this.rank = rank;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return " " + rank + " of " + suit;
	}

	public String getSuit() {
		return suit;
	}

	public int getRank() {
		return rank;
	}

	/**
     * Compares this card with the specified card for order. Cards are compared
     * based on their ranks.
     *
     * @param other the card to be compared
     * @return a negative integer, zero, or a positive integer as this card is less
     *         than, equal to, or greater than the specified card.
     */
    @Override
    public int compareTo(Card other, String leadSuit, String trumpSuit) {
  
    	if (other.suit.equals(leadSuit)) {
    			return this.rank - other.rank;
    	} else if (!this.card.suit.equals(trumpSuit) && other.suit.equals(trumpSuit)) {
				return -1;
			} else if (this.card.suit.equals(trumpSuit) && !other.suit.equals(trumpSuit)) {
				return 1;
			} else {
				return trumpCompare(Card other);
			}
    		return 0;
    	}

			public int trumpCompare(Card other) {

			}

}
