package com.cs506group12.backend.models;

/**
 * Card Class to represent the playable cards in the game
 *
 * @author Kaldan
 *
 */
public class Card {

	/**
	 * Enumeration for the card class
	 */
	public enum SUIT {
		CLUBS,
		DIAMONDS,
		HEARTS,
		SPADES;

		SUIT getSuit() {
			return (this.getSuit());
		}
	}

	private final SUIT suit;
	private final int value;

	/**
	 * Constructor for the card class. Takes in a SUIT and a value
	 * 
	 * @param s
	 * @param val
	 */
	public Card(SUIT s, int val) {
		this.suit = s;
		this.value = val;
	}

	/**
	 * 
	 * Returns false if the other object is null or if the class is different.
	 * 
	 * Returns true if the card and suit are the same.
	 * 
	 * @param Object obj The other object that we will be checking equality with.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Card otherCard = (Card) obj;
		return this.value == otherCard.value && this.suit == otherCard.suit;
	}

	/**
	 * String method for the Card class. Returns the value and the suit as a string.
	 */
	@Override
	public String toString() {

		String toReturn = "";

		if (this.value == 9) {
			toReturn = "Nine";
		} else if (this.value == 10) {
			toReturn = "Ten";
		} else if (this.value == 11) {
			toReturn = "Jack";
		} else if (this.value == 12) {
			toReturn = "Queen";
		} else if (this.value == 13) {
			toReturn = "King";
		} else if (this.value == 14) {
			toReturn = "Ace";
		} else {
			toReturn = "NOT VALID";
		}

		toReturn += " of ";

		if (this.suit == SUIT.CLUBS) {
			toReturn += "Clubs";
		} else if (this.suit == SUIT.DIAMONDS) {
			toReturn += "Diamonds";
		} else if (this.suit == SUIT.HEARTS) {
			toReturn += "Hearts";
		} else {
			toReturn += "Spades";
		}

		return toReturn;
	}

	/**
	 * Method to calculate the value of the card to compare
	 * 
	 * @param trumpSuitCard The trump of the round
	 * @param leading       The leading card of this round
	 * @return The value of the card based on the trump and the leading value
	 */
	public int value(Card trumpSuitCard, SUIT leading) {
		SUIT sameColor = twinColor(trumpSuitCard);

		if (this.suit == trumpSuitCard.getSuit()) {
			if (this.value == 11) { // Jack of trump suit
				return 44;
			} else {
				return this.value + 28; // face + 28 (max is 42)
			}
		}

		if (this.suit == sameColor && this.value == 11) { // Jack of same color
			return 43; // one less than jack of trump, one more than max other value
		}

		if (this.suit == leading) {
			return this.value + 14;
		}

		return this.value;
	}

	/**
	 * Method to calculate what the other color is that matters for the trump.
	 * 
	 * @param trumpSuitCard The suit that is trump this round
	 * @return The corresponding suit for the trump
	 */
	public static SUIT twinColor(Card trumpSuitCard) {
		Card.SUIT suit = trumpSuitCard.getSuit();
		SUIT sameColor = null;
		switch (suit) {
			case CLUBS:
				sameColor = SUIT.SPADES;
				break;
			case SPADES:
				sameColor = SUIT.CLUBS;
				break;
			case HEARTS:
				sameColor = SUIT.DIAMONDS;
				break;
			default:
				sameColor = SUIT.HEARTS;
				break;
		}
		return sameColor;
	}

	/**
	 * Getter method for the suit of the card class
	 * 
	 * @return the suit of the card.
	 */
	public SUIT getSuit() {
		return this.suit;
	}

	/**
	 * Getter method for the vlaue of the card class
	 * 
	 * @return The value of the card
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Compares the valeus of the the two cards
	 * 
	 * @param other
	 * @param trumpSuitCard
	 * @param leading
	 * @return
	 */
	public boolean greater(Card other, Card trumpSuitCard, SUIT leading) {
		return this.value(trumpSuitCard, leading) > other.value(trumpSuitCard, leading);
	}

	public static SUIT twinColorSuit(SUIT trumpSuit) {
		SUIT sameColor = null;
		switch (trumpSuit) {
			case CLUBS:
				sameColor = SUIT.SPADES;
				break;
			case SPADES:
				sameColor = SUIT.CLUBS;
				break;
			case HEARTS:
				sameColor = SUIT.DIAMONDS;
				break;
			default:
				sameColor = SUIT.HEARTS;
				break;
		}
		return sameColor;
	}
}