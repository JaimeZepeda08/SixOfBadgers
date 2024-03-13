package com.cs506group12.backend.controllers;

import com.cs506group12.backend.frontendToBackend.FBUser;
import com.cs506group12.backend.models.GameRecord;
import com.cs506group12.backend.models.User;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.cs506group12.backend.DatabaseConnection.*;

@RestController
public class PlayerController {
    @PostMapping("/player/get")
    public User getPlayer(@RequestBody FBUser user) throws SQLException {
        return getUser(user.userName, user.password);
    }

    @PostMapping("/player/save")
    public void saveUser(@RequestBody FBUser user) throws SQLException {
        System.out.println("username: " + user.userName + "\npassword: " + user.password);
        storeUser(user.userName, user.password);
    }
}
