package com.cs506group12.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User object to model an authenticated user logged in to the system.
 * This differs from a player object, which is a representation of one of the
 * players in the game.
 * Stores settings as an array of Strings. When settings are implemented,
 * individual methods
 * should be created to access each setting rather than accessing the entire
 * array.
 */
public class User {

    int userUID;
    String userName;
    String email;

    /**
     * Creates a User object. Should only be called in the DatabaseConnection class
     * as it requires the UID of the
     * user from the database
     * 
     * @param userUID  The unique identifier of the user in the MySQL database
     * @param userName The name of the user
     * @param email A string representing the users email
     */
    @JsonCreator
    public User(@JsonProperty("userUID") int userUID, @JsonProperty("userName") String userName, @JsonProperty("email") String email) {
        this.userUID = userUID;
        this.userName = userName;
        this.email = email;
    }

    /**
     * Overloaded constructor, used because user settings are not yet implemented.
     *
     * @param userUID The unique identifier of the user in the MySQL database
     * @param userName The name of the user
     */
    public User(int userUID, String userName){
        this.userUID = userUID;
        this.userName = userName;
    }

    /**
     * Gets the username
     * 
     * @return User's name
     */
    public String getName() {
        return this.userName;
    }

    /**
     * Gets the user's UID. Used for methods connecting to the database to retrieve
     * linked records.
     * 
     * @return User's unique identifier
     */
    public int getUserUID() {
        return this.userUID;
    }

    /**
     * Gets the user's email. Used for methods connecting to the database to retrieve
     * user information
     *
     * @return User's email
     */
    public String getEmail() {
        return this.email;
    }
}
