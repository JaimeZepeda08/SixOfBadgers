package com.cs506group12.backend;

import java.sql.*;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.cs506group12.backend.models.User;
import com.cs506group12.backend.models.GameRecord;

/**
 * DatabaseConnection is a class used to connect the backend game engine to the
 * MySQL database
 * to retrieve user profiles and game records. This class contains static
 * methods that do not assume
 * a connection to the database is open at the time they are called.
 * 
 * @author Eric Knepper
 */
public class DatabaseConnection {

    private DataSource dataSource;
    private Connection databaseConnection;

    public DatabaseConnection(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * This method creates a connection to the database. If there is an error in
     * creating the connection,
     * an error message is sent to System.err and it returns false.
     * 
     * @return Whether the connection was created successfully.
     */
    public Boolean createConnection() {
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) { // lazy evaluation to avoid null ref
                databaseConnection = dataSource.getConnection();
                return true;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    /**
     * Gets the record of a user with the given name and email address.
     * 
     * @param userName     The name of the user to be retrieved
     * @param userEmailAddress The email address of the user to be retrieved
     * @return A User object representing the requested user, or null if there is no
     *         such user in the database
     * @throws SQLException Throws an exception if a connection to the database
     *                      cannot be created.
     */
    public  User getUser(String userName, String userEmailAddress) throws SQLException {
        if (!createConnection()) {
            throw new SQLException("Unable to create database connection.");
        }

        PreparedStatement selectUser = databaseConnection.prepareStatement(
            "SELECT UserUID, Settings from Users WHERE UserName = ?"
                + " AND EmailAddress = ?;");
        selectUser.setString(1, userName);
        selectUser.setString(2, userEmailAddress);
        ResultSet rsUser = selectUser.executeQuery();

        
        if (rsUser.next()) {
            // settings are stored as comma separated strings in the database
            int userUID = rsUser.getInt(1);
            String settingsString = rsUser.getString(2);
            String[] settingsArray = null;
            if (settingsString != null) {
                settingsArray = settingsString.split(",");
            }
            User user = new User(userUID, userName, userEmailAddress);
            return user;
        } else {
            return null;
        }

    }

    /**
     * Creates a new user record in the database with the given name and email address.
     * Throws an exception if it cannot connect to the database.
     * Returns true if user successfully created, false otherwise.
     * 
     * @param userName     The name of the user to be stored
     * @param userEmailaddress The email address of the user to be stored
     * @throws SQLException Throws an exception if the databse connection cannot be
     *                      created or if there is a problem with the query.
     */
    public  Boolean storeUser(String userName, String userEmailaddress) throws SQLException {
        if (!createConnection()) {
            throw new SQLException("Unable to create database connection.");
        }

        PreparedStatement checkUser = databaseConnection.prepareStatement(
            "SELECT UserUID FROM Users WHERE EmailAddress = ?;"
        );
        checkUser.setString(1,userEmailaddress);
        ResultSet rsUserCheck = checkUser.executeQuery();

        // If the ResultSet contains a row, a user with the given name already exists.
        if (rsUserCheck.isBeforeFirst()) {
            return false;
        }

        PreparedStatement storeUser = databaseConnection.prepareStatement(
            "INSERT INTO Users (UserName, EmailAddress, AccountCreation) VALUES (?, ?, CURRENT_TIME());");
        storeUser.setString(1, userName);
        storeUser.setString(2, userEmailaddress);

        storeUser.executeUpdate();

        return true;
    }

    /**
     * Stores a record of a played game in the database.
     * 
     * @param players The email addresses uniquely identifying the players. AI players should have address "EuchreBot"
     * @param scores The scores at the end of the game 
     * @param startTime The time the game started
     * @param endTime The time the game finished
     * @throws SQLException Throws a sql exception if a connection to the database
     *                      cannot be created or if there is an error with the sql
     *                      query.
     */
    public  void storeGameRecord(String[] players, int[] scores, Timestamp startTime, Timestamp endTime)
            throws SQLException {
        if (!createConnection()) {
            throw new SQLException("Unable to create database connection.");
        }

        PreparedStatement getUserFKs = databaseConnection.prepareStatement(
                "select u1.UserUID, u2.UserUID, u3.UserUID, u4.UserUID FROM Users as u1, Users as u2, Users as u3, Users as u4 "
                        + "WHERE u1.EmailAddress = ? AND u2.EmailAddress = ? AND u3.EmailAddress = ? AND u4.EmailAddress = ?");
        for (int i = 0; i < 4; i++) {
            getUserFKs.setString(i + 1, players[i]);
        }
        ResultSet rsGetUserFKs = getUserFKs.executeQuery();

        rsGetUserFKs.next();

        PreparedStatement storeGame = databaseConnection.prepareStatement("INSERT INTO Games "
                + "(Player1, Player2, Player3, Player4, GameStartTime, GameEndTime, Team1Score, Team2Score) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        for (int i = 0; i < 4; i++) {
            storeGame.setInt(i + 1, rsGetUserFKs.getInt(i + 1));
        }
        storeGame.setTimestamp(5, startTime);
        storeGame.setTimestamp(6, endTime);
        storeGame.setInt(7, scores[0]);
        storeGame.setInt(8, scores[1]);

        storeGame.executeUpdate();

        return;
    }

    /**
     * Gets all game records in the SQL database where the user with the given user
     * UID was a player.
     * 
     * @param userUID The unique identifier in the MySQL database of the user.
     *                Should be retrieved from the User object.
     * @return An ArrayList of GameRecord objects consisting of all games where the
     *         user was a player that are recorded in the database.
     * @throws SQLException Throws an exception if a connection to the database
     *                      cannot be made or if there is an error in the sql query.
     */
    public ArrayList<GameRecord> getGameRecords(int userUID) throws SQLException {

        if (!createConnection()) {
            throw new SQLException("Unable to create database connection.");
        }

        ArrayList<GameRecord> gameRecords = new ArrayList<GameRecord>();

        PreparedStatement getRecords = databaseConnection.prepareStatement(
        "select GameUID, GameStartTime, GameEndTime, u1.UserName, u2.UserName, u3.UserName, u4.UserName, Team1Score, Team2Score"
        + " FROM Games left join Users AS u1 on Games.Player1=u1.UserUID"
        + " left join Users AS u2 on Games.Player2=u1.UserUID"
        + " left join Users AS u3 on Games.Player3=u3.UserUID"
        + " left join Users AS u4 on Games.Player4=u4.UserUID"
        + " WHERE Player1=? OR Player2=? OR Player3=? OR Player4=?;");
        for(int i=1; i<5 ;i++){
            getRecords.setInt(i, userUID);
        }
        ResultSet rsGameRecords = getRecords.executeQuery();
                

        String[] players;
        int[] scores;

        while (rsGameRecords.next()) {
            players = new String[4];
            scores = new int[2];

            for (int i = 0; i < 4; i++) {
                players[i] = rsGameRecords.getString(i + 4);
            }
            scores[0] = rsGameRecords.getInt(8);
            scores[1] = rsGameRecords.getInt(9);

            gameRecords.add(new GameRecord(rsGameRecords.getInt(1), rsGameRecords.getTimestamp(2), rsGameRecords.getTimestamp(3), players, scores));
        }

        return gameRecords;
    }

}
