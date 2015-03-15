/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 10/giu/2014
 *
 */

package it.polimi.sheepland.view;

import it.polimi.sheepland.client.ClientRemote;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andrea
 */
public class RMIClient extends Client {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private ClientRemote rmiClient;
	
	/**
	 * This is the constructor for the class.
	 * Set the remote object of the client
	 * @param client
	 */
	public RMIClient(ClientRemote client) {
		rmiClient = client;
	}
	
	/**
	 * This method send a message to the client
	 * @param string of the message
	 */
	@Override
	protected void sendMessage(String string) {
		try {
			this.rmiClient.showError(string);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Impossibile mandare il messaggio al client",e);
		}
	}

	/**
	 * This method return the remote object of the connecetd client
	 * @return
	 */
	public ClientRemote getRemoteClient(){
		return rmiClient;
	}

	/**
	 * This method check if the connection with the client is up.
	 * @return true if the connection is up
	 */
	@Override
	public boolean checkConnected(){
		try {
			return rmiClient.checkConnected();
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Impossibile contattare il client",e);
			return false;
		}
	}	

	/**
	 * This method send the token to the client
	 */
	@Override
	public void sendToken(){
		try {
			rmiClient.saveClientToken(token.toString());
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Impossibile inviare il token al client",e);
			disconnect();
		}
	}

	/**
	 * This method disconnect the client
	 */
	@Override
	public void disconnect() {		
	}

	/**
	 * This method get the token string directly from the client
	 * @return token string
	 */
	@Override
	public String getTokenFromClient() {
		try {
			return rmiClient.getToken();
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Errore inatteso: ClassNotFoundException while getting the token from client",e);
			return null;
		}
	}
}
