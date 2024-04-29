package com.cs506group12.backend.models;

import java.util.Iterator;

import com.cs506group12.backend.models.Card.SUIT;
import com.cs506group12.backend.models.GameState.PHASE;
import com.cs506group12.backend.interfaces.*;

/**
 * Decorator class for Player that implements methods for AI players
 * to select trump or a card to play
 * 
 * @author Eric Knepper
 */
public class AIPlayerDecorator extends PlayerDecorator{

    private final static int TRUMP_VAL_THRESHOLD = 38; //Threshold for picking trump
    private final static int GO_ALONE_THRESHOLD = 60; //Threshold for picking trump and going alone

    public AIPlayerDecorator(Player player){
        super(player);
    }

    public String getName(){
        return "EuchreBot " + super.decoratedPlayer.getPosition();
    }

    public void sendMessage(String formattedJSON){
        return;
    }

    public int getPosition(){
        return super.decoratedPlayer.getPosition();
    }

    /**
     * Method for getting an AI player to select a suit for trump. 
     * Returns the turmp the AI player will select, or null if the AI player passes.
     * Can modify game state if the AI player decides to go alone (sets playerGoingAlone)
     * @param state The current game state
     * @param playerNumber The index of the AI player
     * @return The called suit for trump, or null if passing
     */
    public Card.SUIT chooseTrump(GameState state){
        
        int position = super.decoratedPlayer.getPosition();
        Card faceUpCard = state.getFaceUpCard();
        Card.SUIT trump = null;
        int suitValue;

        //Phase PICKTRUMP1 is where there is a face up card to decide on
        if(state.getCurrentPhase() == PHASE.PICKTRUMP1){
 

            //get value of hand if face up card became trump
            suitValue = totalCardValue(state, faceUpCard.getSuit());

            //If you're on the same team as the dealer (yourself or your teammate), then count face up card
            if(state.getDealerPosition() == position || state.getDealerPosition() == ((position + 2) % 4)){
                suitValue += faceUpCard.value(faceUpCard.getSuit(), faceUpCard.getSuit()) - 28;
            }else{
                //otherwise reduce weight by part of cards value since your opponents get it
                suitValue -= (faceUpCard.value(faceUpCard.getSuit(), faceUpCard.getSuit()) - 28)/2;
            }

            //Check to see if your hand is good enough
            if(suitValue >= TRUMP_VAL_THRESHOLD){
                trump = faceUpCard.getSuit();

                //If hand is great, go alone.
                if(suitValue > GO_ALONE_THRESHOLD){
                    state.setPlayerGoingAlone(position);
                }
            }else{
                trump = SUIT.NONE;
            }

        //phase is PICKTRUMP2, i.e. select any suit that wasnt the face up card.
        }else{ 

            Card.SUIT highestSuit = null;
            int highestValue = 0;

            //check through remaining suits to see if any has sufficiently strong cards to pick a trump
            for (Card.SUIT suit : Card.SUIT.values()) {
                if(suit == faceUpCard.getSuit()){
                    break; //can't select the same suit that was face up
                }else{
                    //Check value of cards of given suit, record the highest valued suit
                    suitValue = totalCardValue(state, suit);
                    if(suitValue > highestValue){
                        highestSuit = suit;
                        highestValue = suitValue;
                    }
                }
            }

            //Check to see if your best hand is good enough to call trump, or if you are stuck as the dealer
            if(highestValue >= TRUMP_VAL_THRESHOLD || position == state.getDealerPosition()){
                trump = highestSuit;

                //If hand is great, go alone.
                if(highestValue > GO_ALONE_THRESHOLD){
                    state.setPlayerGoingAlone(position);
                }
            }else{
                trump = SUIT.NONE;
            }

        }
        return trump;
    }

    /**
     * Method for selecting which card to play fron the AI player's hand
     * @param state The current game state
     * @param playerNumber The index of the AI player
     * @return The card to be played.
     */
    public Card chooseCard(GameState state){
        int position = super.decoratedPlayer.getPosition();

        //If leading player, play your best card
        if(state.getLeadingPlayerPosition() == position){
            return getBestCard(state, new boolean[]{true,true,true,true,true});
        }else{
            Card.SUIT leadingSuit = state.getLeadingSuit();
            Card.SUIT trump = state.getTrump();

            //if your best card is better than the current leading card, play it
            boolean[] playableCards = state.getHand(position).getPlayableCards(leadingSuit, trump);
            Card bestCard = getBestCard(state, playableCards);
            if(bestCard.value(trump, leadingSuit) > state.highestPlayedCard().value(trump, leadingSuit)){
                return bestCard;
            }

            //otherwise play your worst card
            return getWorstCard(state, playableCards);
        }
    }

    /**
     * Method for getting an AI player to choose which card in their hand to replace
     * with the face up trump card. At point of method call, trump card has been added to hand
     * @param state The current game state
     */
    public Card chooseReplacement(GameState state){
        return getWorstCard(state, null); //null playable indicates replacing card
    }

    /**
     * Method for determining when/what suit to call as trump. Gets total value of cards of a 
     * certain suit (if it were trump).
     * @param state The current game state
     * @param trump The suit to evaluate for trump
     * @return The total value of cards in hand of the given suit for trump. Used to compare to a threshold value.
     */
    private int totalCardValue(GameState state, Card.SUIT trump){
        int position = super.decoratedPlayer.getPosition();
        int sum = 0;
        Hand hand = state.getHand(position);
        Card c;
        Iterator<Card> it = hand.iterator();
        while(it.hasNext()){
            c = it.next();
            if (c.getSuit() == trump || (c.getValue() == 11 && Card.twinColor(c.getSuit()) == trump)) {
                sum += c.value(trump, trump) - 28;
            }
        }
        return sum;
    }

    /**
     * Method used to decide which card in hand is least valuable. When playableIndicies is not null, evaluates
     * only playable cards.
     * @param state The Current game state
     * @param playerNumber The index of the AI player
     * @param playableIndicies The indicies of playable cards from Player.getPlayableCards. Leave null when
     *      evaluating which card to discard while picking trump
     * @return The worst card in hand
     */
    private Card getWorstCard(GameState state, boolean[] playableIndicies){
        int position = super.decoratedPlayer.getPosition();
        Hand hand = state.getHand(position);
        Card c;
        int currentValue;
        int lowestValue = Integer.MAX_VALUE;
        Card lowestCard = null;
        Card.SUIT currentTrump = state.getTrump();
        Card.SUIT leadingSuit = state.getLeadingSuit();

        //Otherwise just find the lowest valued card given the current trump
        for(int i=0; i<hand.getSize(); i++){
            c = hand.getCard(i);
            
            //When there is no leading suit we're picking a card to replace in the hand
            if (leadingSuit == null){ 
                currentValue = c.value(currentTrump, c.getSuit());

            //Otherwise we're intentionally playing a bad card in a trick
            }else{ 

                //When we're playing a card, only evaluate cards that are playable 
                if(playableIndicies != null && !playableIndicies[i]){
                    continue;
                }
                currentValue = c.value(currentTrump, leadingSuit);
            }
            
            if (currentValue < lowestValue){
                lowestCard = c;
                lowestValue = currentValue;
            }
        }

        return lowestCard;
        
    }

    /**
     * Method to determine which playable card in hand is most valuable.
     * @param state The current game state
     * @param playerNumber The index of the AI player
     * @param playableIndicies The boolean array indicating playable cards from Player.getPlayableCards
     * @return The highest valued playable card
     */
    private Card getBestCard(GameState state, boolean[] playableIndicies){
        int position = super.decoratedPlayer.getPosition();
        Hand hand = state.getHand(position);
        Card c;
        Card.SUIT currentTrump = state.getTrump();
        Card.SUIT leadingSuit = state.getLeadingSuit();
        Card.SUIT testSuit;
        int currentValue;
        int highestValue = Integer.MIN_VALUE;
        Card highestCard = null;

        //Iterate through the hand and find the highest valued playable card given the trump and leading suit
        for(int i=0; i<hand.getSize(); i++){

            //Don't evaluate cards we can't play
            if(playableIndicies[i] == false){
                continue; 
            }

            c = hand.getCard(i);
            if(leadingSuit != null){
                testSuit = leadingSuit;
            }
            //If no leading suit, then we are playing our best card given its own suit
            else{
                testSuit = c.getSuit();
            }
            currentValue = c.value(currentTrump, testSuit);
            if(currentValue > highestValue){
                highestValue = currentValue;
                highestCard = c;
            }
        }

        return highestCard;
    }

    //Pick trump

    //Evaluate expected tricks from hand
        //if picking first round trump and you or partner is dealer, EV+ (get an additional trump)

    //Pick a card to play - expect to win or expect to lose
        //lose trick on purpose to draw out higher cards - if you have 2 mid trump & one low trump bait out with low trump first

    //Pick high card to play
        //when to go for trump vs when to go for non-trump as leading player

    //pick low card to play
        //get rid of single suit low cards if you have trump (so you don't have to follow suit)

    //can we have all played cards in gamestate for AI to check for decision maing?
        //useful to know which cards have been played to know which cards are the highest remaining - this is more advanced logic

    //basic model vs smart model

    //basic model actions
        //Pick a trump - only pick if you have 3 or more cards of that suit (if stuck, pick from suits with highest number of cards with the highest avg card value)
        //Pick a card to play - play your best card if you are leading, otherwise pick your highest card if it is higher than current pool, otherwise pick lowest card
        //when to go alone -- if you have 4-5 of trump suit when calling

        //utilities needed for this - # of cards in suit, Avg value of cards in suit, 
        //best card in hand, worst card in hand, is a card in hand higher than the best in pool

}
