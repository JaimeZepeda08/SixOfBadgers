package euchreGame;

import java.util.*;

public class EuchreGame {
    private ArrayList<Card> deck;
    private List<Card>[] playerHands;
    private int cardsLeft;

    public EuchreGame() {
        initializeDeck();
        shuffleDeck();
        dealCards();
        cardsLeft = deck.size(); 
        
    }

    /**
     * Initializes the original deck of 24 cards
     */
    private void initializeDeck() {
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Shuffles the deck of cards to give to players
     */
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Deals out 4 cards to each person
     */
    private void dealCards() {
        playerHands = new List[4];
        for (int i = 0; i < 4; i++) {
            playerHands[i] = new ArrayList<>();
        }

        Iterator<Card> iterator = deck.iterator();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                playerHands[j].add(iterator.next());
            }
        }

        cardsLeft = deck.size() - 16;
    }


    /*
     * Getter method for playerHands
     */
    public List<Card>[] getPlayerHands() {
        return playerHands;
    }

    /*
     * Getter method for cards left in the deck
     */
    public int getCardsLeft() {
        return cardsLeft;
    }

    
    public static void main(String[] args) {
        EuchreGame game = new EuchreGame();
        List<Card>[] playerHands = game.getPlayerHands();

        for (int i = 0; i < 4; i++) {
            System.out.println("Player " + (i + 1) + "'s hand:");
            for (Card card : playerHands[i]) {
                System.out.println(card);
            }
            System.out.println();
        }

        System.out.println("Cards left in the deck: " + game.getCardsLeft());
    }
}
