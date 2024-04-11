package com.cs506group12.backend.models;

import java.util.*;

import com.cs506group12.backend.models.Card.SUIT;

public class EuchreGame {
    private static ArrayList<Card> deck;
    private List<Card>[] playerHands;
    private int cardsLeft;

    private ArrayList<Player> players;
    private boolean areTurnsTimed;
   // private String trump; // not currently used, create a card witb CARD.SUIT.suit , 0  to establish suit 
    private int dealer = 0; // position of dealer
    private int leadingPlayer = 1;  // player who plays first card of truck
    private int teamOneScore; // do turns until one of the team scores is over threshhold
    private int teamTwoScore;
    private int pointsThreshold = 10;
    private Card faceUpCard;  // used to helpo establish trump
    private Card trumpSuitCard; // for comparison
    private int numPlayingCards = 4;  // for if we implement 3 player
	private int teamThatWonTrick = 0;
    private int teamThatWonTurn;
	public int attackingTeam;  // team who establishes trump
    public int[] teamOverallScores = {0,0};
	private int[] numTricks = {0,0};
    private ArrayList<Card> playedCards =  new ArrayList<Card>();
    private boolean isSoloPlayer = false;  // if a player goes alone (3 players) - currently unimplemented
    private int soloPlayerIndex;
    // GameSession session; 


    public static ArrayList<Integer> ranks = new ArrayList<>();

    public EuchreGame() {
    }


    /**
     * The function from which the game is run.  Game will run until one team has 10 points
     */
    public void euchreGameLoop(){
        this.deck = new ArrayList<>();
        initializeDeck();
        dealCards();
        players = new ArrayList<>();

        for(int i = 0;i < 4; i ++) {
            //players.addAll(session.getAllPlayers().getID());
            players.add(new Player("Name" + i));
        }
        
        cardsLeft = deck.size();        

        Card.SUIT trump = null;

        // game loop - exits once a team wins 
        while (teamOneScore < pointsThreshold && teamTwoScore < pointsThreshold) {
            trump = establishTrump();
            for (int i = 0; i < 5; i ++){                
                handleTrick();            
            }

            teamThatWonTurn = determineTurnWinner();
            teamOverallScores[teamThatWonTurn] += handlePoints(teamThatWonTurn); // updates overall point total

            Collections.shuffle(deck);
            dealCards();  // resets
            dealer = (dealer + 1) % 4;
            //leadingPlayer = (dealer + 1) % 4; // goes first in first round  MAYBE REMOVE 
            
        }
    }

    /**
     * Handles one trick where each player plays one card.
     */
    public void handleTrick() {
        Card.SUIT leadingSuit = null; // Leading suit of the trick
        Card currentWinningCard = null; // Highest card played in the trick
        int currentWinner = -1; // Index of the player who played the current winning card

        // Loop through each player playing one card
        for (int i = 0; i < 4; i++) {
            Player currentPlayer = players.get((leadingPlayer + i) % 4); // Get current player
            Card playedCard = currentPlayer.playAndRemoveCard(null); // Player plays a card
            playedCards.add(playedCard); // Add the played card to the list

            if (i == 0) {
                leadingSuit = playedCard.getSuit(); // Set the leading suit for this trick
                currentWinningCard = playedCard; // Set the current winning card initially
                currentWinner = leadingPlayer; // The leading player is currently the winner
            } else if (playedCard.greater(currentWinningCard, trumpSuitCard, leadingSuit)) {
                // If the played card is greater than the current winning card, update the winner and winning card
                currentWinner = (leadingPlayer + i) % 4;
                currentWinningCard = playedCard;
            }
        }

        // Update the team that won the trick and the leading player for the next trick
        teamThatWonTrick = currentWinner % 2;
        leadingPlayer = currentWinner;
        numTricks[teamThatWonTrick]++; // Increment the number of tricks won by the winning team
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
    public int handlePoints(int winningTeam){
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

		for (int i = 0; i < cards.size(); i++){  // starts at leading player
			if (cards.get(i).value(trumpSuitCard, cards.get(i).getSuit()) > max){  // temporary - need to assign trump suit
				maxIndex = i;
				max = cards.get(i).value(trumpSuitCard, cards.get(i).getSuit());
			}
		}
		return maxIndex;
		
	}

    /**
     * presents all players with option to chose face up card as trumop, if a player chooses, dealer swaps out a card
     * else, dealer choses trump
     * @return Card.SUIT of the trump suit
     */
    public Card.SUIT establishTrump(){
        Card.SUIT trump = null;
        for (int i = 1; i < 4; i++){
            // controller present player ((dealer+i)%4) with option to choose trumo
            Player present = players.get((dealer+i)%4);


            //iterate through session.clients()
            //client.sendMessage("chooseTrump", "Choose a trump. Or pass to pass")
            //
            if (present.chooseTrump() != null) {
                trump = present.chooseTrump();
                present.playAndRemoveCard(null);
            }
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

    public int getDealer() {
        return dealer;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public int getLeadingPlayer() {
        return leadingPlayer;
    }

    public void setLeadingPlayer(int leadingPlayer) {
        this.leadingPlayer = leadingPlayer;
    }

    public void setFaceUpCard(Card faceUpCard) {
        this.faceUpCard = faceUpCard;
    }

    public int getTeamThatWonTrick() {
        return teamThatWonTrick;
    }

    public void setTeamThatWonTrick(int teamThatWonTrick) {
        this.teamThatWonTrick = teamThatWonTrick;
    }

    public int getTeamThatWonTurn() {
        return teamThatWonTurn;
    }

    public void setTeamThatWonTurn(int teamThatWonTurn) {
        this.teamThatWonTurn = teamThatWonTurn;
    }

    public int getAttackingTeam() {
        return attackingTeam;
    }

    public void setAttackingTeam(int attackingTeam) {
        this.attackingTeam = attackingTeam;
    }

    public int[] getTeamOverallScores() {
        return teamOverallScores;
    }

    public void setTeamOverallScores(int[] teamOverallScores) {
        this.teamOverallScores = teamOverallScores;
    }

    public int[] getNumTricks() {
        return numTricks;
    }

    public void setNumTricks(int[] numTricks) {
        this.numTricks = numTricks;
    }

    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(ArrayList<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public boolean isSoloPlayer() {
        return isSoloPlayer;
    }

    public void setSoloPlayer(boolean isSoloPlayer) {
        this.isSoloPlayer = isSoloPlayer;
    }

    public void setTrumpSuitCard(Card card) {
        this.trumpSuitCard = card;
    }
    public Card getTrumpSuitCard() {
        return trumpSuitCard;
    }

}
