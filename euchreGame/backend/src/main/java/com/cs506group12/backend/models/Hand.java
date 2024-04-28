package com.cs506group12.backend.models;

import java.util.*;

/**
 * The Hand class wraps an ArrayList of Cards. It contains a number of methods
 * to check the contents of the hand. The hand should not be modified directly,
 * but instead modified through the gameState class, so that updates to the
 * hand will be captured by the GameStateObserver and sent to the clients.
 */
public class Hand {
    
    private ArrayList<Card> hand;

	public Hand(){
		this.hand = new ArrayList<Card>();
	}

    /**
	 * Method to get the playable cards for the round
	 * 
	 * @param leadingSuit The leading suit of the trick
	 * @param trumpSuit   The trump suit of the trick
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

    public String toJSON() {
		String handString = "[";
		for (Card card : hand) {
			handString += card.cardToJSON() + ",";
		}
		handString = handString.substring(0, handString.length() - 1);
		handString += "]";
		return handString;
	}

    public Card getCard(int i){
        return hand.get(i);
    }

    /*
     * Removes a card with the given index from the hand. Should only be called by
     * GameState class.
     */
    public Card removeCard(int i){
        Card c = hand.get(i);
        hand.remove(i);
        return c;
    }

	public void removeCard(Card c){
		hand.remove(c);
	}

    /*
     * Adds a card to the hand. Should only be called by GameState class.
     */
    public void addCard(Card c){
        hand.add(c);
    }

    public int getSize(){
        return hand.size();
    }

	public void clearHand(){
		this.hand.clear();
	}

    public Iterator<Card> iterator(){
        return hand.iterator();
    }

    public String toSqlString(){
    
        String handString = "";
        Card card;
        for(int i=0; i<5; i++){
            card = hand.get(i);
            if(card != null){
                handString += card.toSqlString();
            }
            handString += ",";
        }
        handString = handString.substring(0,handString.length() - 1); //trim last comma

        return handString;
    
    }
}
