package com.cs506group12.backend.models;

import java.sql.Timestamp;
import com.cs506group12.backend.interfaces.*;
import java.util.*;

/**
 * Class representing a datastructure for storing the state of the game. Implements
 * an observer architecture. When variables that connected clients need to
 * be aware of change, the observer is updated with the changes. When the state
 * is updated the observer sends a batched update to the connected clients.
 * In general, GameState should not contain game logic, only what is necessary
 * to return the correct values. Any game logic should be implemented in EuchreGame.
 * 
 * @author Eric Knepper
 */
public class GameState {

    public enum PHASE{
        
        STARTROUND,
        PICKTRUMP1,     //Pick trump by telling the dealer to pick up the face card or passing.
                        //If dealer pick up, -> REPLACECARD, else when all have passed, ->PICKTRUMP2
        PICKTRUMP2,     //Pick trump by calling a suit other than the face up card. When trump is selected,
                        // -> PLAYTRICK. If all others have passed, dealer is forced to choose in final round.
        REPLACECARD,    //Dealer replaces a card from their hand, then -> PLAYTRICK
        PLAYTRICK,      //Play cards, following leading suit. When 4 cards are played -> SCORETRICK
        SCORETRICK,     //Determine winner of trick. If numTricks < 5, -> PLAYTRICK, else
        SCOREROUND,     //Determine the score of the round. If a team's score > 10 -> ENDGAME, else -> STARTROUND
        ENDGAME         //Determine winner of the game, store game record to DB. Prompt to play again?
    }

    private GameStateObserver observer;

    private String gameUUID; //unique identifier used to connect to the game
    private ArrayList<Card> deck;
    private Hand[] playerHands;
    private String[] playerNames;
    private ArrayList<Player> players;
    private int activePlayerIndex;      //Index of the active player
    private int dealerIndex;            //Index of of dealer
    private int leadingPlayerIndex;     //Index of player who plays first card of trick
    private int[] tricks;
    private int[] score;
    private Card faceUpCard;  // used to help establish trump
    private Card.SUIT trumpSuit; // Current suit of trump
	private int attackingTeam;  // team who established trump
    private ArrayList<Card> playedCards;
    private int playerGoingAlone; //index of the player who is going alone. -1 if full roster is playing.
    private Timestamp gameStartTime;

    //Do not set the phase directly - whenever phase changes observer.sendUpdate() needs to be called.
    private PHASE currentPhase;

    private Card.SUIT leadingSuit;

    /**
     * Initializes GameState to starting state. This includes creating
     * and shuffling the deck.
     */
    public GameState(){
        this.observer = new GameStateObserver();
        this.playerHands = new Hand[4];
        for(int i=0; i<4; i++){
            this.playerHands[i] = new Hand();
        }
        playerNames = new String[4];
        this.players = new ArrayList<Player>();
        this.dealerIndex = -1;
        this.leadingPlayerIndex = -1;
        this.score = new int[]{0,0};
        this.tricks = new int[]{0,0};
        this.faceUpCard = null;
        this.trumpSuit = null;
        this.attackingTeam = -1;
        this.playedCards = new ArrayList<Card>();
        this.playerGoingAlone = -1;
        this.gameStartTime = new Timestamp(System.currentTimeMillis());

        this.activePlayerIndex = -1;

        this.deck = new ArrayList<Card>();
        this.initializeDeck();

        observer.updateEntireState(dealerIndex + 1, leadingPlayerIndex + 1,activePlayerIndex + 1,
            faceUpCard,trumpSuit,leadingSuit,playedCards,playerHands,score,tricks,attackingTeam);
    }

     /**
      * Restores a game state from the SQL database. Don't use to make modifications to a game in progress.
      * TODO: select correct game phase
      * TODO: Load players based on names
      * @param gameUUID
      * @param playerHands
      * @param playerNames
      * @param dealerPosition
      * @param leadingPlayerPosition
      * @param teamOneScore
      * @param teamTwoScore
      * @param teamOneTricks
      * @param teamTwoTricks
      * @param trumpSuit
      * @param attackingTeam
      * @param playerGoingAlone
      */
    public GameState(String gameUUID, Hand[] playerHands, String[] playerNames,  int dealerPosition, int leadingPlayerPosition,
        int teamOneScore, int teamTwoScore, int teamOneTricks, int teamTwoTricks, Card.SUIT trumpSuit, int attackingTeam, int playerGoingAlone){
        this.observer = new GameStateObserver();
        
        this.gameUUID = gameUUID;
        this.playerHands = playerHands;
        this.playerNames = playerNames;
        this.dealerIndex = (dealerPosition - 1) % 4;
        this.leadingPlayerIndex = (leadingPlayerPosition - 1) % 4;
        this.activePlayerIndex = (leadingPlayerPosition - 1) % 4;
        this.score = new int[]{teamOneScore, teamTwoScore};
        this.tricks = new int[]{teamOneTricks, teamTwoTricks};
        this.faceUpCard = null;
        this.trumpSuit = trumpSuit;
        this.attackingTeam = attackingTeam;
        this.playedCards = new ArrayList<Card>();
        this.playerGoingAlone = playerGoingAlone;

        this.deck = new ArrayList<Card>();
        this.initializeDeck();

        //TODO
        if(teamOneTricks > 0 || teamTwoTricks > 0){
            this.currentPhase = PHASE.PLAYTRICK;
        }else{
            this.currentPhase = PHASE.STARTROUND;
        }

        observer.updateEntireState(dealerPosition,leadingPlayerPosition,0,
            faceUpCard,trumpSuit,leadingSuit,playedCards,playerHands,score,tricks,attackingTeam);
    }

    /**
     * Initialize the deck of cards by creating card objects and then shuffling them
     */
    private void initializeDeck(){
        
        for (int x = 9; x < 15; x++) {
            deck.add(new Card(Card.SUIT.CLUBS, x));
            deck.add(new Card(Card.SUIT.DIAMONDS, x));
            deck.add(new Card(Card.SUIT.HEARTS, x));
            deck.add(new Card(Card.SUIT.SPADES, x));
        }

        Collections.shuffle(deck);
    }

    /**
     * Deals out 5 cards to each person
     */
    public void dealCards() {
        
        Iterator<Card> iterator = deck.iterator();
        //Deal 5 cards to each of 4 players using deck iterator.
        for (int i = 0; i < 4; i++) {
            Hand hand = playerHands[i];
            for (int j = 0; j < 5; j++) {
                Card nextCard = iterator.next();
                hand.addCard(nextCard);
            }
            observer.updateHand(i, hand);
        }

        // set face up card to next card in deck
        faceUpCard = iterator.next();
        observer.updateFaceUpCard(faceUpCard);

        this.setPhase(PHASE.PICKTRUMP1);

    }

    public void setUUID(String UUID){
        this.gameUUID = UUID;
    }
    
    public String getUUID(){
        return this.gameUUID;
    }

    public String[] getPlayerNames(){
        return this.playerNames;
    }

    public Hand getHand(int playerNumber){
        return playerHands[(playerNumber - 1) % 4];
    }

    public Timestamp getStartTime(){
        return this.gameStartTime;
    }

    /**
     * 
     * @param team
     * @return
     */
    public int getTeamScore(int team){
        return score[(team - 1) % 2];
    }

    /**
     * 
     * @param team
     * @return
     */
    public int getTeamTricks(int team){
        return tricks[(team - 1) % 2];
    }

    /**
     * 
     * @param player
     * @return
     */
    public String getHandSQL(int player){
        return playerHands[player].toSqlString();
    }

    /**
     * 
     * @return
     */
    public int getDealerPosition(){
        if(dealerIndex == -1){
            return -1;
        }else{
            return this.dealerIndex + 1;
        }
    }

    /**
     * 
     * @return
     */
    public int getLeadingPlayerPosition(){
        return this.leadingPlayerIndex + 1;
    }

    /**
     * 
     * @return
     */
    public Card.SUIT getTrump(){
        return this.trumpSuit;
    }

    /**
     * 
     * @return
     */
    public int getAttackingTeam(){
        return this.attackingTeam;
    }

    /**
     * 
     * @return
     */
    public int getPlayerGoingAlone(){
        return this.playerGoingAlone;
    }

    /**
     * 
     * @return
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    /**
     * 
     * @return
     */
    public Card getFaceUpCard(){
        return faceUpCard;
    }

    /**
     * 
     * @return
     */
    public PHASE getCurrentPhase(){
        return this.currentPhase;
    }

    /**
     * Call on resolution of any phase, even if returning to the same phase.
     * Executes batched update for GameObserver
     * @param phase
     */
    public void setPhase(PHASE phase){
        observer.sendUpdate(this.currentPhase, phase, this.players);
        this.currentPhase = phase;

    }

    /**
     * 
     * @return
     */
    public Card.SUIT getLeadingSuit(){
        return this.leadingSuit;
    }

    /**
     * 
     * @return
     */
    public Card highestPlayedCard(){
        int max = Integer.MIN_VALUE;
        Card c;
        Card highestCard = null;
        int currentValue;

        for (int i = 0; i < playedCards.size(); i++){
            c = playedCards.get(i);
            currentValue = playedCards.get(i).value(trumpSuit, leadingSuit);
            if (currentValue > max){
                highestCard = c;
                max = currentValue;
            }
        }
        return highestCard;
        
    }

    /**
     * 
     * @param player
     * @param position
     */
    public void addPlayer(Player player, int position){
        this.players.add((position - 1) % 4, player);
    }

    /**
     * 
     * @param suit
     */
    public void setTrump(Card.SUIT suit){
        this.trumpSuit = suit;
        observer.updateTrump(suit);
    }

    /**
     * 
     * @param position
     */
    public void setActivePlayer(int position){
        this.activePlayerIndex = (position - 1) % 4;
        observer.updateActivePlayer(position);
    }

    /**
     * 
     * @return
     */
    public Player getActivePlayer(){
        if(activePlayerIndex == -1){
            return null;
        }
        return players.get(activePlayerIndex);
    }

    /**
     * 
     * @param position
     * @return
     */
    public Player getPlayer(int position){
        return this.players.get((position - 1) % 4 );
    }

    /**
     * 
     * @param position
     * @param c
     */
    public void removeCard(int position, Card c){
        Hand h = playerHands[(position - 1) % 4];
        h.removeCard(c);
        observer.updateHand((position - 1) % 4, h);
    }

    /**
     * 
     * @param c
     */
    public void addPlayedCard(Card c){
        playedCards.add(c);
        Hand hand = playerHands[activePlayerIndex];
        hand.removeCard(c);
        observer.updatePlayedCards(playedCards);
        observer.updateHand(activePlayerIndex, hand);
    }

    /**
     * 
     * @param position
     */
    public void setLeadingPlayer(int position){
        this.leadingPlayerIndex = (position - 1) % 4;
        observer.updatePositions(this.dealerIndex + 1, position);
    }

    /**
     * 
     * @param position
     */
    public void addTrick(int position){
        int winningTeam = (position - 1) % 2;
        tricks[winningTeam] = tricks[winningTeam] + 1;
        observer.updateTricks(winningTeam + 1, tricks[winningTeam]);
    }

    /**
     * 
     */
    public void clearPlayedCards(){
        this.playedCards.clear();
        observer.updatePlayedCards(playedCards);
    }

    /**
     * 
     * @param team
     * @param pointsScored
     */
    public void addScore(int team, int pointsScored){
        int teamIndex = (team - 1) % 2;
        score[teamIndex] = score[teamIndex] + pointsScored;
        observer.updateScore(teamIndex + 1, score[teamIndex]);
    }

    /**
     * 
     * @param position
     */
    public void setDealerPosition(int position){
        this.dealerIndex = (position - 1) % 4;
        observer.updatePositions(position, leadingPlayerIndex + 1);
    }

    /**
     * 
     * @param position
     */
    public void clearHand(int position){
        Hand hand = this.playerHands[(position - 1) % 4];
        hand.clearHand();
        observer.updateHand((position - 1) % 4, hand);
    }

    /**
     * 
     * @param position
     */
    public void setPlayerGoingAlone(int position){
        if(position == -1){
            this.playerGoingAlone = -1;
        }else{
            this.playerGoingAlone = (position - 1) % 4;
        }
    }

    /**
     * 
     * @param suit
     */
    public void setLeadingSuit(Card.SUIT suit){
        this.leadingSuit = suit;
        observer.updateLeadingSuit(suit);
    }

    public void setAttackingTeam(int playerPosition){
        this.attackingTeam = ((playerPosition - 1) % 2) + 1;
    }
}
