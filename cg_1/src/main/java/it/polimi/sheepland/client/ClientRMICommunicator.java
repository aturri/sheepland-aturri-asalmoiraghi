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

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.sheepland.server.ServerRemote;

/**
 * This class represents client communicator for RMI.
 * @author Andrea
 */
public class ClientRMICommunicator extends ClientCommunicator  implements ClientRemote{
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private ServerRemote remoteServer;
	private TerrainCard cardForSale;

	private ClientRemote clientRMIStub;
	
	/**
	 * This is constructor that sets up logger level.
	 */
	public ClientRMICommunicator(){
		LOGGER.setLevel(Level.INFO);
	}

	/**
	 * This method starts the client.
	 */
	public void startCommunications() {
		try {
			LOGGER.log(Level.INFO, "Attendo che si connettano altri giocatori...");
			clientRMIStub = (ClientRemote) UnicastRemoteObject.exportObject(this,0);
			connectToServer();
			runConnectionChecker();
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Cannot connect to server",e);	
		}catch (NotBoundException e) {
			LOGGER.log(Level.WARNING, "Unable to get remote server stub",e);
		}
	}	
	
	/**
	 * This method check if the connection with the server is Up
	 * @throws RemoteException
	 */
	public boolean checkConnected() throws RemoteException {
		return true;
	}
	
	/**
	 * This method sets remote deck and shows it to view.
	 * 
	 * @param deck
	 * @throws RemoteException
	 */
	public void setRemoteDeck(Deck remoteDeck) throws RemoteException {
		setDeck(remoteDeck);
		cView.showDeck();
	}

	/**
	 * This method shows shepherd to view.
	 * 
	 * @throws RemoteException
	 */
	public void showShepherd() throws RemoteException {
		cView.showShepherd();
	}

	/**
	 * This method shows player to view.
	 * 
	 * @throws RemoteException
	 */
	public void showPlayer() throws RemoteException {
		cView.showPlayer();
	}

	/**
	 * This method shows animals to view.
	 * 
	 * @throws RemoteException
	 */
	public void showAnimals() throws RemoteException {
		cView.showAnimals();
	}

	/**
	 * This method shows wolf to view.
	 * 
	 * @throws RemoteException
	 */
	public void showWolf() throws RemoteException {
		cView.showWolf();
	}

	/**
	 * This method shows blacksheep to view.
	 * 
	 * @throws RemoteException
	 */
	public void showBlackSheep() throws RemoteException {
		cView.showBlackSheep();
	}

	/**
	 * This method shows message to view.
	 * 
	 * @param string message
	 * @throws RemoteException
	 */
	public void showMessage(String string) throws RemoteException {
		cView.showMessage(string);
	}

	/**
	 * This method shows total scores to view.
	 * 
	 * @param map scores
	 * @throws RemoteException
	 */
	public void showTotalScores(Map<String, Integer> scores) throws RemoteException {
		List<String> listScores = new ArrayList<String>();
		Iterator<Entry<String, Integer>> scoreIt = scores.entrySet().iterator();
		while(scoreIt.hasNext()){
			Entry<String, Integer> score = scoreIt.next();
			listScores.add(score.getKey()+":"+score.getValue());
		}
		cView.showFinalScore(listScores);
	}

	/**
	 * This method shows error to view.
	 * 
	 * @param error message
	 * @throws RemoteException
	 */
	public void showError(String error) throws RemoteException {
		cView.showError(error);
	}

	/**
	 * This method asks input to view.
	 * 
	 * @param string
	 * @param min
	 * @param max
	 * @throw RemoteException
	 */
	public int askInput(String string, int minIndex, int maxIndex) throws RemoteException {
		if("STREET".equals(string)){
			String message = "ASK:STREET";
			return cView.askInput(message, minIndex, maxIndex);
		}else if("SHEPHERD".equals(string)){
			String message = "ASK:SHEPHERD";
			return cView.askInput(message, minIndex, maxIndex);
		}else if("TURN".equals(string)){
			String message = "ASK:TURN";
			return cView.askInput(message, minIndex, maxIndex);
		}else if("OVINE".equals(string)){
			String message = "ASK:OVINE";
			return cView.askInput(message, minIndex, maxIndex);
		}else if("CARD".equals(string)){
			String message = "ASK:CARD";
			return cView.askInput(message, minIndex, maxIndex);
		}else if("REGION".equals(string)){
			String message = "ASK:REGION";
			return cView.askInput(message, minIndex, maxIndex);
		}else if("CARD_FOR_SALE".equals(string)){
			String message = "ASK:PUT:"+cardForSale.getTerrainType().name();
			return cView.askInput(message, minIndex, maxIndex);
		}else if("PRICE_FOR_SALE".equals(string)){
			String message = "ASK:PRICE";
			return cView.askInput(message, minIndex, maxIndex);
		}else if("BUY_CARD".equals(string)){
			String message = "ASK:BUY:"+Integer.toString(deck.getCardsForSale().size());
			return cView.askInput(message, minIndex, maxIndex);
		}
		return -1;
	}

	/**
	 * This method shows wait message to view.
	 * 
	 * @param player string
	 * @throws RemoteException
	 */
	public void showWaitingMessage(String player) throws RemoteException {
		cView.showWait(player);
	}

	/**
	 * This method shows connected message to view.
	 * 
	 * @throws RemoteException
	 */
	public void showConnected() throws RemoteException {
		cView.showConnected();
	}

	/**
	 * This method shows selectable shepherds to view.
	 * 
	 * @param list of player's shepherds
	 * @throws RemoteException
	 */
	public void showShepherdForSelection(List<Shepherd> playerSepherds) throws RemoteException {
		cView.showListShepherds(playerSepherds);
	}

	/**
	 * This method shows selectable turns to view.
	 * 
	 * @param list of turns
	 * @throws RemoteException
	 */
	public void showTurnType(List<TurnType> listOfTurns) throws RemoteException {
		cView.showListTurns(listOfTurns);		
	}

	/**
	 * This method shows selectable ovines to view.
	 * 
	 * @param list of ovines
	 * @throws RemoteException
	 */
	public void showNeighbourOvines(List<Ovine> ovines) throws RemoteException {
		cView.showListOvines(ovines);
	}

	/**
	 * This method shows selectable cards to view.
	 * 
	 * @param list of cards
	 * @throws RemoteException
	 */
	public void showAffordableCards(List<TerrainCard> listOfTerrainCards) throws RemoteException {
		cView.showListCards(listOfTerrainCards);
	}

	/**
	 * This method shows selectable regions to view.
	 * 
	 * @param list of regions
	 * @throws RemoteException
	 */
	public void showNeighbourRegions(List<Region> listOfRegions) throws RemoteException {
		 cView.showListRegions(listOfRegions);
		
	}

	/**
	 * This method shows sets card for sale.
	 * 
	 * @param card
	 * @throws RemoteException
	 */
	public void showCardToPutForSale(TerrainCard card) throws RemoteException {
		cardForSale = card;
	}

	/**
	 * This method shows selectable cards of market to view.
	 * 
	 * @param list of cards
	 * @throws RemoteException
	 */
	public void showCardsForSale(List<TerrainCard> listOfCardsForSale) throws RemoteException {
		cView.showListCardsMarket(listOfCardsForSale);
	}

	/**
	 * This method saves client token.
	 * 
	 * @param token
	 */
	public boolean saveClientToken(String token) {
		setToken(token);
		saveToken(token);
		return true;
	}

	/**
	 * This method check if the connection with the server is Up
	 */
	@Override
	public boolean checkConnection() {
		try {
			return remoteServer.checkConnected();
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "The connection is down",e);			
			return false;
		}
	}

	/**
	 * This method try to connect at the server.
	 * 
	 * @throws NotBoundException impossibile get the remote stub from server on RMI connection
	 * @throws RemoteException
	 */
	@Override
	public void connectToServer() throws NotBoundException, RemoteException {
		String name = "ServerRemote";
		Registry registry = LocateRegistry.getRegistry(IP_ADDR,13475);
		remoteServer = (ServerRemote) registry.lookup(name);
		remoteServer.registerClient(clientRMIStub);
	}

	/**
	 * This method returns the token.
	 * 
	 * @throws RemoteException
	 */
	public String getToken() throws RemoteException {
		return token;
	}

	/**
	 * This method terminates the game.
	 */
	public void terminateGame() {
		stopConnectionChecker();
		deleteFile();
	}
	
	/**
	 * This method run the communication when use Socket (RMI do nothing).
	 */
	@Override
	public void run() {
		return;
	}	
}
