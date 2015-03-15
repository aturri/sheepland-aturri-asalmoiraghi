/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 31/mag/2014
 *
 */

package it.polimi.sheepland.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the listener for the incoming RMI connections
 * @author Andrea
 */
public class RMIListener implements Runnable{
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static WaitingRoom waitingRoom;
	private static final int RMI_PORT = 13475;
	

	/**
	 * This is the constructor of the class
	 * @param waitingRoom
	 */
	public RMIListener(WaitingRoom waitingRoom) {
		RMIListener.waitingRoom = waitingRoom;
	}


	/**
	 * This method runs the thread of the RMI listener
	 */
	public void run() {
		try {
			rmiPlay();
		} catch (Exception e) {			
			LOGGER.log(Level.WARNING, "Unable to start RMI server",e);
		}
	}
	
	/**
	 * This method locate the stub on the registry
	 * @throws RemoteException 
	 * @throws AlreadyBoundException 
	 * 
	 */
	private void rmiPlay() throws RemoteException, AlreadyBoundException {
		ServerRemote rmiServer = new ServerRMI(waitingRoom);
		Registry registry = LocateRegistry.createRegistry(RMI_PORT);
		registry.bind("ServerRemote",rmiServer);
		LOGGER.log(Level.INFO, "RMI listener is running on port "+RMI_PORT+". Ready for connection");
	}
}
