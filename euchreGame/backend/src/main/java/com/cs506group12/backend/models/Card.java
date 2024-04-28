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
		SPADES,
		NONE; //Used for selecting trump. NONE is equivalent to passing. Please don't instantiate cards with no suit.

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
		} else if (this.suit == SUIT.SPADES){
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
		return value(trumpSuitCard.suit, leading);
	}

	public int value(SUIT trump, SUIT leading){
		SUIT sameColor = twinColor(trump);

		if (this.suit == trump) {
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
		return twinColor(trumpSuitCard.suit);
	}

	public static SUIT twinColor(SUIT suit){
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
			case DIAMONDS:
				sameColor = SUIT.HEARTS;
				break;
			case NONE:
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

	/**
	 * Gets the suit of the same color as the input. Used for classifying the left bauer.
	 * @param trumpSuit The suit whose pair should be returned
	 * @return The suit of the same color as the input suit.
	 */
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
			case DIAMONDS:
				sameColor = SUIT.HEARTS;
				break;
			case NONE:
				break;
		}
		return sameColor;
	}

	/**
	 * Converts this Card object into a String formatted for JSON
	 * @return The String representation of the Card
	 */
	public String cardToJSON() {
		String suitString = suitToJSON(this.suit);
		return value + "-" + suitString;
	}

	/**
	 * Converts a card into a consice format for storage in the SQL database. Used to 
	 * store the game state.
	 * @return The String representation of this Card
	 */
	public String toSqlString(){
		String sqlString = "" + this.value;
		switch (this.suit) {
			case CLUBS:
				sqlString += "C";
				break;
			case SPADES:
				sqlString += "S";
				break;
			case HEARTS:
				sqlString += "H";
				break;
			case DIAMONDS:
				sqlString += "D";
				break;
			case NONE:
				break;
		}

		return sqlString;
	}

	/**
	 * Creates a card object from a String stored in the SQL database
	 * @param cardString The String to be converted into a Card
	 * @return The Card representation of the String
	 */
	public static Card fromSqlString(String cardString){
		String suit = String.valueOf(cardString.charAt(cardString.length() - 1));
		return new Card(stringToSuit(suit), Integer.valueOf(cardString.substring(0, cardString.length() - 1)));
	}

	/**
	 * Converts a single-character or fully elaborated string to a suit enum.
	 * @param suitString The single-character string to convert to a suit.
	 * @return The suit represented by the string
	 */
	public static SUIT stringToSuit(String suitString){
		switch(suitString){
			case "C":
			case "CLUBS":
				return SUIT.CLUBS;
			case "S":
			case "SPADES":
				return SUIT.SPADES;
			case "H":
			case "HEARTS":
				return SUIT.HEARTS;
			case "D":
			case "DIAMONDS":
				return SUIT.DIAMONDS;
			default:
				return SUIT.NONE;

		}
	}

	/**
	 * Returns the suit as a simple one-character String
	 * @param suit The suit to become a string
	 * @return The suit represented in a one-character String (C,S,H, or D)
	 */
	public static String suitToString(SUIT suit){
		switch (suit) {
			case CLUBS:
				return "C";
			case SPADES:
				return "S";
			case HEARTS:
				return "H";
			case DIAMONDS:
				return "D";
			default:
				return "";
		}
	}

	public static String suitToJSON(SUIT suit){
		if(suit == null){
			return "NONE";
		}
		switch (suit) {
			case SPADES:
				return "SPADES";
			case DIAMONDS:
				return "DIAMONDS";
			case CLUBS:
				return "CLUBS";
			case HEARTS:
				return "HEARTS";
			default:
				return "NONE";
		}
	}

	public static Card fromJSON(String input){
		String value = input.substring(0, input.indexOf("-"));
		String suit = input.substring(input.indexOf("-") + 1, input.length());
		return new Card(Card.stringToSuit(suit), Integer.parseInt(value));
	}
}