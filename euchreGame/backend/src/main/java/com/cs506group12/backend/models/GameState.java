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
      * @param gameUUID The UUID of the game that players use to join
      * @param playerHands Array of player hands
      * @param playerNames Array of player names - used to match to clients
      * @param dealerPosition Current position of the dealer
      * @param leadingPlayerPosition Position of the leading player or -1 if not playing a trick
      * @param teamOneScore Score of team 1
      * @param teamTwoScore Score of team 2
      * @param teamOneTricks Tricks won by team 1
      * @param teamTwoTricks Tricks won by team 2
      * @param trumpSuit Current trump suit or null if not yet called
      * @param attackingTeam Team that called trump or -1 if not yet called
      * @param playerGoingAlone Index of the player that is going alone, or -1 if none
      */
    public GameState(String gameUUID, Hand[] playerHands, String[] playerNames,  int dealerPosition, int leadingPlayerPosition,
        int teamOneScore, int teamTwoScore, int teamOneTricks, int teamTwoTricks, Card.SUIT trumpSuit, int attackingTeam, int playerGoingAlone){
        this.observer = new GameStateObserver();
        
        this.gameUUID = gameUUID;
        this.playerHands = playerHands;
        this.players = new ArrayList<Player>();
        for(int i=0; i<4; i++){
            this.players.add(new EuchrePlayer(playerNames[i], i));
        }

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

        //TODO make sure we can only save inbetween tricks or at the start of the round.
        if(teamOneTricks > 0 || teamTwoTricks > 0){
            this.currentPhase = PHASE.PLAYTRICK;
        }else{
            this.currentPhase = PHASE.STARTROUND;
        }

        observer.updateEntireState(dealerPosition,leadingPlayerPosition,0,
            faceUpCard,trumpSuit,leadingSuit,playedCards,playerHands,score,tricks,attackingTeam);
    }

    /**
     * Creates player objects based on connected clients from loaded state
     * There is expected to be one client for each human player in the saved state
     * @param clients
     */
    public void loadConnectedPlayersRestore(ArrayList<Client> clients){
        //Add players to the game, starting with connected human players
        Client client;
        Player newPlayer;
        String clientID;
        for (int i = 0; i < clients.size(); i++) {
            client = clients.get(i);
            clientID = client.getClientId();
            for(int j=0; j < players.size(); j++){
                if(clientID.equals(players.get(i).getName())){
                    newPlayer = new EuchrePlayer(client.getClientId(), i + 1);
                    players.remove(i);
                    this.addPlayer(new HumanPlayerDecorator(newPlayer, client), i + 1); 
                }
            }
        }
        //loop up from number of current clients to 4 to fill in the remaining slots with bots
        for (int i = clients.size(); i < 4; i++){
            newPlayer = new EuchrePlayer(null, i + 1); //don't need to set name for bots
            players.remove(i);
            this.addPlayer(new AIPlayerDecorator(newPlayer), i + 1);
        }
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
     * Gets a representation of a player's hand in for storing in SQL.
     * @param player index of the player whose hand should be SQLd (0<=i<=3)
     * @return A string prepared to be stored in SQL
     */
    public String getHandSQL(int player){
        return playerHands[player].toSqlString();
    }

    /**
     * Gets the position of the dealer
     * @return Position of the dealer (1 to 4)
     */
    public int getDealerPosition(){
        if(dealerIndex == -1){
            return -1;
        }else{
            return this.dealerIndex + 1;
        }
    }

    /**
     * Gets the position of the leading player
     * @return position of the leading player (1 to 4)
     */
    public int getLeadingPlayerPosition(){
        if(this.leadingPlayerIndex == -1){
            return -1;
        }else{
            return this.leadingPlayerIndex + 1;
        }
    }

    /**
     * Gets the current trump suit, or null if no suit set.
     * @return the current trump suit
     */
    public Card.SUIT getTrump(){
        return this.trumpSuit;
    }

    /**
     * Gets the number of the team that called trump, or -1 if none
     * @return the number of the team that called trump, or -1 if none
     */
    public int getAttackingTeam(){
        return this.attackingTeam;
    }

    /**
     * Gets the position of the player going alone, or -1 if none
     * @return the position of the player going alone, or -1 if none
     */
    public int getPlayerGoingAlone(){
        if(this.playerGoingAlone == -1){
            return -1;
        }else{
            return this.playerGoingAlone + 1;
        }
    }

    /**
     * Getter for played cards
     * @return an ArrayList of cards that have been played
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    /**
     * Getter for the faceup card
     * @return the faceup card
     */
    public Card getFaceUpCard(){
        return faceUpCard;
    }

    /**
     * Getter for the current phase
     * @return the current game phase
     */
    public PHASE getCurrentPhase(){
        return this.currentPhase;
    }

    /**
     * Call on resolution of any phase, even if returning to the same phase.
     * Executes batched update for GameObserver
     * @param phase phase that the game is transitioning to.
     */
    public void setPhase(PHASE phase){
        observer.sendUpdate(this.currentPhase, phase, this.players);
        this.currentPhase = phase;

        //Once updated, if we're moving to STARTROUND initialize round starting values.
        if(phase == PHASE.STARTROUND){
            this.tricks[0]=0;
            observer.updateTricks(1, 0);
            this.tricks[1]=0;
            observer.updateTricks(2, 0);
            this.attackingTeam = -1;
            observer.updateAttackingTeam(-1);
            if(this.playerGoingAlone != -1){
                this.playerHands[playerGoingAlone].clearHand();
                observer.updateHand(playerGoingAlone, this.playerHands[playerGoingAlone]);
                this.playerGoingAlone = -1;
            }
            this.faceUpCard = null;
            observer.updateFaceUpCard(null);
            this.leadingPlayerIndex = -1;
            observer.updatePositions(this.dealerIndex + 1, this.leadingPlayerIndex);
            this.leadingSuit = null;
            observer.updateLeadingSuit(null);
            this.playedCards.clear();
            observer.updatePlayedCards(this.playedCards);
            this.trumpSuit = null;
            observer.updateTrump(null);
        }

    }

    /**
     * getter for the leading suit
     * @return the leading suit of the trick, or null if none
     */
    public Card.SUIT getLeadingSuit(){
        return this.leadingSuit;
    }

    /**
     * getter for the highest played card this trick
     * @return the highest played card this trick
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
     * Add a player at a given position
     * @param player Player to add
     * @param position Player's position (1 to 4)
     */
    public void addPlayer(Player player, int position){
        this.players.add((position - 1) % 4, player);
    }

    /**
     * Sets the trump
     * @param suit Suit to set as trump
     */
    public void setTrump(Card.SUIT suit){
        this.trumpSuit = suit;
        observer.updateTrump(suit);
    }

    /**
     * Sets the active player
     * @param position Position of the active player (1 to 4)
     */
    public void setActivePlayer(int position){
        this.activePlayerIndex = (position - 1) % 4;
        observer.updateActivePlayer(position);
    }

    /**
     * getter for the active player
     * @return The active player
     */
    public Player getActivePlayer(){
        if(activePlayerIndex == -1){
            return null;
        }
        return players.get(activePlayerIndex);
    }

    /**
     * Gets a player based on a specific position
     * @param position position of the player to get (1 to 4)
     * @return The player at the given position
     */
    public Player getPlayer(int position){
        return this.players.get((position - 1) % 4 );
    }

    /**
     * Removes a card from a player's hand
     * @param position Position of the player to remove the card from
     * @param c The card to remove
     */
    public void removeCard(int position, Card c){
        Hand h = playerHands[(position - 1) % 4];
        h.removeCard(c);
        observer.updateHand((position - 1) % 4, h);
    }

    /**
     * Adds a card to the list of played cards. removes the given card
     * from the active player's hand
     * @param c card to play
     */
    public void addPlayedCard(Card c){
        playedCards.add(c);
        Hand hand = playerHands[activePlayerIndex];
        hand.removeCard(c);
        observer.updatePlayedCards(playedCards);
        observer.updateHand(activePlayerIndex, hand);
    }

    /**
     * sets the leading player
     * @param position position of the leading player (1 to 4)
     */
    public void setLeadingPlayer(int position){
        this.leadingPlayerIndex = (position - 1) % 4;
        observer.updatePositions(this.dealerIndex + 1, position);
    }

    /**
     * Adds a trick won by the player at the given position
     * @param position the position of the player who won the trick (1 to 4)
     */
    public void addTrick(int position){
        int winningTeam = (position - 1) % 2;
        tricks[winningTeam] = tricks[winningTeam] + 1;
        observer.updateTricks(winningTeam + 1, tricks[winningTeam]);
    }

    /**
     * Clear the list of played cards
     */
    public void clearPlayedCards(){
        this.playedCards.clear();
        observer.updatePlayedCards(playedCards);
    }

    /**
     * Adds a number of points to a team's score
     * @param team Team to add the points to (1 or 2)
     * @param pointsScored The number of points scored
     */
    public void addScore(int team, int pointsScored){
        int teamIndex = (team - 1) % 2;
        score[teamIndex] = score[teamIndex] + pointsScored;
        observer.updateScore(teamIndex + 1, score[teamIndex]);
    }

    /**
     * Sets the position of the dealer
     * @param position The position of the dealer (1 to 4)
     */
    public void setDealerPosition(int position){
        this.dealerIndex = (position - 1) % 4;
        observer.updatePositions(position, leadingPlayerIndex + 1);
    }

    /**
     * Clears the given player's hand
     * @param position The position of the player to clear hand (1 to 4)
     */
    public void clearHand(int position){
        Hand hand = this.playerHands[(position - 1) % 4];
        hand.clearHand();
        observer.updateHand((position - 1) % 4, hand);
    }

    /**
     * Sets the position of the player going alone
     * @param position position of the player going alone, or -1 if playing with full roster
     */
    public void setPlayerGoingAlone(int position){
        if(position == -1){
            this.playerGoingAlone = -1;
        }else{
            this.playerGoingAlone = (position - 1) % 4;
        }
    }

    /**
     * Sets the leading suit for a trick
     * @param suit The suit of the led card
     */
    public void setLeadingSuit(Card.SUIT suit){
        this.leadingSuit = suit;
        observer.updateLeadingSuit(suit);
    }

    /**
     * Sets the attacking team based on the position of the calling player
     * @param playerPosition The position of the player who called trump (1 to 4)
     */
    public void setAttackingTeam(int playerPosition){
        this.attackingTeam = ((playerPosition - 1) % 2) + 1;
    }
}
