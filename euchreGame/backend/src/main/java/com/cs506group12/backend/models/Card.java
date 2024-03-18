package com.cs506group12.backend.models;

/**
 * Card Class
 * 
 * @author Kaldan
 *
 */
public class Card {
    private final String suit;
    private final int rank;

    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

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

}
