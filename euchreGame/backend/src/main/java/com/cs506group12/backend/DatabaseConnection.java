package com.cs506group12.backend;

import java.sql.*;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.cs506group12.backend.models.*;

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

    public void storeGameState(GameState state) throws SQLException{
        
        if (!createConnection()) {
            throw new SQLException("Unable to create database connection.");
        }

        //Prepare SQL statement
        PreparedStatement storeGameState = databaseConnection.prepareStatement("INSERT INTO GameStates " + 
        "(GameStateUID, Player1, Player2, Player3, Player4, GameStartTime, GameSaveTime, Team1Score, Team2Score, "
        +"Team1Tricks, Team2Tricks, Player1Hand, Player2Hand, Player3Hand, Player4Hand, Dealer, LeadingPlayer, "
        + "Trump, CallingTeam, PlayerGoingAlone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE GameStateUID=?");

        //Add data to statement
        storeGameState.setString(1, state.getUUID());
        String[] players = state.getPlayerNames();
        for (int i=0; i<4; i++){
            storeGameState.setString(i + 2, players[i].toString());
        }
        storeGameState.setTimestamp(6, state.getStartTime());
        storeGameState.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
        storeGameState.setInt(8, state.getTeamScore(1));
        storeGameState.setInt(9, state.getTeamScore(2));
        storeGameState.setInt(10,state.getTeamTricks(1));
        storeGameState.setInt(11,state.getTeamTricks(2));
        for (int i=0; i<4; i++){
            storeGameState.setString(i + 12, state.getHandSQL(i));
        }
        storeGameState.setInt(16, state.getDealerPosition());
        storeGameState.setInt(17, state.getLeadingPlayerPosition());
        storeGameState.setString(18, Card.suitToString(state.getTrump()));
        storeGameState.setInt(19, state.getAttackingTeam());
        storeGameState.setInt(20, state.getPlayerGoingAlone());

        //Execute statement
        storeGameState.executeUpdate();

    }

    /**
     * 
     * @param gameStateUUID
     * @param session
     * @return
     * @throws SQLException
     */
    public GameState loadGameState(String gameStateUUID, GameSession session) throws SQLException{

        if (!createConnection()) {
            throw new SQLException("Unable to create database connection.");
        }

        Statement loadGameState = databaseConnection.createStatement();
        ResultSet rs = loadGameState.executeQuery(
        "select GameStateUID, u1.UserName, u2.UserName, u3.UserName, u4.UserName, GameStartTime, GameSaveTime, "
        + "Team1Score, Team2Score, Team1Tricks, Team2Tricks, Player1Hand, Player2Hand, Player3Hand, Player4Hand, "
        + "Dealer, LeadingPlayer, Trump, CallingTeam, PlayerAlone"
        + " FROM GameStates left join Users AS u1 on Games.Player1=u1.UserUID"
        + " left join Users AS u2 on Games.Player2=u1.UserUID"
        + " left join Users AS u3 on Games.Player3=u3.UserUID"
        + " left join Users AS u4 on Games.Player4=u4.UserUID"
        + " WHERE GameStateUID=" + gameStateUUID + ";");
        
        if(!rs.next()){
            throw new SQLException("No game found with the given identifier: " + gameStateUUID);
        }

        //Create 2D card array to represent player hands
        //Player hands are stored in the database as comma delimited strings up to 19 characters,
        //3 characters per card indicating value (first two) and suit (third).
        Hand[] playerHands = new Hand[4];
        String handString;
        String[] handStringSplit;

        for(int i=0; i<4; i++){

            handString = rs.getString(i+12);
            handStringSplit = handString.split(",");
            playerHands[i] = new Hand();

            for(int j=0; j<handStringSplit.length && j<5; j++){
                playerHands[i].addCard(Card.fromSqlString(handStringSplit[j]));
            }
        }

        //TODO: Need to get players to connect to restored state - check for AI players, and load 
        //emails of logged in players to match with connected sessions.
        //Create ArrayList of players
        String[] playerNames = new String[4];
        for(int i=0; i<4; i++){
            playerNames[i]= rs.getString(i+1);
        }


        //Load the information into the GameState object
        GameState loadedState = new GameState(gameStateUUID, playerHands, playerNames, rs.getInt(16), rs.getInt(17),
            rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11), Card.stringToSuit(rs.getString(18)),
            rs.getInt(19), rs.getInt(20));
        return loadedState;
        

    }
}
