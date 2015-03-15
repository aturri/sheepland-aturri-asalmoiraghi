/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 10/giu/2014
 *
 */

package it.polimi.sheepland.view;

import it.polimi.sheepland.model.Player;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andrea
 */
public abstract class Client extends Observable{
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	protected UUID token;
	private Player player;

	/**
	 * This is the constructor for the class.
	 * Set the logger and the initial token.
	 */
	public Client(){
		LOGGER.setLevel(Level.INFO);
		setToken(UUID.randomUUID());
	}
	
	/**
	 * This method check if the connection with the client is up.
	 * @return true if the connection is up
	 */
	public abstract boolean checkConnected();
	
	/**
	 * This method disconnect the client
	 */
	public abstract void disconnect();

	/**
	 * This method return the UUID token of the client
	 * @return token
	 */
	public UUID getToken() {
		return token;
	}

	/**
	 * This method set the UUID token of the client
	 * @param token
	 */
	private void setToken(UUID token) {
		this.token = token;
	}
	
	/**
	 * This method send the token to the client
	 */
	public abstract void sendToken();
	
	/**
	 * This token check if the received token is equals to the client's token.
	 * If not send it again.
	 * @param receivedToken
	 * @throws RemoteException 
	 */
	protected void checkToken(String receivedToken) throws RemoteException {
		if(!receivedToken.equals(token.toString())){
			sendToken();
		}
	}

	/**
	 * This method get the token string directly from the client
	 * @return token string
	 */
	public abstract String getTokenFromClient();

	/**
	 * This method get the associated player of the client
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * This method set the associated player of the client
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * This method notify observers
	 */
	protected void notifyChangement(){
		setChanged();
		notifyObservers();
	}
	
	/**
	 * This method notify observers
	 */
	public void notifyReconnectedClient(Object reconnectedClient){
		setChanged();
		notifyObservers(reconnectedClient);
	}

	/**
	 * This method send a message to the client
	 * @param string of the message
	 */
	protected abstract void sendMessage(String string);
}
