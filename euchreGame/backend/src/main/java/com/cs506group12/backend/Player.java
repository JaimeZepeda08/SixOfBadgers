import java.util.*;
import com.cs506group12.backend.models.*;

public class Player {
	private ArrayList<Card> hand;
	public String usernName;
	private int points;
	// maybe a team function - i wpuld prefer for teammates to be 0 and 1 in array but maybe no possible?
	// do i need login functions?
	
	public void setHand(Arraylist<Card> hand) {
		this.hand = hand;
	}
	
	public ArrayList<Card> getHand(Arraylist<Card> hand) {
		this.hand = hand;
	}
	
	public int getPoints() {
		return points;
	}
		
	/*
	 * Adds an integer as paramater to points
	 */
	public void addPoints(int points) {
		this.points += points;
	}
	
	/*
	 * takes a Card element and removes it from the hand.  Returns the same element
	 */
	public Card playAndRemoveCard(Card card) {
		for (int i = 0; i < hand.size(); i++) {
			if card.equals(hand.get(i)){
				return hand.remove(i);
			}
		}
	}
	
}
