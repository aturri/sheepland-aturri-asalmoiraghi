/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 27/mag/2014
 *
 */

package it.polimi.sheepland.client;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * This is the interface for the remote object of the client.
 * 
 * @author Andrea
 */
public interface ClientRemote extends Remote {
    
	/**
	 * This method check if the connection is Up
	 */
    boolean checkConnected() throws RemoteException;

    /**
	 * This method save the token received from server
	 */
    boolean saveClientToken(String token) throws RemoteException;
    
    /**
	 * This method return the token
	 */
    String getToken() throws RemoteException;
    
    /**
	 * This method set the deck with the remote deck received from server
	 */
	void setRemoteDeck(Deck remoteDeck) throws RemoteException;
    
	/**
	 * This method show the shepherd
	 */
	void showShepherd() throws RemoteException;

	/**
	 * This method show the player
	 */
	void showPlayer() throws RemoteException;

	/**
	 * This method show the animals
	 */
	void showAnimals() throws RemoteException;

	/**
	 * This method show the wolf
	 */
	void showWolf() throws RemoteException;

	/**
	 * This method show the balck sheep
	 */
	void showBlackSheep() throws RemoteException;

	/**
	 * This method show a message
	 * @param string the message
	 */
	void showMessage(String string) throws RemoteException;

	/**
	 * This method show the final scores
	 * @param scores
	 */
	void showTotalScores(Map<String, Integer> scores) throws RemoteException;

	/**
	 * This method show an error
	 * @param error
	 */
	void showError(String error) throws RemoteException;

	/**
	 * This method ask for an input of the player
	 * @param code of wich input
	 * @param minIndex
	 * @param maxIndex
	 * @return the required input
	 */
	int askInput(String string, int minIndex, int maxIndex) throws RemoteException;

	/**
	 * This method show the waiting message of the other player
	 * @param string 
	 */
	void showWaitingMessage(String string) throws RemoteException;
	
	/**
	 * This method show that the client is connected at the server
	 */
	void showConnected() throws RemoteException;

	/**
	 * This method show the list of shepherd for the selection
	 */
	void showShepherdForSelection(List<Shepherd> playerSepherds) throws RemoteException ;

	/**
	 * This method show the available turns
	 * @param listOfTurns
	 */
	void showTurnType(List<TurnType> listOfTurns) throws RemoteException ;

	/**
	 * This method show the ovines near the shepherd
	 * @param listOfOvines
	 */
	void showNeighbourOvines(List<Ovine> listOfOvines) throws RemoteException ;

	/**
	 * This method show the available card of the terrains near the shepherd
	 * @param listOfTerrainCards
	 */
	void showAffordableCards(List<TerrainCard> terrainCard) throws RemoteException ;

	/**
	 *This method show the regions near the shepherd
	 * @param listOfRegions
	 */
	void showNeighbourRegions(List<Region> listOfRegions) throws RemoteException ;

	/**
	 * This method show a card to put for sale
	 * @param card
	 */
	void showCardToPutForSale(TerrainCard card) throws RemoteException ;

	/**
	 * This method show the cards on sale
	 * @param listOfCardsForSale
	 */
	void showCardsForSale(List<TerrainCard> listOfCardsForSale) throws RemoteException ;

	/**
	 * This metode terminate the game
	 */
	void terminateGame() throws RemoteException;
}

