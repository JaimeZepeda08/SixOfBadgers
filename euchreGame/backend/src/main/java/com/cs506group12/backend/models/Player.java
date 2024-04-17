package com.cs506group12.backend.models;

import java.util.*;
import org.springframework.web.socket.WebSocketSession;

public class Player extends Client {
	public ArrayList<Card> hand;
	public String userName;
	private int points;
	// maybe a team function - i wpuld prefer for teammates to be 0 and 1 in array
	// but maybe no possible?
	// do i need login functions?

	/**
	 * Constructor class for the Player class.
	 */
	public Player(WebSocketSession session) {
		super(session);
		this.hand = new ArrayList<>();
		this.points = 0;
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

		for (Card c : hand) {
			if (c.getSuit() == suit && (high == null || c.getValue() > high.getValue())) {
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

		for (Card c : hand) {
			if (c.getSuit() == suit && (low == null || c.getValue() < low.getValue())) {
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

	/**
	 * Checks if the players hands has tghe param suit
	 * 
	 * @param suit The suit that will be checked to see if there is a player hand
	 * @return
	 */
	public boolean hasSuitCard(Card.SUIT suit) {
		for (Card c : hand) {
			if (c.getSuit() == suit) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to get the playable cards for the round
	 * 
	 * @param leadingSuit The leading suit of the trick
	 * @param trumpSuit   The trump suit of the tricm
	 * @return Returns an array of boolean for the player cards.
	 */
	public boolean[] getPlayableCards(Card.SUIT leadingSuit, Card.SUIT trumpSuit) {
		boolean[] toReturn = new boolean[hand.size()];
		boolean hasAtLeastOneOfSuit = false;
		Card.SUIT sameColorAsTrump = Card.twinColorSuit(trumpSuit);
		Card c;

		for (int i = 0; i < hand.size(); i++) {
			c = hand.get(i);

			// suit is the leading suit and not the same color as trump and a jack
			// OR leading is trump and card is same color as the trump and a jack
			if ((c.getSuit() == leadingSuit && !(c.getSuit() == sameColorAsTrump && c.getValue() == 11)) ||
					(leadingSuit == trumpSuit && c.getSuit() == sameColorAsTrump && c.getValue() == 11)) {
				hasAtLeastOneOfSuit = true;
				toReturn[i] = true;
			} else {
				toReturn[i] = false;
			}
		}

		if (!hasAtLeastOneOfSuit) {
			for (int i = 0; i < hand.size(); i++) {
				toReturn[i] = true;
			}
		}

		return toReturn;
	}

	/**
	 * Class to choose the trump for the round
	 * TODO: implement with frotnend
	 * 
	 * @return Card.SUIT enum of the chosen suit, null if none picked
	 */
	public Card.SUIT chooseTrump() {
		Card.SUIT trump = null;
		for (Card c : this.hand) {
			trump = c.getSuit();

		}

		return null;
	}

	/**
	 * used to get players hand as a json
	 * 
	 * @return a json formatted string of cards
	 */
	public String handAsJson() {
		String finalJson = "[";
		for (int i = 0; i < hand.size(); i++) {
			finalJson += hand.get(i).toString() + ",";
		}
		// here if one to many ,
		finalJson = finalJson.substring(0, finalJson.length() - 1);
		finalJson += "]";

		return finalJson;
	}

	/**
	 * 
	 * @param playableHand the cards available for user to play
	 * @return a json formatted string of cards
	 */
	public String handAsJson(ArrayList<Card> playableHand) {
		String finalJson = "{\n\t\"hand\": [";
		for (int i = 0; i < playableHand.size(); i++) {
			finalJson += playableHand.get(i).cardToJson() + ",";
		}
		// here if one to many ,
		finalJson += "\t]\n}";

		return finalJson;
	}

	/**
	 * 
	 * @return
	 */
	public String playerAsJson() {
		return "{\n\t\"player_name\": " + userName + "\"\n}";
	}

	public void sendPlayerCards() {
		String handString = "[";
		for (Card card : hand) {
			handString += card.toString() + ",";
		}
		handString = handString.substring(0, handString.length() - 1);
		handString += "]";
		System.out.println(getClientId() + ": " + handString); // debug
		sendMessage("cards", handString);
	}

}
