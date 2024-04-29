package com.cs506group12.backend.controllers;

import com.cs506group12.backend.DatabaseConnection;
import com.cs506group12.backend.models.GameRecord;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.cs506group12.backend.models.User;

/**
 * Controller class for handling User information with database
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private DatabaseConnection connection;
    private MysqlDataSource source;

    @Autowired
    public UserController(HttpServletResponse httpServletResponse){
        source = new MysqlDataSource();
        source.setURL("jdbc:mysql://database:3306/euchre");
        source.setUser("root");
        source.setPassword("supersecure");
        connection = new DatabaseConnection(source);
    }

    /**
     * Saves the user to the database with the given information
     */
    @PostMapping("/save")
    public void saveUser(@RequestBody User user) throws SQLException {
        String email = user.getEmail();
        String name = user.getName();
        System.out.println("User: " + name + "\nEmail: " + email);
        if(!name.equals("guestUser")) {
            Boolean result = connection.storeUser(name, email);
            if(result) {
                System.out.println("User saved successfully");
            } else {
                System.out.println("User already exists in the database");
            }
        }
    }

    /**
     * Retrieves all the saved games associated with a user. Helper method for getSavedGames
     * @param user The user to retrieve the saved games for
     * @return An ArrayList of GameRecord objects representing the saved games as stored in the database
     * @throws SQLException If there is an error retrieving the saved games
     */
    public ArrayList<GameRecord> getGameRecords(User user) throws SQLException {
        String email = user.getEmail();
        String name = user.getName();
        System.out.println("User: " + name + "\nEmail: " + email);
        ArrayList<GameRecord> records = new ArrayList<>();
        if(name.equals("guest")) {
            name = "guestUser";
            Timestamp time = new Timestamp(System.currentTimeMillis());
            records.add(new GameRecord(-1, time, time, null, null));
        }
        User temp = connection.getUser(name, email);
        System.out.println("User: " + temp.getName() + "\nEmail: " + temp.getEmail() + "\nUID: " + temp.getUserUID());
        records = connection.getGameRecords(temp.getUserUID());
        return records;
    }

    /**
     * Retrieves all the saved games associated with a user and returns them in the right format for the frontend
     * @param user - object passed from frontend containing name and email
     * @return ArrayList of GameRecord objects representing the saved games for frontend
     * @throws SQLException
     */
    @PostMapping("/games/reports")
    public ArrayList<GameRecord> getSavedGames(@RequestBody User user) throws SQLException {
        ArrayList<GameRecord> records = getGameRecords(user); // grabs backend formatted records
        if(!records.isEmpty() && records.get(0).getGameUID() == -1) {
            System.out.println("Guest user doesn't have saved games");
            return records;
        }
        ArrayList<GameRecord> newRecords = new ArrayList<>();
        records.forEach(record -> { // Reformats each record to be suitable for frontend consumption
            int myTeamNum = record.getTeamNumber(user.getName());
            int enemyTeamNum = (myTeamNum + 1) % 2;
            String[] myTeam = record.getTeam(myTeamNum);
            String[] enemyTeam = record.getTeam(enemyTeamNum);
            String[] players = new String[]{myTeam[0], myTeam[1], enemyTeam[0], enemyTeam[1]}; // [P1, P2, P3, P4]
            int[] scores = new int[]{record.getScore(myTeamNum), record.getScore(enemyTeamNum)}; // [MyScore, EnemyScore]
            GameRecord newRecord = new GameRecord(record.getGameUID(), record.getStartTime(), record.getEndTime(), players, scores);
            newRecords.add(newRecord);
        });

        return newRecords;
    }
}