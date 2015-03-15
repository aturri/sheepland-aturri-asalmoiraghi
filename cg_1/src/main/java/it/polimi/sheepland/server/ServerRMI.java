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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * This is the implementation of the server remote for RMI
 * @author Andrea
 */
public class ServerRMI extends UnicastRemoteObject implements ServerRemote{

	private static final long serialVersionUID = -7960912748737649559L;
	private static WaitingRoom waitingRoom;
	
	/**
	 * This is the constructor of the class.
	 * Set the waiting romm
	 * @param waitingRoom
	 * @throws RemoteException
	 */
	protected ServerRMI(WaitingRoom waitingRoom) throws RemoteException {
		super();
		ServerRMI.waitingRoom = waitingRoom;
	}

	/**
	 * This method register the client connected at the server
	 * @param ClientRemote
	 * @throws RemoteException
	 */
	public void registerClient(ClientRemote client) throws RemoteException {
		waitingRoom.registerClient(client);
	}

	/**
	 * This method return check the connection with the client
	 * @return true if the connection is up
	 * @throws RemoteException
	 */
	public boolean checkConnected() throws RemoteException {
		return true;
	}
}
