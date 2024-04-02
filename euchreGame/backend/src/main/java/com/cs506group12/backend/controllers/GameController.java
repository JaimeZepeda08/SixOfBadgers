package com.cs506group12.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import com.cs506group12.backend.models.Card;

@RestController
public class GameController {

    @GetMapping("/getHand")
    public ArrayList<Card> getHand() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.SUIT.CLUBS, 11));
        cards.add(new Card(Card.SUIT.DIAMONDS, 12));
        cards.add(new Card(Card.SUIT.HEARTS, 9));
        cards.add(new Card(Card.SUIT.HEARTS, 14));
        cards.add(new Card(Card.SUIT.SPADES, 10));

        return cards;
    }
}