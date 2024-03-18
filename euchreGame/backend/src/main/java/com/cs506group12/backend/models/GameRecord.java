package com.cs506group12.backend.models;

import java.sql.*;

/**
 * Class for objects to represent the records of past games played.
 */
public class GameRecord {
    
    private Timestamp startTime;
    private Timestamp endTime;
    private String[] players;
    private int[] score;

    /**
     * Creates a GameRecord object.
     * 
     * @param startTime The timestamp of the game's start
     * @param endTime The timestamp of the game's end
     * @param players A String array of size 4 consisting of the usernames of the players in the game. 
     * @param score An int array of size 2 consisting of the scores of teams 1 and 2. Team 1 consists of players 1 and 3, and team 2 consists of players 2 and 4
     */
    public GameRecord(Timestamp startTime, Timestamp endTime, String players[], int score[]){
        this.startTime = startTime;
        this.endTime = endTime;
        this.players = players;
        this.score = score;
    }

    /**
     * Gets the players on the team with the given team number.
     * 
     * @param teamNumber The identifying number (1 or 2) of the team from which to get the players.
     * @return String array of size 2 with the names of the players on the given team
     */
    public String[] getTeam(int teamNumber){
        teamNumber = (teamNumber + 1) % 2;
        String[] team = {this.players[teamNumber], this.players[teamNumber + 2]};
        return team;
    }

    /**
     * Gets the score of the team with the given team number (1 or 2)
     * 
     * @param teamNumber The identifying number (1 or 2) of the team whose scores should be returned.
     * @return The score of the team
     */
    public int getScore(int teamNumber){
        return score[ (teamNumber + 1) % 2 ];
    }

    /**
     * Gets the team number that the given player played on.
     * Returns -1 if the player is not found in the list. 
     *  
     * @param playerName The name of the player to identify
     * @return The team number the player was on. -1 if the player was not found on a team.
     */
    public int getTeamNumber(String playerName){
        int team = -1;
        for (int i=0; i<4; i++){
            if (players[i].equals(playerName)){
                team = (i + 1 ) % 2;
            }
        }
        return team;
    }

    /**
     * Gets the starting time of the game
     * @return The time the game started
     */
    public Timestamp getStartTime(){
        return this.startTime;
    }

    /**
     * Gets the ending time of the game
     * @return The time the game ended
     */
    public Timestamp getEndTime(){
        return this.endTime;
    }

}
