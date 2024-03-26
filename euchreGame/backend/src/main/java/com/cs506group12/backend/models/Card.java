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
		SPADES
	}

	public final SUIT suit;
	public final int value;

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

		String ret = "";

		if (this.value == 9) {
			ret = "Nine";
		} else if (this.value == 10) {
			ret = "Ten";
		} else if (this.value == 11) {
			ret = "Jack";
		} else if (this.value == 12) {
			ret = "Queen";
		} else if (this.value == 13) {
			ret = "King";
		} else if (this.value == 14) {
			ret = "Ace";
		} else {
			ret = "NOT VALID";
		}

		ret += " of ";

		if (this.suit == SUIT.CLUBS) {
			ret += "Clubs";
		} else if (this.suit == SUIT.DIAMONDS) {
			ret += "Diamonds";
		} else if (this.suit == SUIT.HEARTS) {
			ret += "Hearts";
		} else {
			ret += "Spades";
		}

		return ret;
	}


	/**
	 * Method to calculate the value of the card to compare
	 * 
	 * @param trump The trump of the round
	 * @param leading The leading card of this round
	 * @return The value of the card based on the trump and the leading value
	 */
	public int value(SUIT trump, SUIT leading) {
		SUIT sameColor = twinColor(trump);
	
		if (this.suit == trump) {
			if (this.value == 11) { /*Jack of trump suit*/
				return 44;
			} else {
				return this.value + 28;
			}
		}
	
		if (this.suit == sameColor && this.value == 11) { /*Jack of same color*/
			return 43;
		}
	
		if (this.suit == leading) {
			return this.value + 14;
		}
	
		return this.value;
	}
	

	/**
	 * Method to calculate what the other color is that matters for the trump.\
	 * 
	 * @param trump The suit that is trump this round
	 * @return The corresponding suit for the trump
	 */
	public static SUIT twinColor(SUIT trump){

		SUIT sameColor = null;
		switch (trump) 
		{
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
	public SUIT getSuit(){
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



	


}