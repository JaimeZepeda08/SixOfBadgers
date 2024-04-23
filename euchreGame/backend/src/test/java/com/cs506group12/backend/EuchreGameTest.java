// package com.cs506group12.backend;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.Mockito.*;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import com.cs506group12.backend.models.Card;
// import com.cs506group12.backend.models.EuchreGame;
// import com.cs506group12.backend.models.Player;

// import ch.qos.logback.core.net.server.Client;

// public class EuchreGameTest {

//     @Mock
//     private static Player client1;
//     @Mock
//     private static Player client2;
//     @Mock
//     private static Player client3;
//     @Mock
//     private static Player client4;

//     private static EuchreGame euchreGame;

//     @BeforeAll
//     public static void setUp() {
//         when(client1.getHand()).thenReturn(new ArrayList<Card>());
//         when(client2.getHand()).thenReturn(new ArrayList<Card>());
//         when(client3.getHand()).thenReturn(new ArrayList<Card>());
//         when(client4.getHand()).thenReturn(new ArrayList<Card>());

//         euchreGame = new EuchreGame(client1);
//         euchreGame.addClient(client2);
//         euchreGame.addClient(client3);
//         euchreGame.addClient(client4);
//     }

//     @Test
//     public void testInitializeDeck() {
//         EuchreGame.initializeDeck();
//         assertNotNull(EuchreGame.deck);
//         assertEquals(24, EuchreGame.deck.size());
//     }

//     @Test
//     public void testStartGame() {
//         assertTrue(euchreGame.startGame());
//         assertNotNull(euchreGame.getPlayers());
//         assertEquals(4, euchreGame.getPlayers().size());
//     }

//     @Test
//     public void testDealCards() {
//         EuchreGame.initializeDeck();
//         euchreGame.dealCards();
//         assertNotNull(client1.getHand());
//         assertEquals(5, client1.getHand().size());
//         assertEquals(5, client2.getHand().size());
//         assertEquals(5, client3.getHand().size());
//         assertEquals(5, client4.getHand().size());
//         assertEquals(9, euchreGame.getCardsLeft());
//     }

//     @Test
//     public void testGetPlayers() {
//         List<Player> players = euchreGame.getPlayers();
//         assertNotNull(players);
//         assertEquals(4, players.size());
//         assertTrue(players.contains(client1));
//         assertTrue(players.contains(client2));
//         assertTrue(players.contains(client3));
//         assertTrue(players.contains(client4));
//     }

//     @Test
//     public void testHandleTrick() {
//         // Mock the player hands
//         ArrayList<Card> hand1 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.CLUBS, 10),
//                 new Card(Card.SUIT.CLUBS, 11), new Card(Card.SUIT.DIAMONDS, 9), new Card(Card.SUIT.SPADES, 12)));
//         ArrayList<Card> hand2 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.HEARTS, 8),
//                 new Card(Card.SUIT.CLUBS, 12), new Card(Card.SUIT.DIAMONDS, 10), new Card(Card.SUIT.SPADES, 9)));
//         ArrayList<Card> hand3 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.HEARTS, 10),
//                 new Card(Card.SUIT.DIAMONDS, 8), new Card(Card.SUIT.DIAMONDS, 11), new Card(Card.SUIT.SPADES, 10)));
//         ArrayList<Card> hand4 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.HEARTS, 9),
//                 new Card(Card.SUIT.CLUBS, 8), new Card(Card.SUIT.DIAMONDS, 12), new Card(Card.SUIT.SPADES, 11)));

//         when(client1.getHand()).thenReturn(hand1);
//         when(client2.getHand()).thenReturn(hand2);
//         when(client3.getHand()).thenReturn(hand3);
//         when(client4.getHand()).thenReturn(hand4);

//         // Simulate playing a trick
//         euchreGame.handleTrick();

//         // Assert that one card is removed from each player's hand
//         verify(client1, times(1)).playAndRemoveCard(any(Card.class));
//         verify(client2, times(1)).playAndRemoveCard(any(Card.class));
//         verify(client3, times(1)).playAndRemoveCard(any(Card.class));
//         verify(client4, times(1)).playAndRemoveCard(any(Card.class));
//     }

//     @Test
//     public void testHandleTrickComplex() {
//         // Mock the player hands
//         ArrayList<Card> hand1 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.CLUBS, 10),
//                 new Card(Card.SUIT.CLUBS, 11), new Card(Card.SUIT.DIAMONDS, 9), new Card(Card.SUIT.SPADES, 12)));
//         ArrayList<Card> hand2 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.HEARTS, 8),
//                 new Card(Card.SUIT.CLUBS, 12), new Card(Card.SUIT.DIAMONDS, 10), new Card(Card.SUIT.SPADES, 9)));
//         ArrayList<Card> hand3 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.HEARTS, 10),
//                 new Card(Card.SUIT.DIAMONDS, 8), new Card(Card.SUIT.DIAMONDS, 11), new Card(Card.SUIT.SPADES, 10)));
//         ArrayList<Card> hand4 = new ArrayList<>(Arrays.asList(new Card(Card.SUIT.HEARTS, 9),
//                 new Card(Card.SUIT.CLUBS, 8), new Card(Card.SUIT.DIAMONDS, 12), new Card(Card.SUIT.SPADES, 11)));

//         when(client1.getHand()).thenReturn(hand1);
//         when(client2.getHand()).thenReturn(hand2);
//         when(client3.getHand()).thenReturn(hand3);
//         when(client4.getHand()).thenReturn(hand4);

//         // Simulate playing a trick
//         euchreGame.handleTrick();

//         // Assert that one card is removed from each player's hand
//         verify(client1, times(1)).playAndRemoveCard(any(Card.class));
//         verify(client2, times(1)).playAndRemoveCard(any(Card.class));
//         verify(client3, times(1)).playAndRemoveCard(any(Card.class));
//         verify(client4, times(1)).playAndRemoveCard(any(Card.class));
//     }

//     @Test
//     public void testDetermineTurnWinner() {
//         // Mock the number of tricks won by each team
//         euchreGame.setNumTricks(new int[]{2, 3}); // Team 1 wins 2 tricks, Team 2 wins 3 tricks

//         // Assert that Team 2 is determined as the turn winner
//         assertEquals(1, euchreGame.determineTurnWinner());
//     }
// }

//TODO Uncomment
    
// package com.cs506group12.backend;

// import static org.junit.jupiter.api.Assertions.assertArrayEquals;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.ArrayList;

// import org.junit.jupiter.api.Test;

// import com.cs506group12.backend.models.Card;
// import com.cs506group12.backend.models.EuchreGame;
// import com.cs506group12.backend.models.Player;

// /**
// * Tests the euchreGame class
// *
// * @author Kaldan Kopp
// */
// public class testEuchreGame {
// /**
// * Tests the constructor.
// */
// @Test
// public void testEuchreGameConstructor() {
// EuchreGame game = new EuchreGame();
// assertNotNull(game);

// }

/**
* tests the score method
*/


// /**
// * tests that other variables are correctly set after trick end
// */
// @Test
// public void testHandleTrick() {
// ArrayList<Card> testPlayedCards = new ArrayList<Card>();
// testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 12));
// testPlayedCards.add(new Card(Card.SUIT.CLUBS, 11));
// testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 13));
// testPlayedCards.add(new Card(Card.SUIT.HEARTS, 10));

// EuchreGame game = new EuchreGame();
// game.setAttackingTeam(0);
// game.setTrumpSuitCard(new Card(Card.SUIT.CLUBS, 0));
// game.setPlayedCards(testPlayedCards);

// assertEquals(1, game.score(testPlayedCards));
// // game.handleTrick();
// assertEquals(4, game.getPlayedCards().size()); // played cards is emptied
// after trick

// // leading player starts at 1
// assertEquals(1, game.getLeadingPlayer()); // tests that the leading player
// for the next round is equal to the
// // one who won the last trick

// assertEquals(0, game.getNumTricks()[1]); // test trick winner is correctly
// assigned
// assertEquals(0, game.getNumTricks()[0]);

// }

// public void testGettersAndSetters() {
// EuchreGame euchreGame = new EuchreGame();
// // Test getters and setters for team scores
// euchreGame.setTeamOneScore(5);
// assertEquals(5, euchreGame.getTeamOneScore());
// euchreGame.setTeamTwoScore(7);
// assertEquals(7, euchreGame.getTeamTwoScore());

// // Test getters and setters for points threshold
// euchreGame.setPointsThreshold(15);
// assertEquals(15, euchreGame.getPointsThreshold());

// // Test getters and setters for dealer position
// euchreGame.setDealer(2);
// assertEquals(2, euchreGame.getDealer());

// // Test getters and setters for leading player
// euchreGame.setLeadingPlayer(3);
// assertEquals(3, euchreGame.getLeadingPlayer());

// // Test getters and setters for face up card
// Card faceUpCard = new Card(Card.SUIT.HEARTS, 10);
// euchreGame.setFaceUpCard(faceUpCard);
// assertEquals(faceUpCard, euchreGame.getFaceUpCard());

// // Test getters and setters for team that won trick
// euchreGame.setTeamThatWonTrick(1);
// assertEquals(1, euchreGame.getTeamThatWonTrick());

// // Test getters and setters for team that won turn
// euchreGame.setTeamThatWonTurn(0);
// assertEquals(0, euchreGame.getTeamThatWonTurn());

// // Test getters and setters for attacking team
// euchreGame.setAttackingTeam(1);
// assertEquals(1, euchreGame.getAttackingTeam());

// // Test getters and setters for team overall scores
// euchreGame.setTeamOverallScores(new int[] { 20, 25 });
// assertArrayEquals(new int[] { 20, 25 }, euchreGame.getTeamOverallScores());

// // Test getters and setters for number of tricks
// euchreGame.setNumTricks(new int[] { 3, 2 });
// assertArrayEquals(new int[] { 3, 2 }, euchreGame.getNumTricks());

// // Test getters and setters for played cards
// ArrayList<Card> playedCards = new ArrayList<>();
// playedCards.add(new Card(Card.SUIT.CLUBS, 9));
// playedCards.add(new Card(Card.SUIT.DIAMONDS, 10));
// euchreGame.setPlayedCards(playedCards);
// assertEquals(playedCards, euchreGame.getPlayedCards());

// // Test getters and setters for solo player flag
// euchreGame.setSoloPlayer(true);
// assertTrue(euchreGame.isSoloPlayer());

// // Test getters and setters for trump suit card
// Card trumpSuitCard = new Card(Card.SUIT.HEARTS, 11);
// euchreGame.setTrumpSuitCard(trumpSuitCard);
// assertEquals(trumpSuitCard, euchreGame.getTrumpSuitCard());
// }

// @Test
// public void testHandlePointsSpecialCases() {
// EuchreGame euchreGame = new EuchreGame();
// // Test case 1: Defenders win
// euchreGame.setAttackingTeam(0);
// assertEquals(2, euchreGame.handlePoints(1));

// // Test case 2: Solo player wins 5 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setSoloPlayer(true);
// euchreGame.setNumTricks(new int[] { 5, 0 });
// assertEquals(4, euchreGame.handlePoints(0));

// // Test case 3: Solo player wins 3 or 4 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setSoloPlayer(true);
// euchreGame.setNumTricks(new int[] { 3, 0 });
// assertEquals(1, euchreGame.handlePoints(0));

// // Test case 4: Attacking team wins 3 or 4 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setNumTricks(new int[] { 3, 0 });
// assertEquals(1, euchreGame.handlePoints(0));

// // Test case 5: Attacking team wins 5 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setNumTricks(new int[] { 5, 0 });
// assertEquals(4, euchreGame.handlePoints(0));

// // Test case 6: No specific condition met
// euchreGame.setNumTricks(new int[] { 2, 3 });
// assertEquals(2, euchreGame.handlePoints(1));

// /*
// * // Test case 7: Defenders win but solo player is active
// * euchreGame.setAttackingTeam(1);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {3, 2});
// * assertEquals(2, euchreGame.handlePoints(1));
// *
// * // Test case 8: Attacking team wins with solo player active and winning 5
// * tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {5, 0});
// * assertEquals(2, euchreGame.handlePoints(0));
// *
// * // Test case 9: Attacking team wins with solo player active and winning 3
// or
// * 4 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {4, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 10: Attacking team wins with solo player active and not
// winning
// * 5 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {3, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 11: Attacking team wins with solo player not active
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {4, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 12: Attacking team wins with solo player not active and
// winning
// * 5 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {5, 0});
// * assertEquals(2, euchreGame.handlePoints(0));
// *
// * // Test case 13: Attacking team wins with solo player not active and
// winning
// * 3 or 4 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {3, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 14: Attacking team wins with solo player not active and not
// * winning 5 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {2, 2});
// * assertEquals(1, euchreGame.handlePoints(0));
// */
// }

// @Test
// public void testInitializeDeck() {
// EuchreGame euchreGame = new EuchreGame();
// euchreGame.initializeDeck();
// ArrayList<Card> deck = euchreGame.deck;
// assertNotNull(deck);
// assertEquals(24, deck.size());

// // Verify the deck contains cards of different suits and ranks
// boolean hasClubs = false, hasDiamonds = false, hasHearts = false, hasSpades =
// false;
// for (Card card : deck) {
// switch (card.getSuit()) {
// case CLUBS:
// hasClubs = true;
// break;
// case DIAMONDS:
// hasDiamonds = true;
// break;
// case HEARTS:
// hasHearts = true;
// break;
// case SPADES:
// hasSpades = true;
// break;
// }
// assertTrue(card.getValue() >= 9 && card.getValue() <= 14); // Ranks between 9
// and 14 (inclusive)
// }
// assertTrue(hasClubs && hasDiamonds && hasHearts && hasSpades); // Ensure all
// suits are present
// }

// // game loop is currently untested - will add once more finctionality

// package com.cs506group12.backend;

// import static org.junit.jupiter.api.Assertions.assertArrayEquals;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.ArrayList;

// import org.junit.jupiter.api.Test;

// import com.cs506group12.backend.models.Card;
// import com.cs506group12.backend.models.EuchreGame;
// import com.cs506group12.backend.models.Player;

// /**
// * Tests the euchreGame class
// *
// * @author Kaldan Kopp
// */
// public class testEuchreGame {
// /**
// * Tests the constructor.
// */
// @Test
// public void testEuchreGameConstructor() {
// EuchreGame game = new EuchreGame();
// assertNotNull(game);

// }

// /**
// * tests the score method
// */
// @Test
// public void testEuchreGameScore() {
// ArrayList<Card> testPlayedCards = new ArrayList<Card>();
// testPlayedCards.add(new Card(Card.SUIT.CLUBS, 14));
// testPlayedCards.add(new Card(Card.SUIT.CLUBS, 11));
// testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 12));

// EuchreGame game = new EuchreGame();
// game.setTrumpSuitCard(new Card(Card.SUIT.DIAMONDS, 10));
// game.setPlayedCards(testPlayedCards);
// assertEquals(2, game.score(testPlayedCards)); // tests if lower card with
// trump beats higher card without trump
// game.setTrumpSuitCard(new Card(Card.SUIT.CLUBS, 10));
// assertEquals(1, game.score(testPlayedCards)); // tests if trump jack beats
// higher value of same suit
// game.setTrumpSuitCard(new Card(Card.SUIT.HEARTS, 0));
// assertEquals(0, game.score(testPlayedCards)); // tests highest card wins when
// no trump card played

// testPlayedCards.add(new Card(Card.SUIT.HEARTS, 14));
// testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 11)); // tests if same color
// jack is second highest card
// assertEquals(4, game.score(testPlayedCards));

// testPlayedCards.add(new Card(Card.SUIT.HEARTS, 11)); // jack of trump suit
// higher
// assertEquals(5, game.score(testPlayedCards));
// }

// /**
// * tests is points correctly assigned after turn end
// */
// @Test
// public void testHandlePoints() {
// EuchreGame game = new EuchreGame();
// game.setAttackingTeam(1);
// assertEquals(2, game.handlePoints(0)); // defending team wins

// int[] testNumTricks = { 2, 3 };
// game.setSoloPlayer(false);
// game.setNumTricks(testNumTricks);
// assertEquals(1, game.handlePoints(1)); // attacking team wins less than 5
// tricks
// testNumTricks[0] = 1;
// testNumTricks[1] = 4;
// game.setNumTricks(testNumTricks);
// assertEquals(1, game.handlePoints(1)); // attacking team wins less than 5
// tricks

// testNumTricks[0] = 0;
// testNumTricks[1] = 5;
// game.setNumTricks(testNumTricks);
// assertEquals(2, game.handlePoints(1)); // attacking team wins less than 5
// tricks

// game.setSoloPlayer(true);
// assertEquals(4, game.handlePoints(1)); // solo player wins 5 tricks

// }

// /**
// * tests that other variables are correctly set after trick end
// */
// @Test
// public void testHandleTrick() {
// ArrayList<Card> testPlayedCards = new ArrayList<Card>();
// testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 12));
// testPlayedCards.add(new Card(Card.SUIT.CLUBS, 11));
// testPlayedCards.add(new Card(Card.SUIT.DIAMONDS, 13));
// testPlayedCards.add(new Card(Card.SUIT.HEARTS, 10));

// EuchreGame game = new EuchreGame();
// game.setAttackingTeam(0);
// game.setTrumpSuitCard(new Card(Card.SUIT.CLUBS, 0));
// game.setPlayedCards(testPlayedCards);

// assertEquals(1, game.score(testPlayedCards));
// // game.handleTrick();
// assertEquals(4, game.getPlayedCards().size()); // played cards is emptied
// after trick

// // leading player starts at 1
// assertEquals(1, game.getLeadingPlayer()); // tests that the leading player
// for the next round is equal to the
// // one who won the last trick

// assertEquals(0, game.getNumTricks()[1]); // test trick winner is correctly
// assigned
// assertEquals(0, game.getNumTricks()[0]);

// }

// public void testGettersAndSetters() {
// EuchreGame euchreGame = new EuchreGame();
// // Test getters and setters for team scores
// euchreGame.setTeamOneScore(5);
// assertEquals(5, euchreGame.getTeamOneScore());
// euchreGame.setTeamTwoScore(7);
// assertEquals(7, euchreGame.getTeamTwoScore());

// // Test getters and setters for points threshold
// euchreGame.setPointsThreshold(15);
// assertEquals(15, euchreGame.getPointsThreshold());

// // Test getters and setters for dealer position
// euchreGame.setDealer(2);
// assertEquals(2, euchreGame.getDealer());

// // Test getters and setters for leading player
// euchreGame.setLeadingPlayer(3);
// assertEquals(3, euchreGame.getLeadingPlayer());

// // Test getters and setters for face up card
// Card faceUpCard = new Card(Card.SUIT.HEARTS, 10);
// euchreGame.setFaceUpCard(faceUpCard);
// assertEquals(faceUpCard, euchreGame.getFaceUpCard());

// // Test getters and setters for team that won trick
// euchreGame.setTeamThatWonTrick(1);
// assertEquals(1, euchreGame.getTeamThatWonTrick());

// // Test getters and setters for team that won turn
// euchreGame.setTeamThatWonTurn(0);
// assertEquals(0, euchreGame.getTeamThatWonTurn());

// // Test getters and setters for attacking team
// euchreGame.setAttackingTeam(1);
// assertEquals(1, euchreGame.getAttackingTeam());

// // Test getters and setters for team overall scores
// euchreGame.setTeamOverallScores(new int[] { 20, 25 });
// assertArrayEquals(new int[] { 20, 25 }, euchreGame.getTeamOverallScores());

// // Test getters and setters for number of tricks
// euchreGame.setNumTricks(new int[] { 3, 2 });
// assertArrayEquals(new int[] { 3, 2 }, euchreGame.getNumTricks());

// // Test getters and setters for played cards
// ArrayList<Card> playedCards = new ArrayList<>();
// playedCards.add(new Card(Card.SUIT.CLUBS, 9));
// playedCards.add(new Card(Card.SUIT.DIAMONDS, 10));
// euchreGame.setPlayedCards(playedCards);
// assertEquals(playedCards, euchreGame.getPlayedCards());

// // Test getters and setters for solo player flag
// euchreGame.setSoloPlayer(true);
// assertTrue(euchreGame.isSoloPlayer());

// // Test getters and setters for trump suit card
// Card trumpSuitCard = new Card(Card.SUIT.HEARTS, 11);
// euchreGame.setTrumpSuitCard(trumpSuitCard);
// assertEquals(trumpSuitCard, euchreGame.getTrumpSuitCard());
// }

// @Test
// public void testHandlePointsSpecialCases() {
// EuchreGame euchreGame = new EuchreGame();
// // Test case 1: Defenders win
// euchreGame.setAttackingTeam(0);
// assertEquals(2, euchreGame.handlePoints(1));

// // Test case 2: Solo player wins 5 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setSoloPlayer(true);
// euchreGame.setNumTricks(new int[] { 5, 0 });
// assertEquals(4, euchreGame.handlePoints(0));

// // Test case 3: Solo player wins 3 or 4 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setSoloPlayer(true);
// euchreGame.setNumTricks(new int[] { 3, 0 });
// assertEquals(1, euchreGame.handlePoints(0));

// // Test case 4: Attacking team wins 3 or 4 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setNumTricks(new int[] { 3, 0 });
// assertEquals(1, euchreGame.handlePoints(0));

// // Test case 5: Attacking team wins 5 tricks
// euchreGame.setAttackingTeam(0);
// euchreGame.setNumTricks(new int[] { 5, 0 });
// assertEquals(4, euchreGame.handlePoints(0));

// // Test case 6: No specific condition met
// euchreGame.setNumTricks(new int[] { 2, 3 });
// assertEquals(2, euchreGame.handlePoints(1));

// /*
// * // Test case 7: Defenders win but solo player is active
// * euchreGame.setAttackingTeam(1);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {3, 2});
// * assertEquals(2, euchreGame.handlePoints(1));
// *
// * // Test case 8: Attacking team wins with solo player active and winning 5
// * tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {5, 0});
// * assertEquals(2, euchreGame.handlePoints(0));
// *
// * // Test case 9: Attacking team wins with solo player active and winning 3
// or
// * 4 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {4, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 10: Attacking team wins with solo player active and not
// winning
// * 5 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(true);
// * euchreGame.setNumTricks(new int[] {3, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 11: Attacking team wins with solo player not active
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {4, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 12: Attacking team wins with solo player not active and
// winning
// * 5 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {5, 0});
// * assertEquals(2, euchreGame.handlePoints(0));
// *
// * // Test case 13: Attacking team wins with solo player not active and
// winning
// * 3 or 4 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {3, 1});
// * assertEquals(1, euchreGame.handlePoints(0));
// *
// * // Test case 14: Attacking team wins with solo player not active and not
// * winning 5 tricks
// * euchreGame.setAttackingTeam(0);
// * euchreGame.setIsSoloPlayer(false);
// * euchreGame.setNumTricks(new int[] {2, 2});
// * assertEquals(1, euchreGame.handlePoints(0));
// */
// }

// @Test
// public void testInitializeDeck() {
// EuchreGame euchreGame = new EuchreGame();
// euchreGame.initializeDeck();
// ArrayList<Card> deck = euchreGame.deck;
// assertNotNull(deck);
// assertEquals(24, deck.size());

// // Verify the deck contains cards of different suits and ranks
// boolean hasClubs = false, hasDiamonds = false, hasHearts = false, hasSpades =
// false;
// for (Card card : deck) {
// switch (card.getSuit()) {
// case CLUBS:
// hasClubs = true;
// break;
// case DIAMONDS:
// hasDiamonds = true;
// break;
// case HEARTS:
// hasHearts = true;
// break;
// case SPADES:
// hasSpades = true;
// break;
// }
// assertTrue(card.getValue() >= 9 && card.getValue() <= 14); // Ranks between 9
// and 14 (inclusive)
// }
// assertTrue(hasClubs && hasDiamonds && hasHearts && hasSpades); // Ensure all
// suits are present
// }

// // game loop is currently untested - will add once more finctionality

// }

