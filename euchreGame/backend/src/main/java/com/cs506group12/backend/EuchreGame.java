package com.cs506group12.backend;

import java.util.*;
import com.cs506group12.backend.models.*;

public class EuchreGame {
    private ArrayList<Card> deck;
    private List<Card>[] playerHands;
    private int cardsLeft;

    public EuchreGame() {
        initializeDeck();
        shuffleDeck();
        dealCards();
        cardsLeft = deck.size();
<<<<<<< HEAD:euchreGame/src/euchreGame/EuchreGame.java

=======
>>>>>>> 51f249ce09b871b29df7b7a6d8a8f5049636cc69:euchreGame/backend/src/main/java/com/cs506group12/backend/EuchreGame.java
    }

    /**
     * Initializes the original deck of 24 cards
     */
    private void initializeDeck() {
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Shuffles the deck of cards to give to players
     */
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Deals out 4 cards to each person
     */
    private void dealCards() {
        playerHands = new List[4];
        for (int i = 0; i < 4; i++) {
            playerHands[i] = new ArrayList<>();
        }

        Iterator<Card> iterator = deck.iterator();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                playerHands[j].add(iterator.next());
            }
        }

        cardsLeft = deck.size() - 16;
    }

    /*
     * Getter method for playerHands
     */
    public List<Card>[] getPlayerHands() {
        return playerHands;
    }

    /*
     * Getter method for cards left in the deck
     */
    public int getCardsLeft() {
        return cardsLeft;
    }


    }
