package com.cs506group12.backend.controllers;

import com.cs506group12.backend.models.GameRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class SavedGamesController {
    @GetMapping("/getGameRecords")
    public ArrayList<GameRecord> getRecords(@RequestBody String userName) throws SQLException {
        return null;
    }

    @PostMapping("/saveGameRecord")
    public void postRecord() {

    }
}
