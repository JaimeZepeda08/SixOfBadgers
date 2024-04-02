package com.cs506group12.backend.models;

import java.util.*;
import com.cs506group12.backend.models.*;

public class Player {
	public ArrayList<Card> hand = new ArrayList<Card>();
	public String userName;
	private int points;
	// maybe a team function - i wpuld prefer for teammates to be 0 and 1 in array
	// but maybe no possible?
	// do i need login functions?

	/**
	 * Constructor class for the Player class.
	 */
	public Player(String name) {
		this.userName = name;
		this.hand = new ArrayList<Card>();
	}

	/*
	 * Setter class for the hand of the player
	 */
	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	/*
	 * Getter class for the hand of the player
	 */
	public ArrayList<Card> getHand() {
		return this.hand;
	}

	/**
	 * Getter method for the number of points for the player
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * returns the highest card of the passed param suit
	 * 
	 * @param suit The suit that we want the highest card of.
	 * @return The card that is the highest of the param's suit in the player's hand
	 */
	public Card getHighCardofSuit(Card.SUIT suit) {

		Card high = null;

		for (Card c: hand) {
			if (c.getSuit() == suit && (high==null || c.getValue() > high.getValue())) {
				high = c;
			}
		}

		return high;
	}


	/**
	 * returns the lowest card of the passed param suit
	 * 
	 * @param suit The suit that we want the lowest card of.
	 * @return The card that is the lowest of the param's suit in the player's hand
	 */
	public Card getLowCardOfSuit(Card.SUIT suit) {
		Card low = null;

		for (Card c: hand) {
			if (c.getSuit() == suit && (low==null || c.getValue() < low.getValue())) {
				low = c;
			}
		}

		return low;
	}


	/*
	 * Adds an integer as paramater to points
	 */
	public void addPoints(int points) {
		this.points += points;
	}

	/*
	 * takes a Card element and removes it from the hand. Returns the same element
	 */
	public Card playAndRemoveCard(Card card) {
		for (int i = 0; i < hand.size(); i++) {
			if (card.equals(hand.get(i))) {
				return hand.remove(i);
			}
		}
		return null;
	}

	/**
	 * returns the cards of the parameter suit and returns them as an arraylist
	 * 
	 * @param suit The suit to be searched for
	 * @return The list of the suits.
	 */
	public ArrayList<Card> getSuit(Card.SUIT suit) {

		ArrayList<Card> list = new ArrayList<Card>();

		for (Card c : hand) {
			if (c.getSuit() == suit) {
				list.add(c);
			}
		}
		return list;
	}
}
