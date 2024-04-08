import java.util.ArrayList;


class Trick {
	public int leadingPlayer;
	public Card.SUIT leadingSuit;
	public int currentPlayer;
	public int currentWinner;
	public Card currentWinningCard;
	public Card.SUIT trump;
	public ArrayList<Card> cardsPlayed;
	
	public Trick(int leadPlayer, Card.SUIT tr)
	{
		leadingPlayer = leadPlayer % 4;
		currentPlayer = leadPlayer % 4;
		trump = tr;
		cardsPlayed = new ArrayList<Card>();
	}
	
	public void playCardForCurrentPlayer(Card c)
	{
		if (currentPlayer == leadingPlay
		{
			leadingSuit = c.getSuit();
			currentWinner = currentPlayer;
			currentWinningCard = c;
		}
		else if (c.greater(currentWinningCard, trump, leadingSuit))
		{
			currentWinner = currentPlayer;
			currentWinningCard = c;
		}
		cardsPlayed.add(c);
		incrementTurn();
	}
	
	public void incrementTurn()
	{
		currentPlayer = (currentPlayer + 1) % 4;
	}
	
	public boolean isStarted()
	{
		return cardsPlayed.size() != 0;
	}
	
	public boolean isOver()
	{
		System.out.println(cardsPlayed.size() == 4);
		return cardsPlayed.size() == 4;
	}
	
	public Trick getNextTrick()
	{
		return new Trick(currentWinner,trump);
	}
}