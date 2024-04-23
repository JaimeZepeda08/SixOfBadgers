package com.cs506group12.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Player;

public class testPlayer {

    WebSocketSession session;
    Player player;

    @BeforeEach
    public void setUp() {
        session = mock(WebSocketSession.class);
        player = new Player(session);
    }

    @Test
    public void testPlayerInitialization() {
        assertNotNull(player.getHand());
        assertEquals(0, player.getHand().size());
        assertEquals(0, player.getPoints());
    }

    @Test
    public void testAddPoints() {
        player.addPoints(10);
        assertEquals(10, player.getPoints());
    }

    @Test
    public void testGetHighCardOfSuit() {
        Card card1 = mock(Card.class);
        when(card1.getSuit()).thenReturn(Card.SUIT.HEARTS);
        when(card1.getValue()).thenReturn(10);

        Card card2 = mock(Card.class);
        when(card2.getSuit()).thenReturn(Card.SUIT.HEARTS);
        when(card2.getValue()).thenReturn(8);

        Card card3 = mock(Card.class);
        when(card3.getSuit()).thenReturn(Card.SUIT.DIAMONDS);
        when(card3.getValue()).thenReturn(12);

        player.getHand().add(card1);
        player.getHand().add(card2);
        player.getHand().add(card3);

        assertEquals(card1, player.getHighCardOfSuit(Card.SUIT.HEARTS));
        assertNull(player.getHighCardOfSuit(Card.SUIT.SPADES));
    }

    @Test
    public void testGetLowCardOfSuit() {
        Card card1 = mock(Card.class);
        when(card1.getSuit()).thenReturn(Card.SUIT.HEARTS);
        when(card1.getValue()).thenReturn(10);

        Card card2 = mock(Card.class);
        when(card2.getSuit()).thenReturn(Card.SUIT.HEARTS);
        when(card2.getValue()).thenReturn(8);

        Card card3 = mock(Card.class);
        when(card3.getSuit()).thenReturn(Card.SUIT.DIAMONDS);
        when(card3.getValue()).thenReturn(12);

        player.getHand().add(card1);
        player.getHand().add(card2);
        player.getHand().add(card3);

        assertEquals(card2, player.getLowCardOfSuit(Card.SUIT.HEARTS));
        assertNull(player.getLowCardOfSuit(Card.SUIT.SPADES));
    }

    @Test
public void testSetAndGetHand() {
    // Create a list of cards
    ArrayList<Card> hand = new ArrayList<>();
    hand.add(new Card(Card.SUIT.HEARTS, 10));
    hand.add(new Card(Card.SUIT.SPADES, 7));

    // Set the hand for the player
    player.setHand(hand);

    // Verify that the hand is set correctly
    assertEquals(hand, player.getHand());
}

@Test
public void testPlayAndRemoveCard() {
    // Create cards for testing
    Card card1 = new Card(Card.SUIT.HEARTS, 10);
    Card card2 = new Card(Card.SUIT.SPADES, 7);

    // Add cards to player's hand
    player.getHand().add(card1);
    player.getHand().add(card2);

    // Play and remove a card
    Card playedCard = player.playAndRemoveCard(card1);

    // Verify that the played card is returned and removed from the hand
    assertEquals(card1, playedCard);
    assertFalse(player.getHand().contains(card1));
}

@Test
public void testGetSuit() {
    // Create cards for testing
    Card card1 = new Card(Card.SUIT.HEARTS, 10);
    Card card2 = new Card(Card.SUIT.HEARTS, 8);
    Card card3 = new Card(Card.SUIT.SPADES, 7);

    // Add cards to player's hand
    player.getHand().add(card1);
    player.getHand().add(card2);
    player.getHand().add(card3);

    // Get cards of hearts suit
    ArrayList<Card> heartsCards = player.getSuit(Card.SUIT.HEARTS);

    // Verify that only cards of hearts suit are returned
    assertTrue(heartsCards.contains(card1));
    assertTrue(heartsCards.contains(card2));
    assertFalse(heartsCards.contains(card3));
}

@Test
public void testHasSuitCard() {
    // Create cards for testing
    Card card1 = new Card(Card.SUIT.HEARTS, 10);
    Card card2 = new Card(Card.SUIT.SPADES, 7);

    // Add cards to player's hand
    player.getHand().add(card1);
    player.getHand().add(card2);

    // Verify that player has a card of hearts suit
    assertTrue(player.hasSuitCard(Card.SUIT.HEARTS));
    // Verify that player does not have a card of diamonds suit
    assertFalse(player.hasSuitCard(Card.SUIT.DIAMONDS));
}

@Test
public void testGetPlayableCards() {
    // Create cards for testing
    Card card1 = new Card(Card.SUIT.HEARTS, 10);
    Card card2 = new Card(Card.SUIT.SPADES, 7);
    Card card3 = new Card(Card.SUIT.CLUBS, 5);

    // Add cards to player's hand
    player.getHand().add(card1);
    player.getHand().add(card2);
    player.getHand().add(card3);

    // Test with leading suit as hearts and trump suit as diamonds
    boolean[] playableCards = player.getPlayableCards(Card.SUIT.HEARTS, Card.SUIT.DIAMONDS);

    // Verify that only hearts cards are playable
    assertTrue(playableCards[0]);
    assertFalse(playableCards[1]);
    assertFalse(playableCards[2]);
}

@Test
public void testChooseTrump() {
    // Create cards for testing
    Card card1 = new Card(Card.SUIT.HEARTS, 10);
    Card card2 = new Card(Card.SUIT.DIAMONDS, 7);
    Card card3 = new Card(Card.SUIT.DIAMONDS, 5);

    // Add cards to player's hand
    player.getHand().add(card1);
    player.getHand().add(card2);
    player.getHand().add(card3);

    // Test choosing trump suit
    //Comment out once implemented
    //assertEquals(Card.SUIT.DIAMONDS, player.chooseTrump());
}
    
}
