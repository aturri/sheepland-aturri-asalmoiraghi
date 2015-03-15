package it.polimi.sheepland.server;

/**
 * This class is Main for Server.
 * @author Andrea
 */
public class ServerMain {
	
	/**
	 * This is private constructor for the class, to hide the public one.
	 */
	private ServerMain() {
		return;
	}

	/**
	 * This is main method for Server
	 * @param args
	 */
	public static void main(String[] args) {
		WaitingRoom waitingRoom = new WaitingRoom();
		
		SocketListener socketListener = new SocketListener(waitingRoom);
		Thread socketThread = new Thread(socketListener);
		
		RMIListener rmiListener = new RMIListener(waitingRoom);
		Thread rmiThread = new Thread(rmiListener);
		
		socketThread.start();
		rmiThread.start();		
	}
}
