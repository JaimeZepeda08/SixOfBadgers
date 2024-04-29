package com.cs506group12.backend.models;

import java.util.*;
import com.cs506group12.backend.interfaces.*;
import com.cs506group12.backend.models.Card.SUIT;
import com.cs506group12.backend.models.GameState.PHASE;

public class EuchreGame extends GameSession {

    private GameState state;

    private int playersPassedTrump;

    /**
     * Constructor for a EuchreGame object
     *
     * @param host the client that started the game
     */
    public EuchreGame(Client host) {
        super(host);
    }

    /**
     * Constructor with state injection
     * @param host The client that started the game
     * @param state The game state data structure
     */
    public EuchreGame(Client host, GameState state){
        super(host);
        this.state = state;
    }


    /**
     * Overloads the startGame function of the GameSession Class. It is in charge of
     * initializing the game and setting up initial values
     */
    public boolean startGame() {
        if (super.startGame()) {
            //Initialize the game state
            state = new GameState();
            state.setUUID(super.gameId);

            //Add players to the game, starting with connected human players
            Client client;
            Player player;
            for (int i = 0; i < super.clients.size(); i++) {
                client = super.clients.get(i);
                player = new EuchrePlayer(client.getClientId(), i + 1);
                state.addPlayer(new HumanPlayerDecorator(player, client), i + 1);
            }
            //loop up from number of current clients to 4 to fill in the remaining slots with bots
            for (int i = super.clients.size(); i < 4; i++){
                player = new EuchrePlayer(null, i + 1); //don't need to set name for bots
                state.addPlayer(new AIPlayerDecorator(player), i + 1);
            }

            //Start the first round
            startRound();

            return true;
        }
        return false;
    }

    /**
     * Method for STARTROUND phase
     */
    public void startRound(){
        //Player to the left of previous dealer deals next
        int dealerPosition = state.getDealerPosition();

        //If no dealer is set yet (Game has just started)
        if(dealerPosition == -1){
            //set initial dealer at position 1 (host)
            state.setDealerPosition(1);
        }
        //Otherwise dealer position moves one player left
        else{
            state.setDealerPosition(state.getDealerPosition() + 1);
        }

        //Next round starts with player to the dealer's left
        state.setActivePlayer(state.getDealerPosition() + 1);

        state.setPhase(PHASE.STARTROUND);
        state.dealCards();

        state.setPhase(PHASE.PICKTRUMP1);
        boolean proceed = pickTrump(null); //null indicates initial entry to phase

        //If an AI player picks a trump before it gets to a human player, proceed directly to next phase
        if(proceed){
            playTrick(null); //null indicates initial entry to phase again
        }
    }

    /**
     * Method for handling trump selection phases. 
     * @param suit Suit selected by the player, or null if no player yet asked for selection.
     * @return If true, proceed to next phase. If false, return and await player input.
     */
    public boolean pickTrump(Card.SUIT suit){
        
        Player activePlayer = state.getActivePlayer();
        Boolean b;

        //If in first trump selection phase, picking based on face-up card.
        if(state.getCurrentPhase() == PHASE.PICKTRUMP1){
            b = this.pickTrumpPhase1(suit, activePlayer);

            //If the result is null, we go straight to PICKTRUMP2, otherwise return the value
            if(b != null){
                return (boolean) b;
            }else{
                state.setPhase(PHASE.PICKTRUMP2);
            }
        }

        //If in second trump selection phase, picking suit other than face-up suit
        if (state.getCurrentPhase() == PHASE.PICKTRUMP2) {
            return this.pickTrumpPhase2(suit, activePlayer);
        }
        return true;
    }

    /**
     * Helper method of pickTrump representing possibilites in phase PICKTRUMP1.
     * @param suit The suit the player selected
     * @param activePlayer The active player
     * @return True or False if the outer method should return one of those values, null
     * if the outer method should proceed straight to phase PICKTRUMP2. True represents proceeding
     * to PLAYTRICK, false represents waiting for player input
     */
    public Boolean pickTrumpPhase1(Card.SUIT suit, Player activePlayer){
        //If the player is directly to the dealer's left, just starting phase.
        if(activePlayer.getPosition() == (state.getDealerPosition() + 1)){
            playersPassedTrump = 0;

            suit = activePlayer.chooseTrump(state);

            //If suit is null, we're awaiting a response from a human player
            if(suit == null){
                return (Boolean) false;
            }
        }

        if(suit == SUIT.NONE){
            //Player has passed
            playersPassedTrump++;
            state.setActivePlayer(activePlayer.getPosition() + 1);
            //If all have passed, proceed to PICKTRUMP2
            if(playersPassedTrump == 4){
                playersPassedTrump = 0;
                state.setPhase(PHASE.PICKTRUMP2);
                return null; //null indicates we're going straight into phase 2
            }else{
                //Ask next player for suit.
                activePlayer = state.getActivePlayer();
                state.setPhase(PHASE.PICKTRUMP1);
                suit = activePlayer.chooseTrump(state);

                //If null, await player response
                if(suit == null){
                    return (Boolean) false;

                //Otherwise an AI player has responded.
                }else{
                    return pickTrumpPhase1(suit, activePlayer);
                }
            }
        
        }

        //Otherwise a suit has been selected, proceed to REPLACECARD
        else{
            state.setTrump(suit);
            state.setAttackingTeam(activePlayer.getPosition());

            Player dealer = state.getPlayer(state.getDealerPosition());

            //Add face up card to dealer's hand
            state.getHand(dealer.getPosition()).addCard(state.getFaceUpCard());
            state.setPhase(PHASE.REPLACECARD);

            //Ask dealer to choose which card gets replaced
            Card c = dealer.chooseReplacement(state);

            if(c == null){
                return (Boolean) false; //exit and await player response
            }else{
                //AI dealer has selected a card, proceed to PLAYTRICK
                return replaceCard(c); 
            }
        }
    }

    /**
     * Method for the REPLACECARD state
     * @param c The card to replace
     * @return True (proceed to the next state)
     */
    public boolean replaceCard(Card c){
        state.getHand(state.getDealerPosition()).removeCard(c);
        state.setLeadingPlayer(state.getActivePlayer().getPosition());
        state.setPhase(PHASE.PLAYTRICK);
        return true;
    }

    /**
     * Helper method for pickTrump representing possible states in phase PICKTRUMP2
     * @param suit The suit the active player has chosen
     * @param activePlayer The active player
     * @return Boolean value for outer method to return. True represents proceeding
     * to playTrick, false represents waiting for player input.
     */
    private boolean pickTrumpPhase2(Card.SUIT suit, Player activePlayer){
        if(suit == Card.SUIT.NONE){
            playersPassedTrump++;
            state.setActivePlayer(activePlayer.getPosition() + 1);
            activePlayer = state.getActivePlayer();
            suit = activePlayer.chooseTrump(state);
            
            if(suit == null){
                return false; //exit and await player input
            }else{
                state.setPhase(PHASE.PICKTRUMP2);
                return pickTrumpPhase2(suit, activePlayer);
            }

        }else{
            //Suit has been selected, proceed to PLAYTRICK
            state.setTrump(suit);
            state.setAttackingTeam(activePlayer.getPosition());
            state.setLeadingPlayer(activePlayer.getPosition());
            state.setPhase(PHASE.PLAYTRICK);
            return true;
        }
    }

    /**
     * Method for the PLAYTRICK state
     * @param c The card played, or null if no card played yet
     * @return True if the game should proceed to the next state, or false if awaiting player input
     */
    public boolean playTrick(Card c){
        //If no card yet played, ask active player for a card
        if(c == null){
            c = state.getActivePlayer().chooseCard(state);

            //if still null, awaiting player response. 
            if(c == null){
                return false;
            }
            //Otherwise we have a card played by an AI player
            else{
                return playTrick(c);
            }
        }else{
            //Play the card
            state.addPlayedCard(c);
            if(state.getPlayedCards().size() == 1){
                state.setLeadingSuit(c.getSuit());
            }

            //If enough cards have been played, score the trick
            int numPlayedCards = state.getPlayedCards().size();
            if(numPlayedCards == 4 || (numPlayedCards == 3 & state.getPlayerGoingAlone() != -1)){
                state.setPhase(PHASE.SCORETRICK);
                return true;
            }
            //Otherwise activate the next player and ask for a card.
            else{
                Player activePlayer = state.getActivePlayer();
                state.setActivePlayer(activePlayer.getPosition() + 1);
                activePlayer = state.getActivePlayer();
                state.setPhase(PHASE.PLAYTRICK);
                c = activePlayer.chooseCard(state);
                if(c == null){
                    return false;
                }
                else{
                    return playTrick(c);
                }
            }
        }
    }

    /**
     * Scores the current trick and adds the point to the winning team.
     */
    public void scoreTrick(){
        ArrayList<Card> playerCards = state.getPlayedCards();

        int winningPlayerPosition = determineWinningPlayer(playerCards);

        state.addTrick(winningPlayerPosition);
        
        //Clear played cards and suits for the trick
        state.clearPlayedCards();
        state.setTrump(null);
        state.setLeadingSuit(null);

        int totalTricks = state.getTeamTricks(1) + state.getTeamTricks(2);
        if(totalTricks < 5){
            state.setLeadingPlayer(winningPlayerPosition); //player who won current trick starts of next trick
            state.setActivePlayer(winningPlayerPosition);
            state.setPhase(PHASE.PLAYTRICK);

            //Ask next player for input
            Card c = state.getActivePlayer().chooseCard(state);
            
            //If null, waiting on player response
            if(c == null){
                return;
            }
            //Otherwise an AI player has selected a card, proceed with playTrick.
            else{
                playTrick(c);
            }
        }
        else{
            state.setLeadingPlayer(-1);
            state.setPhase(PHASE.SCOREROUND);
            scoreRound();
        }
    }

    /**
     * Determines highest value card that wins trick
     * 
     * @param cards - The cards that have been played, in played order
     * @return The position of the player who won the trick
     */
    private int determineWinningPlayer(ArrayList<Card> cards) {
        int max = Integer.MIN_VALUE;
        int maxIndex = 0;
        int currentValue;

        Card.SUIT leadingSuit = state.getLeadingSuit();
        Card.SUIT trumpSuit = state.getTrump();

        for (int i = 0; i < cards.size(); i++) { // starts at leading player
            currentValue = cards.get(i).value(trumpSuit, leadingSuit);
            if (currentValue > max) {
                maxIndex = i;
                max = currentValue;
            }
        }
        return ((state.getLeadingPlayerPosition() + maxIndex) % 4);
    }

    public void scoreRound(){
        int teamOneTricks = state.getTeamTricks(1);
        int teamTwoTricks = state.getTeamTricks(2);

        //Figure out who won
        int winningTeam;
        int winningTricks;
        if (teamOneTricks > teamTwoTricks){
            winningTeam = 1;
            winningTricks = teamOneTricks;
        }else{
            winningTeam = 2;
            winningTricks = teamTwoTricks;
        }
        
        int attackingTeam = state.getAttackingTeam();
        if(attackingTeam == winningTeam){
            //If calling team won all 5 tricks
            if(winningTricks == 5){
                //And if the player went alone
                if(state.getPlayerGoingAlone() != -1){
                    //Then they score 4 points
                    state.addScore(winningTeam, 4);
                }else{
                    //Otherwise just 2 points
                    state.addScore(winningTeam, 2);
                }
                
            }else{
                //If they scored fewer than 5 tricks, they score 1 point
                state.addScore(winningTeam, 1);
            }
        }
        //If non-calling team won, they score 2 points
        else{
            state.addScore(winningTeam, 2);
        }

        //See if anyone has won the game
        int teamOneScore = state.getTeamScore(1);
        int teamTwoScore = state.getTeamScore(2);
        if(teamOneScore >= 10){
            endGame(1);
        }
        else if(teamTwoScore >= 10){
            endGame(2);
        }
        //if not, go to next round
        else{
            state.setPhase(PHASE.STARTROUND);
            startRound();
        }

    }

    //Updates the state to ENDGAME, which triggers the observer to update the clients
    public void endGame(int winningTeam){
        state.setPhase(PHASE.ENDGAME);
    }

    public GameState.PHASE getCurrentPhase(){
        return state.getCurrentPhase();
    }

    public Player getActivePlayer(){
        //When replacing a card, dealer is the active player
        if(state.getCurrentPhase().equals(PHASE.REPLACECARD)){
            return state.getPlayer(state.getDealerPosition());
        }else{
            return state.getActivePlayer();
        }
    }

}
