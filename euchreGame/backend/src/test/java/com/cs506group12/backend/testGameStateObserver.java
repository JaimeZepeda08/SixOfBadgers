package com.cs506group12.backend;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.EuchrePlayer;
import com.cs506group12.backend.models.GameStateObserver;
import com.cs506group12.backend.models.Hand;
import com.cs506group12.backend.models.Card.SUIT;
import com.cs506group12.backend.models.GameState.PHASE;
import com.cs506group12.backend.interfaces.*;

public class testGameStateObserver {
    
    @Test
    public void testJSON(){
        GameStateObserver observer = new GameStateObserver();
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new EuchrePlayer("test1", 1));
        players.add(new EuchrePlayer("test2", 2));
        players.add(new EuchrePlayer("test3", 3));
        players.add(new EuchrePlayer("test4", 4));

        observer.updateTrump(SUIT.CLUBS);
        observer.updateAttackingTeam(1);
        observer.updateFaceUpCard(new Card(Card.SUIT.SPADES,9));
        observer.updateTricks(1, 3);
        observer.updateTricks(2, 1);
        observer.updateScore(1, 7);
        observer.updateScore(2, 5);
        observer.updateActivePlayer(2);
        observer.updateLeadingSuit(Card.SUIT.DIAMONDS);
        observer.updateSoloPlayerPosition(3);
        observer.updateTrickWinner(1);
        Hand hand = new Hand();
        hand.addCard(new Card(Card.SUIT.CLUBS,11));
        hand.addCard(new Card(Card.SUIT.DIAMONDS,11));
        hand.addCard(new Card(Card.SUIT.HEARTS,11));
        observer.updateHand(1, hand);
        String json = observer.compileJSONContent(players, 1);
        assert(isValidJson(json));
    }

    /**
	 * 
	 * @param json string to check if json
	 * @return true if stirng is a valid json- false otherwise
	 */
	public boolean isValidJson(String json) {
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}
}
