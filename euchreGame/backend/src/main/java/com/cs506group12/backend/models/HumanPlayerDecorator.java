package com.cs506group12.backend.models;

import com.cs506group12.backend.interfaces.Player;

/**
 * Decorator class for Player that implements methods for asking a human
 * player which card to play or trump to select
 */
public class HumanPlayerDecorator extends PlayerDecorator {
	private Client client;

	/**
	 * Constructor class for the HumanPlayerDecorator class.
	 */
	public HumanPlayerDecorator(Player player, Client client) {
		super(player);
		this.client = client;
	}

	public String getName(){
		return super.decoratedPlayer.getName();
	}

	public int getPosition(){
		return super.decoratedPlayer.getPosition();
	}

	/**
	 * Send a message to the human player prompting them for a card, then return null
	 * indicating that the program is awaiting a websocket message from the player.
	 */
	public Card chooseCard(GameState state){
		//TODO: Send message to player prompting a card choice
		client.sendMessage("{\n\t\"message\": \"pickCard\"\n}");
		return null;
	}

	/**
	 * Send a message to the human player prompting them to select a suit, then
	 * return null indicating that the program is awaiting a websocket message from
	 * the player.
	 * 
	 * @return null
	 */
	public Card.SUIT chooseTrump(GameState state) {
		//TODO: Send message to player prompting trump choice
		if(state.getCurrentPhase() == GameState.PHASE.PICKTRUMP1){
			client.sendMessage("{\n\t\"message\": \"choseIfYouWantFaceupAsTrump\"\n}");
		}

		if(state.getCurrentPhase() == GameState.PHASE.PICKTRUMP2
			&& super.decoratedPlayer.getPosition() == state.getDealerPosition()){
			//forced to pick trump
			client.sendMessage("{\n\t\"message\": \"pickTrumpOtherThanFaceupCard\"\n}");

		}
		return null;
	}

	/**
	 * 
	 */
	public Card chooseReplacement(GameState state){
		//TODO Send message to player for choice of card to replace
		client.sendMessage("{\n\t\"message\": \"choseCardToSwapWithFaceUpCard\"\n}");
		return null;
	}

	public void sendPlayer() {
		client.sendMessage(playerToJSON());
	}

	public void sendMessage(String header, String content){
        client.sendMessage(header, content);
    }

	public Client getClient(){
		return this.client;
	}

	public String playerToJSON() {
		return "{"
				+ "\"header\" : \"player\", "
				+ "\"name\" : " + "\"" + super.decoratedPlayer.getName() + "\", "
				+ "\"position\" : " + "\"" + super.decoratedPlayer.getPosition() + "\""
				+ "}";
	}

	
}