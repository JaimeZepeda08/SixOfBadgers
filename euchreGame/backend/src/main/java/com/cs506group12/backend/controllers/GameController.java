package com.cs506group12.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import com.cs506group12.backend.models.Card;

/**
 * Controller class for handling game-related requests.
 */
@RestController
public class GameController {

    /**
     * Retrieves the player's hand.
     *
     * @return An ArrayList containing Card objects representing the player's hand.
     */
    @GetMapping("/getHand")
    public ArrayList<Card> getHand() {
        // Create an ArrayList to hold Card objects
        ArrayList<Card> cards = new ArrayList<>();

        // Add sample dummy Card objects representing a player's hand
        cards.add(new Card(Card.SUIT.CLUBS, 11)); // Clubs, Jack
        cards.add(new Card(Card.SUIT.DIAMONDS, 12)); // Diamonds, Queen
        cards.add(new Card(Card.SUIT.HEARTS, 9)); // Hearts, 9
        cards.add(new Card(Card.SUIT.HEARTS, 14)); // Hearts, Ace
        cards.add(new Card(Card.SUIT.SPADES, 10)); // Spades, 10

        return cards;
    }
}
