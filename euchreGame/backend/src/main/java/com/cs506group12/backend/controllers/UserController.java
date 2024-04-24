package com.cs506group12.backend.controllers;

import com.cs506group12.backend.DatabaseConnection;
import com.cs506group12.backend.models.GameRecord;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.sql.DriverManager;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cs506group12.backend.models.User;
import com.cs506group12.backend.DatabaseConnection.*;

import javax.sql.DataSource;

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
        source.setURL("jdbc:mysql://localhost:5306/euchre");
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
        Boolean result = connection.storeUser(name, email);
        if(result) {
            System.out.println("User saved successfully");
        } else {
            System.out.println("User aleady exists in the database");
        }
    }

    /**
     * Retrieves all the saved games associated with a user
     * @param user The user to retrieve the saved games for
     * @return An ArrayList of GameRecord objects representing the saved games
     * @throws SQLException
     */
    @PostMapping("/games")
    public ArrayList<GameRecord> getSavedGames(@RequestBody User user) throws SQLException {
        String email = user.getEmail();
        String name = user.getName();
        System.out.println("User: " + name + "\nEmail: " + email);
        User temp = connection.getUser(name, email);
        return connection.getGameRecords(temp.getUserUID());
    }
}