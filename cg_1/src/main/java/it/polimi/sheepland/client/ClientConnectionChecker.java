/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 13/giu/2014
 *
 */

package it.polimi.sheepland.client;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class check periodically if the communicator received as parameter can contact the server or if the connection is down.
 * 
 * @author Andrea
 */
public class ClientConnectionChecker {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static final int WAITING_PERIOD_TIME = 600;
	private static final int MAX_NUMBER_OF_ATTEMPTS = 5;
	private static final int WAITING_ATTEMPTS_TIME = 1000;
	
	private ClientCommunicator communicator;
	private ClientView cView;
	private Timer timer;
	
	/**
	 * The constructor of the class
	 * @param clientCommunicator
	 * @param View
	 */
	public ClientConnectionChecker(ClientCommunicator clientCommunicator, ClientView cView) {
		this.communicator = clientCommunicator;
		this.cView = cView;
	}

	/**
	 * This method start the timer
	 */
	public void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			   	checkConnected();
			  }				
			},0, WAITING_PERIOD_TIME);
	}
	
	/**
	 * This check if the connection is Up
	 */
	private void checkConnected() {
		if(!communicator.checkConnection()){
			tryReconnection();		
		}
	}
	
	/**
	 * This method try to reconnect the server
	 */
	private void tryReconnection() {
		cView.showWaitReconnection();
		boolean reconnected = false;
		int attempts = 0;
		while(!reconnected && attempts < MAX_NUMBER_OF_ATTEMPTS){
			try {
				LOGGER.log(Level.INFO, "Tentativo di riconnession n°:"+(attempts+1));
				communicator.connectToServer();
				communicator.run();
				reconnected = true;
				LOGGER.log(Level.INFO, "Tentativo di riconnessione riuscito!");
			} catch (Exception e) {
				LOGGER.log(Level.INFO, "Tentativo di riconnessione fallito!");
				LOGGER.log(Level.WARNING,"Errore:",e);
				attempts++;
				waitTime(WAITING_ATTEMPTS_TIME);
			}
		}
		if(!reconnected) {
			cView.showDisconnected();
			stopChecking();
		} else {
			cView.showReconnected();
		}
	}
	
	/**
	 * This method stop to check the connection with the server
	 */
	public void stopChecking() {
		timer.cancel();
		timer.purge();
	}

	/**
	 * This method wait a time expressed in ms
	 * @param waitingTime
	 */
	private void waitTime(int waitingTime){
		try {
			TimeUnit.MILLISECONDS.sleep(waitingTime);
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, "Unexpected error while waiting for reconnection of the client",e);
		}
	}
}
