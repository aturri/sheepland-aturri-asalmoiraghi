package it.polimi.sheepland.client;

import java.rmi.RemoteException;

/**
 * This class represents the main for Client.
 * 
 * @author Andrea
 */
public class ClientMain {

	/**
	 * Empty private constructor to hide the public one.
	 */
	private ClientMain() {
		return;
	}
	
	/**
	 * This is the main method
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {
		ClientInitializer clientInitializer = new ClientInitializer();
		clientInitializer.initialize();
	}
}
