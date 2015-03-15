/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 27/mag/2014
 *
 */

package it.polimi.sheepland.server;

import it.polimi.sheepland.client.ClientRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the interface for the remote object of the server
 * @author Andrea
 */
public interface ServerRemote extends Remote {
	
	/**
	 * This method register the client connected at the server
	 * @param ClientRemote
	 * @throws RemoteException
	 */
	public void registerClient(ClientRemote client) throws RemoteException;
	
	/**
	 * This method return check the connection with the client
	 * @return true if the connection is up
	 * @throws RemoteException
	 */
	public boolean checkConnected() throws RemoteException;
}
