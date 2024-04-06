package com.cs506group12.backend.models;

import java.util.*;

public class EuchreGame {
    private static ArrayList<Card> deck;
    private List<Card>[] playerHands;
    private int cardsLeft;

    private ArrayList<Player> players;
    private boolean areTurnsTimed;
    private String trump;
    private int dealer; // position of dealer
    private int leadingPlayer;  // player who plays first card of truck
    private int teamOneScore; // do turns until one of the team scores is over threshhold
    private int teamTwoScore;
    private int pointsThreshold = 10;
    private Card faceUpCard;  // used to helpo establish trump
    private int numPlayingCards = 4;  // for if we implement 3 player
	private int teamThatWonTrick = 0;
    private int teamThatWonTurn;
	public int attackingTeam;  // team who establishes trump
    public int[] teamOverallScores = {0,0};
	private int[] numTricks = {0,0};
    private ArrayList<Card> playedCards =  new ArrayList<Card>();
    private boolean isSoloPlayer = false;  // if a player goes alone (3 players) - currently unimplemented
    private int soloPlayerIndex;


    public static ArrayList<Integer> ranks = new ArrayList<>();

    public EuchreGame() {
        initializeDeck();
        dealCards();
        establishTrump();
        cardsLeft = deck.size();


        // game loop - exits once a team wins 
        while (teamOneScore < pointsThreshold && teamTwoScore < pointsThreshold) {
            for (int i = 0; i < 5; i ++){  // for each trick - TODO terminate early if team wins 
                handleTrick();            
            }

            teamThatWonTurn = determineTurnWinner();
            teamOverallScores[teamThatWonTurn] += handlePoints(teamThatWonTurn); // updates overall point total

            Collections.shuffle(deck);
            dealCards();  // resets
            dealer = (dealer + 1) % 4;
            leadingPlayer = (dealer + 1) % 4; // goes first in first round
            establishTrump();


        }
    }

    /**
	 * all players play one card then scored
	 * @return the team number that won the trick
	 */
    private void handleTrick(){
        // TODO if player goes alone (3 players)

		for (int i = 0; i < numPlayingCards; i++){
			// call a controller and add result to arraylist of played cards then score - starting from leading player
			teamThatWonTrick = (leadingPlayer + score(playedCards)) % 2;  
            leadingPlayer = (leadingPlayer + score(playedCards) % 4); // player who won current trick starts of next trick
            numTricks[teamThatWonTrick]++; // UPDATES

		}
		playedCards.clear();
	}

    /**
     * looks for which team won more tricks
     * @return the index of the team that won the turn
     */
    private int determineTurnWinner(){
        if (numTricks[0] > numTricks[1])
            return 0;
        else {
            return 1;
        }
    }


    /**
     * After all tricks are won, assign points to winning team
     * @param winningTeam the team that won the turn
     * @return the number of points the winning team recieves
     */
    private int handlePoints(int winningTeam){
        if (winningTeam != attackingTeam){  // if defenders win
            return 2;
        }
        if (isSoloPlayer){ // if a player goes alone and wins  - only attacking team can go alone so should work
            if (numTricks[winningTeam] == 5){
                return 4; // solo player wins 5 tricks
            }
            else{
                return 1; // solo player wins 3 or 4 tricks
            }
        }
        if (numTricks[winningTeam] != 5 && attackingTeam == winningTeam){ // if attacking team wins 3 or 4
            return 1;
        }
        if (numTricks[winningTeam] == 5 && attackingTeam == winningTeam){ // if attacking team wins 5
            return 2;
        }

        
        numTricks[0] = 0;  // reset the numkber of tricks won because turn is over
        numTricks[1] = 0;
        return 1;
    }

    /**
     * Initializes the original deck of 24 cards, and shuffles it.
     */
    private static void initializeDeck() {
        for (int x = 9; x < 15; x++) {
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

    /**
     * Determines highest value card that wins trick
     * @param cards - the cards that have been played
     * @return index of the card with the highest value
     */
    public int score(ArrayList<Card> cards){
		int max = 0;
		int maxIndex = 0;

		//for (int i = 0; i < numPlayingCards; i++){  // starts at leading player
		//	if (cards.get(i).value(trump, cards.get(i).suit) > max){  // FIX 
		//		maxIndex = i;
		//		max = cards.get(i).value;
		//	}
		//}
		return maxIndex;
		
	}

    /**
     * presents all players with option to chose face up card as trumop, if a player chooses, dealer swaps out a card
     * else, dealer choses trump
     * @return a string representation of the trump suit
     */
    public String establishTrump(){
        for (int i = 1; i < 4; i++){
            // controller present player ((dealer+i)%4) with option to choose trumo
                // if yes, make dealer swap a card, remove card dealer selects from players.get(dealer)'s hand  - use same controller that plays card
            // else - give dealer option to choose - need a controller and buttons for that 
        }
        return trump;
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
