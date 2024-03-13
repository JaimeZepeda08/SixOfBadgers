package com.cs506group12.backend;

import java.sql.*;
import java.util.ArrayList;

import com.cs506group12.backend.models.User;
import com.cs506group12.backend.models.GameRecord;

//Class for handling the JDBC connections to the MySQL database. Does not assume that a connection
// to the database has already been made (or is currently open) before methods are called.
public class DatabaseConnection {
    
    private static Connection databaseConnection;

    //Returns True if a connection was successfully made to the mysql database.
    public static Boolean createConnection() {
        try{
            if (databaseConnection == null || databaseConnection.isClosed()){ //lazy evaluation to avoid null ref
                databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:53306/euchre", "root", "supersecure");
                return true;
            }else{
                return true;
            }
        } catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }

    /*
     * Gets the record of a user with the given name and password. Currently password is passed in plaintext
     * and stored as a 512bit SHA2 hash. TODO make this more secure
     */
    public static User getUser(String userName, String userPassword) throws SQLException{
        if(!createConnection()){
               throw new SQLException("Unable to create database connection.");
        }
        
        Statement selectUser = databaseConnection.createStatement();
        ResultSet rsUser = selectUser.executeQuery("SELECT UserUID, Settings from Users WHERE UserName = '" + userName + "' AND PasswordHash = SHA2('" + userPassword + "',512)");
        if(rsUser.next()){  
            //settings are stored as comma separated strings in the database
            int userUID = rsUser.getInt(1);
            String settingsString = rsUser.getString(2);
            String[] settingsArray = null;
            if(settingsString != null){
                settingsArray = settingsString.split(",");
            }
            User user = new User(userUID, userName, settingsArray);
            return user;
        }else{
            throw new SQLException("Unable to locate user in database");
        }


    }

    //Stores a new user in the database with the given name and password. Throws an exception if it cannot connect
    //to the database or a user already exists with the given name. Returns true if user successfully created, false otherwise.
    public static void storeUser(String userName, String userPassword) throws SQLException{
        if(!createConnection()){
            throw new SQLException("Unable to create database connection.");
        }

        Statement storeUser = databaseConnection.createStatement();
        ResultSet rsUserCheck = storeUser.executeQuery("SELECT UserUID FROM Users WHERE UserName = '" + userName + "';");
            
        //If the ResultSet contains a row, a user with the given name already exists.
        if(rsUserCheck.isBeforeFirst()){
            throw new SQLException("User name already exists in database");
        }

        storeUser.executeUpdate("INSERT INTO Users (UserName, PasswordHash, AccountCreation) VALUES ('"
            + userName + "', SHA2('"+ userPassword +"',512), CURRENT_TIME());");
    }
    
    /*
     * Stores the record of a game.
     */
    public static void storeGameRecord(String[] players, int[] scores, Timestamp startTime, Timestamp endTime ) throws SQLException{
        if(!createConnection()){
            throw new SQLException("Unable to create database connection.");
        }

        PreparedStatement getUserFKs = databaseConnection.prepareStatement("select u1.UserUID, u2.UserUID, u3.UserUID, u4.UserUID FROM Users as u1, Users as u2, Users as u3, Users as u4 "
        + "WHERE u1.UserName = ? AND u2.UserName = ? AND u3.UserName = ? AND u4.UserName = ?");
        for(int i=0; i<4; i++){
            getUserFKs.setString(i+1, players[i]);
        }
        ResultSet rsGetUserFKs = getUserFKs.executeQuery();

        rsGetUserFKs.next();

        PreparedStatement storeGame = databaseConnection.prepareStatement("INSERT INTO Games "
        + "(Player1, Player2, Player3, Player4, GameStartTime, GameEndTime, Team1Score, Team2Score) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
          //  storeGame.executeUpdate("INSERT INTO Games (Player1, Player2, Player3, Player4, GameStartTime, GameEndTime, Team1Score, Team2Score) "
          //  + "VALUES ('" + players[0] + "', '" + players[1] + "', '" + players[2] + "', '" + players[3]
          //  + "', '" + startTime + "', '" + endTime + "', '" + scores[0] + "', '" +scores[1] + "')");
        for(int i=0; i<4; i++){
            storeGame.setInt(i+1, rsGetUserFKs.getInt(i+1));
        }
        storeGame.setTimestamp(5,startTime);
        storeGame.setTimestamp(6,endTime);
        storeGame.setInt(7, scores[0]);
        storeGame.setInt(8, scores[1]);

        storeGame.executeUpdate();

        return;
    }

    /*
     * Gets all game records in the SQL database where the user with the given username played.
     */
    public static ArrayList<GameRecord> getGameRecords(String userName) throws SQLException{

        if(!createConnection()){
            throw new SQLException("Unable to create database connection.");
        }

        ArrayList<GameRecord> gameRecords = new ArrayList<GameRecord>();

        try{
            Statement getRecords = databaseConnection.createStatement();
            ResultSet rsGameRecords = getRecords.executeQuery(
            "select GameStartTime, GameEndTime, u1.UserName, u2.UserName, u3.UserName, u4.UserName, Team1Score, Team2Score" 
            + " FROM Games left join Users AS u1 on Games.Player1=u1.UserUID"
            + " left join Users AS u2 on Games.Player2=u1.UserUID"
            + " left join Users AS u3 on Games.Player3=u3.UserUID"
            + " left join Users AS u4 on Games.Player4=u4.UserUID"
            + " WHERE u1.UserName = '"+ userName +"' OR u2.UserName = '"+ userName +"' OR u3.UserName = '"+ userName +"' OR u1.UserName = '"+ userName +"';");

            String players[] = new String[4];
            int scores[] = new int[2]; 

            while(rsGameRecords.next()){

                for(int i=0; i<4; i++){
                    players[i] = rsGameRecords.getString(i+3);
                }
                scores[0] = rsGameRecords.getInt(7);
                scores[1] = rsGameRecords.getInt(8);

                gameRecords.add(new GameRecord(rsGameRecords.getTimestamp(1), rsGameRecords.getTimestamp(2), players, scores));
            }

        }catch(Exception e){
            System.err.println(e);
        }

        return gameRecords;
    }

}
