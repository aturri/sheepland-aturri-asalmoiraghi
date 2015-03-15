package it.polimi.sheepland.game;

import it.polimi.sheepland.controller.Controller;
import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.view.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class
 * @author Andrea
 *
 */
public class LocalMain {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * This is private constructor to hide the public one.
	 */
	private LocalMain() {
		return;
	}
	
	/**
	 * This method starts the server for local play only.
	 */
	public static void localPlay() {
		LOGGER.setLevel(Level.INFO);
		try {
			LOGGER.log(Level.INFO, "Starting Sheepland...");
			Deck deck = new Deck();
			View view = new CommandLineView(deck);
			Controller controller = new Controller(view, deck);
			controller.startController();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to start Sheepland",e);
		}
	}
	
	/**
	 * This is the main method
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		try {
			localPlay();
		} catch (Exception e) {			
			LOGGER.log(Level.WARNING, "Unable to start server",e);
		}
	}

}
