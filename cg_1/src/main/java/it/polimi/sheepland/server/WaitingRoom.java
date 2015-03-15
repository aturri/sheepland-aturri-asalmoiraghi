/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 02/giu/2014
 *
 */

package it.polimi.sheepland.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.sheepland.client.ClientRemote;
import it.polimi.sheepland.game.Game;
import it.polimi.sheepland.view.Client;
import it.polimi.sheepland.view.RMIClient;
import it.polimi.sheepland.view.SocketClient;

/**
 * This calss is the main controller for the network.
 * Is the waitingRoom for the incoming client that want to play or reconnect at the game.
 * @author Andrea
 */
public class WaitingRoom implements Observer{
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final int WAITING_TIME = 10000;
	private static final int MIN_CLIENTS = 2;
	private static final int MAX_CLIENTS = 4;
	private List<Client> listWaitingClients;
	private List<Client> listConnectedClients;
	private Timer timer;
	
	/**
	 * This is the constructor of the class
	 */
	public WaitingRoom(){
		LOGGER.setLevel(Level.INFO);
		listWaitingClients = new ArrayList<Client>();
		listConnectedClients = new ArrayList<Client>();
	}
	
	/**
	 * This method register the new client.
	 * If the client try to reconnect (send a valid token), try to reconnect it
	 */
	public synchronized void registerClient(Object client){
		Client newClientObject = createClient(client);
		String clientToken = newClientObject.getTokenFromClient();
		if(!(clientToken!=null && reconnectClient(clientToken, newClientObject))){
			registerNewClient(newClientObject);
		}		
	}
	
	/**
	 * This method try to reconnect the client
	 * @param clientToken
	 * @return true if the client is reconnected with success
	 */
	private boolean reconnectClient(String clientToken, Client newClientObject) {
		Client oldClientObject = getOldConnectedClient(clientToken);
		if(oldClientObject!=null && playerIsNotEnabled(oldClientObject)){
			updateClient(newClientObject,oldClientObject);
			return true;
		}
		return false;
	}

	/**
	 * This method checks if the player is really not enabled
	 * @param oldClientObject
	 * @return true if the player is note enabled
	 */
	private boolean playerIsNotEnabled(Client oldClientObject) {
		return !oldClientObject.getPlayer().isEnabled();
	}

	/**
	 * This method register a new client
	 * @param new client
	 */
	private void registerNewClient(Client newClient) {
		listWaitingClients.add(newClient);
		LOGGER.log(Level.INFO, "Client #"+listWaitingClients.size()+" connected throught "+newClient.toString());
		checkNumberOfClients();
	}

	/**
	 * This method change the old client object with the new one of the reconnected client
	 * @param client
	 */
	private void updateClient(Client newClientObject, Client oldClientObject) {
		newClientObject.setPlayer(oldClientObject.getPlayer());
		oldClientObject.notifyReconnectedClient(newClientObject);
		listConnectedClients.remove(oldClientObject);
		listConnectedClients.add(newClientObject);
	}

	/**
	 * This method search if a connected client has the same token received and return it.
	 * @return the client with the searched token.(Return null if no client has the token searched)
	 */
	private Client getOldConnectedClient(String token) {
		for(Client client: listConnectedClients){
			if(token.equals(client.getToken().toString())){
				return client;
			}
		}
		return null;
	}

	/**
	 * This method create the Client object
	 * @param client
	 * @return a new instance of Client
	 */
	private Client createClient(Object client) {
		if(client instanceof Socket){
			return new SocketClient((Socket) client);
		}else{
			return new RMIClient((ClientRemote) client);
		}
	}

	/**
	 * This method checks the actual number of clients.
	 * =2 -> start the timer
	 * =4 -> start directly the game
	 */
	private void checkNumberOfClients() {
		if(listWaitingClients.size()==MIN_CLIENTS){
			startTimer();
		}else if(listWaitingClients.size()==MAX_CLIENTS){
			startGame();
		}
	}

	/**
	 * This method start the waiting timer. After a WAITING_TIME it starts the game
	 */
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
			   	startGame();
			  }
			}, WAITING_TIME);
	}
	
	/**
	 * This method stop the timer
	 */
	private void stopTimer(){
		timer.cancel();
		timer.purge();
	}

	/**
	 * This method starts the new game if the effective connected clients are mmore than 1
	 */
	private void startGame() {
		stopTimer();
		if(ConnectedClients()>1){		
			List<Client> listClients = new ArrayList<Client>(listWaitingClients);
			Game game = new Game(listClients);
			Thread threadGame = new Thread(game);
			threadGame.start();
			
			listConnectedClients.addAll(listWaitingClients);
			listWaitingClients.clear();
		}
	}

	/**
	 * This method check the connected clients.
	 * @return the number of effective connected clients
	 */
	private int ConnectedClients() {
		for(Client client: listWaitingClients){
			if(!client.checkConnected()){
				removeWaitingClient(client);
			}
		}
		return listWaitingClients.size();
	}

	/**
	 *  This method remove the disconnected client from the list of waiting clients
	 * @param client
	 */
	private void removeWaitingClient(Client client) {
		LOGGER.log(Level.INFO, "Client #"+listWaitingClients.indexOf(client)+" disconnected throught "+client.toString());
		listWaitingClients.remove(client);
	}

	/**
	 * This method receive the notify from Game and remove the received clients from the list of connected clients
	 */
	public void update(Observable o, Object arg) {
		listConnectedClients.removeAll((Collection<?>) arg);
	}
}
