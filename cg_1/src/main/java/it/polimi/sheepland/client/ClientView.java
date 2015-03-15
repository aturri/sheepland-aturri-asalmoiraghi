package it.polimi.sheepland.client;

import java.util.List;

/**
 * This is abstract class for client view
 * @author Andrea
 *
 */
public abstract class ClientView{	
	protected ClientCommunicator client;
	
	/**
	 * This method set the communicator with the server
	 * @param client
	 */
	public void setClientCommunicator(ClientCommunicator client) {
		this.client = client;
	}
	
	/**
	 * This method shows a message
	 * @param string
	 */
	public abstract void showMessage(String string);
	
	/**
	 * This method asks user input
	 * @param message
	 * @param min
	 * @param max
	 * @return index
	 */
	public abstract int askInput(String message, int min, int max);

	/**
	 * This method shows the player
	 */
	public abstract void showPlayer();

	/**
	 * This method shows all animals
	 */
	public abstract void showAnimals();

	/**
	 * This method shows the shepherd
	 */
	public abstract void showShepherd();

	/**
	 * This method shows the blacksheep
	 */
	public abstract void showBlackSheep();

	/**
	 * This method shows the wolf
	 */
	public abstract void showWolf();

	/**
	 * This method displays a message for "Connection established"
	 */
	public abstract void showConnected();

	/**
	 * This method shows waiting message
	 * @param playerName
	 */
	public abstract void showWait(String playerName);

	/**
	 * This method shows an error message
	 * @param message
	 */
	public abstract void showError(String message);

	/**
	 * This method shows available shepherds
	 * @param listItems
	 */
	public abstract void showListShepherds(List<?> listItems);

	/**
	 * This method shows available turns
	 * @param listOfTurns
	 */
	public abstract void showListTurns(List<?> listOfTurns);

	/**
	 * This method shows available ovines
	 * @param ovines
	 */
	public abstract void showListOvines(List<?> ovines);

	/**
	 * This method returns available cards
	 * @param listOfTerrainCards
	 */
	public abstract void showListCards(List<?> listOfTerrainCards);

	/**
	 * This method returns available cards for market
	 * @param listOfCardsForSale
	 */
	public abstract void showListCardsMarket(List<?> listOfCardsForSale);

	/**
	 * This method returns available regions
	 * @param listOfRegions
	 */
	public abstract void showListRegions(List<?> listOfRegions);

	/**
	 * This method shows deck
	 */
	public abstract void showDeck();

	/**
	 * This method shows final score
	 * @param string
	 */
	public abstract void showFinalScore(List<?> listItems);
	
	/**
	 * This method shows a message to wait for reconnection
	 */
	public abstract void showWaitReconnection();
	
	/**
	 * This method shows that connection has been re-established
	 */
	public abstract void showReconnected();
	
	/**
	 * This method shows that connection has definitively lost
	 */
	public abstract void showDisconnected();
}
