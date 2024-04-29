package com.cs506group12.backend.models;

import java.util.*;
import com.cs506group12.backend.interfaces.*;

public class GameStateObserver {

    private final String BL = "\",\n\t\"";  //Between Lines in JSON
    private final String BKV = "\": \"";    //Between Key/Value in JSON

    //Object to track the updates before compliling into JSON
    //Access to client sessions to communicate with
    private HashMap<String,String> updates;

    //Hands are private information, so are tracked separately for individual messages
    private String handUpdates[];

    public GameStateObserver(){
        this.updates = new HashMap<String,String>();
        handUpdates = new String[]{"","","",""};
    }
    
    //executes a batched update on state change
    public void sendUpdate(GameState.PHASE currentPhase, GameState.PHASE nextPhase, ArrayList<Player> players){
        for(Player player : players){
            player.sendMessage(this.compileJSONContent(players, player.getPosition()));
        }
        updates.clear();
        handUpdates = new String[]{"","","",""};
    }

    /**
     * Compiles the various updates into a JSON to send to the clients
     * @param playerPosition Position of the player to send the update to
     * @return Compiled string for the content of a JSON message to the player
     */
    public String compileJSONContent(ArrayList<Player> players, int playerPosition){
        String header = "header" + BKV + "gameupdate" + BL;
        String trickInfo = "";
        if(updates.containsKey("DP")) trickInfo += "dealer" + BKV + updates.get("DP") + BL;
        if(updates.containsKey("LP")) trickInfo += "leading_player" + BKV + updates.get("LP") + BL;
        if(updates.containsKey("TR")) trickInfo += "trump_suit" + BKV + updates.get("TR") + BL;
        if(updates.containsKey("LS")) trickInfo += "leading_suit" + BKV + updates.get("LS") + BL;
        if(updates.containsKey("AT")) trickInfo += "attacking_team" + BKV + updates.get("AT") + BL;
        if(updates.containsKey("SP")) trickInfo += "solo_player" + BKV + updates.get("SP") + BL;
        if(updates.containsKey("WTT")) trickInfo += "team_that_won_trick" + BKV + updates.get("WTT") + BL;

        String scoreInfo = "";
        if(updates.containsKey("TS1")) scoreInfo += "team_one_score" + BKV + updates.get("TS1") + BL;
        if(updates.containsKey("TS2")) scoreInfo += "team_two_score" + BKV + updates.get("TS2") + BL;
        if(updates.containsKey("TT1")) scoreInfo += "team_one_tricks" + BKV + updates.get("TT1") + BL;
        if(updates.containsKey("TT2")) scoreInfo += "team_two_tricks" + BKV + updates.get("TT2") + BL;
        
        String playersJson = "all_players\": [\"";
        for (int i = 0; i < players.size(); i++) {
            if(i > 0) playersJson += "\", \"";
            playersJson += players.get(i).getName();
        }
        playersJson += "\"]\n\t";
        String cardsInfo = "";
        cardsInfo += handUpdates[(playerPosition - 1) % 4];
        return "{\n \"" + header + trickInfo + scoreInfo + playersJson + cardsInfo + "}";
    }

    //Adds changes to a player's hand to the update batch
    public void updateHand(int playerIndex, Hand hand){
        handUpdates[playerIndex] = hand.toJSON();
    }

    public void updatePlayedCards(ArrayList<Card> cards){
        updates.put("PC",cardsToJSON(cards));
    }

    //Adds changes to the score to the update batch
    public void updateScore(int teamNumber, int score){
        updates.put("TS"+teamNumber, ""+score);
    }

    public void updateTricks(int teamNumber, int tricks){
        updates.put("TT"+teamNumber, ""+tricks);
    }

    //Adds changes to the trump to the update batch
    public void updateTrump(Card.SUIT suit){
        updates.put("TR",Card.suitToJSON(suit));
    }

    //Adds changes to the dealer or leader position to the update batch
    public void updatePositions(int dealerPosition, int leaderPosition){
        updates.put("DP",""+dealerPosition);
        updates.put("LP",""+leaderPosition);
    }

    //Adds change of active player to the update batch
    public void updateActivePlayer(int activePlayerPosition){
        updates.put("AP",""+activePlayerPosition);
    }

    public void updateFaceUpCard(Card c){
        String json;
        if(c != null){
            json = c.cardToJSON();
        }else{
            json = "NONE";
        }
        updates.put("FC",json);
    }

    /**
     * used when starting game or when loading gamestate from database
     */
    public void updateEntireState(int dealerPosition, int leadingPlayerPosition, int activePlayerPosition,
        Card faceUpCard, Card.SUIT trumpSuit, Card.SUIT leadingSuit, ArrayList<Card> playedCards,
        Hand[] hands, int[] scores, int[] tricks, int attackingTeamNumber){
        updatePositions(dealerPosition, leadingPlayerPosition);
        updateActivePlayer(activePlayerPosition);
        updateFaceUpCard(faceUpCard);
        updateTrump(trumpSuit);
        updateLeadingSuit(leadingSuit);
        for(int i=0; i<hands.length; i++){
            updateHand(i, hands[i]);
        }
        for(int i=0; i<scores.length; i++){
            updateScore(i + 1, scores[i]);
        }
        for(int i=0; i<tricks.length; i++){
            updateTricks(i + 1, tricks[i]);
        }
        updatePlayedCards(playedCards);
        updateAttackingTeam(attackingTeamNumber);
    }

    public void updateLeadingSuit(Card.SUIT suit){
        updates.put("LS",Card.suitToJSON(suit));
    }

    public void updateAttackingTeam(int teamNumber){
        updates.put("AT",""+teamNumber);
    }

    public void updateSoloPlayerPosition(int playerPosition){
        updates.put("SP",""+playerPosition);
    }

    public void updateTrickWinner(int teamNumber){
        updates.put("WTT",""+teamNumber);
    }

    private String cardsToJSON(ArrayList<Card> cards) {
		String cardsString = "[";
		for (Card card : cards) {
			cardsString += card.cardToJSON() + ",";
		}
		cardsString = cardsString.substring(0, cardsString.length() - 1);
		cardsString += "]";
		return cardsString;
	}
}
