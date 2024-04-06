package com.cs506group12.backend.models;

import java.util.*;

public class Turn {
	private String trump;
	private int numPlayers = 4;
	private ArrayList<Card> playedCards =  new ArrayList<Card>();
	private int leadingPlayer;
	private ArrayList<Player> players;
	private int numPlayingCards = 4;  // for if we implement 3 player
	private int teamThatWonTrick = 0;
	private int teamThatWonTurn;
	private int points = 1;
	public int attackingTeam;
	public int[] numTricks = {0,0};


	/*
	/**
	 * @param cards player hands
	 * @return the index of the player with the highest value card
	 */
	public int score(ArrayList<Card> cards){
		int max = 0;
		int maxIndex = 0;

		//for (int i = 0; i < numPlayers; i++){  // starts at leading player

			/** */
			//COMMENTED OUT HERE
		//	if (cards.get(i).value() > max){
		//		maxIndex = i;
		//		COMMENTED OUT HERE - Kaldan 
		//		max = cards.get(i).value();
		//	}
		//}
		return maxIndex;
		
	}


	/**
	 * 
	 * @return the team number that won the trick
	 */
	public int handleTrick(){
		for (int i = 0; i < numPlayingCards; i++){
			// call a controller and add result to arraylist of played cards then score
			teamThatWonTrick = (leadingPlayer + score(playedCards)) % 2; 
			numTricks[teamThatWonTrick]++; 
		}
		playedCards.clear();
		leadingPlayer+=1;
		if (numPlayers==3){
			points = 2;
		}
		return teamThatWonTrick;
	}
	// iterate over players 4 players - dealer is starting position
	// When user selects a card to play, retrieve it from frontend and call
	// players.get(i).removeAndPlayCard(exCard)
	// probably return a winner and how many points
	public Turn(ArrayList<Player> players, boolean isTimed, int leadingPlayer, String trump) { // maybe pass in turned up card
		this.leadingPlayer = leadingPlayer;
		this.trump = trump;
		this.players = players;

	}



}
