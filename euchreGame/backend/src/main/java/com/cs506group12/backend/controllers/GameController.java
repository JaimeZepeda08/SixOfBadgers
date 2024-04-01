package com.cs506group12.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    // Simulated player's hand data
    private static final String playerHand = "Player hand data";

    @GetMapping("/getHand")
    public String getHand() {
        return playerHand;
    }
}
