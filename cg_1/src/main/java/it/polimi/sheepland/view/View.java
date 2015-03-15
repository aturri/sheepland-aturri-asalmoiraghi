package it.polimi.sheepland.view;

import it.polimi.sheepland.exception.ClientUnreachableException;
import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents an abstract view for View
 * @author Andrea
 *
 */
public abstract class View extends Observable implements Observer {
	protected Deck deck;
	protected static final int SETUP_INT = -1;
	protected static final int MIN_PLAYERS = 2;
	protected static final int MAX_PLAYERS = 4;
	protected static final int MIN_STREET = 0;
	protected static final int MAX_STREET = 41;
	protected static final int MIN_INDEX = 0;
	protected static final int MAX_INDEX = 1;

	/**
	 * Constructor for View
	 * @param deck
	 */
	public View(Deck deck) {
		this.deck = deck;
		deck.addObserver(this);
	}
	
	/**
	 * This method asks the number of players and changes status
	 */
	public abstract void askNumPlayers();

	/**
	 * This method asks the street where the shepherd will be placed
	 * @throws ClientUnreachableException 
	 */
	public abstract void askShepherdPosition();
	
	/**
	 * This method asks the street where the shepherd will be placed at the beginning of game
	 * @throws ClientUnreachableException 
	 */
	public abstract void askShepherdInitialPosition() throws ClientUnreachableException;

	/**
	 * This method asks to choose the shepherd
	 * @throws ClientUnreachableException 
	 */
	public abstract void askShepherd() throws ClientUnreachableException;

	/**
	 * This method asks to choose the turn type
	 * @param listOfTurns
	 * @throws ClientUnreachableException 
	 */
	public abstract void askTurnType(List<TurnType> listOfTurns) throws ClientUnreachableException;

	/**
	 * This method asks to choose an ovine to be moved/killed
	 * @throws ClientUnreachableException 
	 */
	public abstract void askOvine(TurnType turnType);
	
	/**
	 * This method asks to choose a card
	 * @throws ClientUnreachableException 
	 */
	public abstract void askCard();

	/**
	 * This method asks to choose a region for coupling
	 * @param turnType 
	 * @throws ClientUnreachableException 
	 */
	public abstract void askRegion(TurnType turnType);
	
	/**
	 * This method asks if player wants to sell the card
	 * @param card
	 * @throws ClientUnreachableException 
	 */
	public abstract void askPutCardForSale(TerrainCard card) throws ClientUnreachableException;

	/**
	 * This method asks if player wants to buy the card
	 * @param card
	 * @throws ClientUnreachableException 
	 */
	public abstract void askBuyCardForSale();
	
	/**
	 * This method shows error
	 * @param exception
	 * @throws ClientUnreachableException 
	 */
	public abstract void showError(Exception e);
	
	/**
	 * This method show the total scores of the players at the end of game
	 * @param playersScores 
	 * @throws ClientUnreachableException 
	 */
	public abstract void showTotalScores(Map<String, Integer> playersScores);

	/**
	 * This method says that player didn't get the number of its shepherd street with the dice
	 */
	public abstract void showDice();
	
	/**
	 * This method initializes player
	 */
	public abstract void initializePlayers();

	/**
	 * This method tell that the game is end
	 */
	public abstract void showEnd();
}
