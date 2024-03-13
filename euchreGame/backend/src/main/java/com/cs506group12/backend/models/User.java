package com.cs506group12.backend.models;

import org.springframework.boot.autoconfigure.domain.EntityScan;

/*
 * User object to model an authenticated user logged in to the system.
 * Stores settings as an array of Strings. When settings are implemented, individual methods
 * should be created to access each setting rather than accessing the entire array.
 */

public class User {
    
    int userUID;
    String userName;
    String[] settings;

    public User(int userUID, String userName, String[] settings){
        this.userUID = userUID;
        this.userName = userName;
        this.settings = settings;
    }

    public String getName(){
        return this.userName;
    }
    

}
