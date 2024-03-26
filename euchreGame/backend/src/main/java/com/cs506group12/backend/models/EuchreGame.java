package com.cs506group12.backend.models;

import java.util.*;

public class EuchreGame {
    private static ArrayList<Card> deck;
    private List<Card>[] playerHands;
    private int cardsLeft;

    private ArrayList<Player> players;
    private boolean areTurnsTimed;
    private boolean threePlayers = false; // currently unimplemented
    private String trump; // move to Turn
    private int turn; // = starts at dealer+1%4
    private int dealer; // position of dealer
    private int teamOneScore; // do turns until one of the team scores is over threshhold
    private int teamTwoScore;
    private int pointsThreshold = 10;
    private Card faceUpCard;


    public static ArrayList<Integer> ranks = new ArrayList<>();

    public EuchreGame() {
        initializeDeck();
        dealCards();
        cardsLeft = deck.size();
        while (teamOneScore > pointsThreshold && teamTwoScore > pointsThreshold) {
            Turn turn = new Turn(players, areTurnsTimed, dealer);
            dealer = (dealer + 1) % 4;
            // need to reassign cards
        }
    }

    /**
     * Initializes the original deck of 24 cards, and shuffles it.
     */
    private static void initializeDeck() {
        for(int x=9; x<15; x++){
			deck.add(new Card(Card.SUIT.CLUBS, x));
			deck.add(new Card(Card.SUIT.DIAMONDS, x));
			deck.add(new Card(Card.SUIT.HEARTS, x));
			deck.add(new Card(Card.SUIT.SPADES, x));
		}

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
