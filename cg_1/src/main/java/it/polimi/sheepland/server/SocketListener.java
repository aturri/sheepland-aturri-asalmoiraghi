/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 31/mag/2014
 *
 */

package it.polimi.sheepland.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the listener for the incoming socket connections
 * @author Andrea
 */
public class SocketListener implements Runnable{
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static WaitingRoom waitingRoom;
	private static final int SOCKET_PORT = 13758;
	
	/**
	 * This is the constructor of the class
	 * @param waitingRoom
	 */
	public SocketListener(WaitingRoom waitingRoom) {
		LOGGER.setLevel(Level.INFO);
		SocketListener.waitingRoom = waitingRoom;
	}

	/**
	 * This method starts the server listener for new connections
	 * @throws IOException 
	 */
	public static void socketPlay() throws IOException {
		ServerSocket listener;		
		listener = new ServerSocket(SOCKET_PORT);	
		LOGGER.log(Level.INFO, "Socket listener is running on port "+SOCKET_PORT+". Ready for connection");
	
		try {
			while (true){
				Socket newClient = listener.accept();
				waitingRoom.registerClient(newClient);
			}			
		} finally {			
			listener.close();
		}
	}
	
	/**
	 * This method runs the thread of the socket listener
	 */
	public void run() {
		try {
			socketPlay();
		} catch (Exception e) {			
			LOGGER.log(Level.WARNING, "Unable to start socket server",e);
		}
	}
}
