package it.polimi.sheepland.game;

import it.polimi.sheepland.controller.Controller;
import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.view.Client;
import it.polimi.sheepland.view.RemoteView;
import it.polimi.sheepland.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a game
 * @author Andrea
 */
public class Game extends Observable implements Runnable {
	private List<Client> listClients = new ArrayList<Client>();	
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * This is constructor for game
	 * @param listSockets
	 */
	public Game(List<Client> listClients) {
		this.listClients = listClients;
	}

	/**
	 * This method stars the thread for game
	 */
	public void run() {
		LOGGER.log(Level.INFO, "Starting new Game "+this.toString());
		Deck deck = new Deck();
		View view = new RemoteView(deck,listClients);
		Controller controller = new Controller(view, deck);
		controller.startController();
		disconnectClients();
		LOGGER.log(Level.INFO, "Game "+this.toString()+" terminated");
	}

	/**
	 * This method notify to observers (WaitingRoom) to delete from connected clients the clients of the game
	 */
	private void disconnectClients() {
		setChanged();
		notifyObservers(listClients);
	}
}