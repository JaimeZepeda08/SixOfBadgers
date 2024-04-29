package com.cs506group12.backend.interfaces;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.GameState;

/**
 * Interface for players in the euchre game
 * 
 * @author Eric Knepper
 */
public interface Player {
    public Card chooseCard(GameState state);
    public Card.SUIT chooseTrump(GameState state);
    public Card chooseReplacement(GameState state);
    public String getName();
    public int getPosition();
    public void sendMessage(String formattedJSON);
}
