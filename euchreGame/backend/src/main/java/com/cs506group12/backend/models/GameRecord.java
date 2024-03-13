package com.cs506group12.backend.models;

import java.sql.*;

/*
 * Object for the records of past games played
 */
public class GameRecord {
    
    private Timestamp startTime;
    private Timestamp endTime;
    private String[] players;
    private int[] score;

    public GameRecord(Timestamp startTime, Timestamp endTime, String players[], int score[]){
        this.startTime = startTime;
        this.endTime = endTime;
        this.players = players;
        this.score = score;
    }

    // Given the team number (1 or 2) return the players on that team
    // Team 1 are positions 0 and 2, team 2 are positions 1 and 3
    public String[] getTeam(int teamNumber){
        teamNumber = (teamNumber % 2) + 1;
        String[] team = {this.players[teamNumber], this.players[teamNumber + 2]};
        return team;
    }

    /*
     * Gets the score of the team with the given team number (1 or 2)
     */
    public int getScore(int teamNumber){
        return score[ (teamNumber % 2) + 1];
    }

    /*
     * Gets the team number that the given player played on.
     * Returns -1 if the player is not found in the list. 
     */
    public int getTeamNumber(String playerName){
        int team = -1;
        for (int i=0; i<4; i++){
            if (players[i].equals(playerName)){
                team = (i % 2) + 1;
            }
        }
        return team;
    }

    public Timestamp getStartTime(){
        return this.startTime;
    }

    public Timestamp getEndTime(){
        return this.endTime;
    }

}
