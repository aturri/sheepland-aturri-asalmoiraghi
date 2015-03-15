package it.polimi.sheepland.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the waiter for user click
 * @author Andrea
 *
 */
public class EventWaiter {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private GUIClientView guiClientView;
	
	/**
	 * This is constructor for the waiter.
	 * @param guiClientView
	 */
	public EventWaiter(GUIClientView guiClientView) {
		this.guiClientView = guiClientView;
		LOGGER.setLevel(Level.WARNING);
	}

	/**
	 * This method blocks the client thread.
	 */
	public synchronized void waitClick() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, "Cannot wait for user input",e);
		}
	}
	
	/**
	 * This method is called when recieved a click, it unlocks the thread. It sends the input to GUICLientView
	 * @param input
	 */
	protected synchronized void notifyClick(int input) {
		guiClientView.setLastInput(input);
		this.notify();
	}
}
