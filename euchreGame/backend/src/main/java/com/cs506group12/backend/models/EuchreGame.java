package com.cs506group12.backend.models;

import java.util.*;

public class EuchreGame {
    private static ArrayList<Card> deck;
    private List<Card>[] playerHands;
    private int cardsLeft;

    private ArrayList<Player> players;
    private boolean areTurnsTimed;
    private boolean threePlayers = false; // currently unimplemented
    private Suit trump; // move to Turn
    private int turn; // = starts at dealer+1%4
    private int dealer; // position of dealer
    private int teamOneScore; // do turns until one of the team scores is over threshhold
    private int teamTwoScore;
    private int pointsThreshold = 10;
    private Card faceUpCard;

    public static ArrayList<String> suits = new ArrayList<String>();

    public static ArrayList<Integer> ranks = new ArrayList<>();

    public EuchreGame() {
        initializeDeck();
        shuffleDeck();
        dealCards();
        cardsLeft = deck.size();
        while (teamOneScore > pointsThreshold && teamTwoScore > pointsThreshold) {
            Turn turn = new Turn(players, areTurnsTimed, dealer);
            dealer = (dealer + 1) % 4;
            // need to reassign cards
        }
    }

    /**
     * Initializes the original deck of 24 cards
     */
    private static void initializeDeck() {
        suits.add("Clubs");
        suits.add("Diamonds");
        suits.add("Hearts");
        suits.add("Clover");

        for (int i = 0; i <= 5; i++) {
            if (i == 0) {
                ranks.add(14); // Ace
            } else if (i == 1) {
                ranks.add(13); // King
            } else if (i == 2) {
                ranks.add(12); // Queen
            } else if (i == 3) {
                ranks.add(11); // Jack
            } else if (i == 4) {
                ranks.add(10); // 10
            } else if (i == 5) {
                ranks.add(9); // 9
            }
        }

        deck = new ArrayList<>();
        for (String suit : suits) {
            for (Integer rank : ranks) {
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

        faceUpCard = iterator.next(); // sets face up card to first card left in deck (after deal)

        cardsLeft = deck.size() - 16;

        // sets it in players object.
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setHand((ArrayList<Card>) playerHands[i]);
        }
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

    /*
     * Getter method for face up card
     */
    public Card getFaceUpCard() {
        return faceUpCard;
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

    public int getPointsThreshold() {
        return pointsThreshold;
    }

    public void setPointsThreshold(int pointsThreshold) {
        this.pointsThreshold = pointsThreshold;
    }

}
