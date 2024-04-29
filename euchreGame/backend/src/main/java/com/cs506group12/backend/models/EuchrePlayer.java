package com.cs506group12.backend.models;

import com.cs506group12.backend.interfaces.*;

/**
 * Base class for players to be decorated with either AIPlayerDecorator or
 * HumanPlayer Decorator
 * 
 * @author Eric Knepper
 */
public class EuchrePlayer implements Player{

    private String playerName;
    private int position;

    public EuchrePlayer(String playerName, int position){
        this.playerName = playerName;
        this.position = position;
    }

    public Card.SUIT chooseTrump(GameState state){
        return null;
    }

    public Card chooseCard(GameState state){
        return null;
    }

    public Card chooseReplacement(GameState state){
        return null;
    }

    public String getName(){
        return this.playerName;
    }

    public int getPosition(){
        return this.position;
    }

    public void sendMessage(String formattedJSON){
        return;
    }

}
