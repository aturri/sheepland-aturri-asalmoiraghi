/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 31/mag/2014
 *
 */

package it.polimi.sheepland.view;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents the Communicator.
 * @author Andrea
 */
public abstract class Communicator extends Observable {

	/**
	 * This method send the deck to the client
	 * (If the client use RMI, this method does nothing)
	 * @param deck 
	 * @throws IOException 
	 */
	public abstract void sendDeck(Deck deck) throws IOException;

	/**
	 * This method send to the client the command to show the shepherd
	 * @throws IOException 
	 * 
	 */
	public abstract void showShepherd() throws IOException;

	/**
	 * This method send to the client the command to show the player
	 * @throws IOException 
	 * 
	 */
	public abstract void showPlayer() throws IOException;

	/**
	 * This method send to the client the command to show the animals position
	 * @throws IOException 
	 * 
	 */
	public abstract void showAnimals() throws IOException;

	/**
	 * This method send to the client the command to show the wolf position
	 * @throws IOException 
	 * 
	 */
	public abstract void showWolf() throws IOException;

	/**
	 * This method send to the client the command to show the black sheep position
	 * @throws IOException 
	 * 
	 */
	public abstract void showBlackSheep() throws IOException;

	/**
	 * This method send to the client the command to show the result of the dice
	 * @throws IOException 
	 * 
	 */
	public abstract void showDice() throws IOException;

	/**
	 * This method notify to the client that the game is over
	 * @throws IOException 
	 * 
	 */
	public abstract void showEnd() throws IOException;

	/**
	 * This method send to the client the command to show the scores of the players
	 * @param scores
	 * @throws IOException 
	 */
	public abstract void showTotalScores(Map<String, Integer> scores) throws IOException;

	/**
	 * This method send to the client the command to show an error
	 * @param e
	 * @throws IOException 
	 */
	public abstract void showError(Exception e) throws IOException;

	/**
	 * This method send to the client the command to show the available shepherds for the player
	 * @param playerSepherds
	 * @throws IOException 
	 */
	public abstract void showShepherdForSelection(List<Shepherd> playerSepherds) throws IOException;

	/**
	 * This method send to the client the command to show the available turns for the player
	 * @param listOfTurns
	 * @throws IOException 
	 */
	public abstract void showTurnType(List<TurnType> listOfTurns) throws IOException;

	/**
	 * This method send to the client the command to show the available ovines to move for the player
	 * @param ovines
	 * @throws IOException 
	 */
	public abstract void showNeighbourOvines(List<Ovine> ovines) throws IOException;

	/**
	 * This method send to the client the command to show the affordable cards
	 * @param listOfTerrainCards
	 * @throws IOException 
	 */
	public abstract void showAffordableCards(List<TerrainCard> listOfTerrainCards) throws IOException;

	/**
	 * This method send to the client the command to show the adjacent regions
	 * @param listOfRegions
	 * @throws IOException 
	 */
	public abstract void showNeighbourRegions(List<Region> listOfRegions) throws IOException;

	/**
	 * This method send to the client the command to show the card to put for sale
	 * @param card
	 * @throws IOException 
	 */
	public abstract void showCardToPutForSale(TerrainCard card) throws IOException;

	/**
	 * This method send to the client the command to show the cards for sale
	 * @param listOfCardsForSale
	 * @throws IOException 
	 */
	public abstract void showCardsForSale(List<TerrainCard> listOfCardsForSale) throws IOException;

	/**
	 * This method send to the client the command to ask the street where place the shepherd
	 * @param minStreet
	 * @param maxStreet
	 * @return the index of the street
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public abstract int askShepherdNewStreet(int minStreet, int maxStreet) throws IOException;

	/**
	 * This method send to the client the command to ask which shepherd to use
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the shepherd
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public abstract int askSelectedShepherd(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to ask which turn to do
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the selected turn
	 * @throws IOException 
	 */
	public abstract int askSelectedTurn(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to ask which ovine wants the player
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the ovine selected
	 * @throws IOException 
	 */
	public abstract int askSelectedOvine(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to ask which card wants the player
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the card
	 * @throws IOException 
	 */
	public abstract int askSelectedCard(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to ask which region wants the player
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the region
	 * @throws IOException 
	 */
	public abstract int askSelectedRegion(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to ask if he want to put for sale the card
	 * @param minIndex
	 * @param maxIndex
	 * @return 0/1 no/yes
	 * @throws IOException 
	 */
	public abstract int askCardToPutForSale(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to ask the price for the card putted for sale
	 * @param minIndex
	 * @param maxIndex
	 * @return the price of the card
	 * @throws IOException 
	 */
	public abstract int askPriceForCardOnSale(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to ask if buy the card on sale
	 * @param minIndex
	 * @param maxIndex
	 * @return the card bought
	 * @throws IOException 
	 */
	public abstract int askCardOnSaleToBuy(int minIndex, int maxIndex) throws IOException;

	/**
	 * This method send to the client the command to show to players that are not the current player a message of waiting
	 * @param currentPlayer
	 * @throws IOException 
	 */
	public abstract void waitingMessage(Player currentPlayer) throws IOException;
	
	/**
	 * This method tell the client that it's connected at the game
	 * @throws IOException
	 */
	public abstract void initializeConnection() throws IOException;

	/**
	 * This client check if the connection with the client is up
	 * @return true it the connection is up
	 */
	public abstract boolean checkClientConnected();

	/**
	 * This method disconnect the client
	 */
	public abstract void disconnectClient();

	/**
	 * This method return the string of the client
	 * @return the string of the client
	 */
	public abstract String getClientString();
	
	/**
	 * This method check if the player associated at the communicator is enabled
	 * @return true if the associated player is enabled
	 */
	public abstract boolean isEnabled();

	/**
	 * This method show a message at the client when another client is disconnected
	 * @param string
	 */
	public abstract void showDisconnection(String string) throws IOException;
	
	/**
	 * This method set an observer on the client object
	 * @param remoteView
	 */
	public abstract void setObserverOfClient(Observer observer);

	/**
	 * This method send the token to the client
	 */
	public abstract void sendTokenToClient();

	/**
	 * This method set the player of the client object
	 * @param player
	 */
	public abstract void setClientPlayer(Player player);

	/**
	 * This method return the client object
	 * @return Client
	 */
	public abstract Client getClient();
}
