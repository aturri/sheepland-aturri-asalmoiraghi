/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 31/mag/2014
 *
 */

package it.polimi.sheepland.client;

import it.polimi.sheepland.model.BlackSheep;
import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.Wolf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the abstract class for the communicator used by the client to talk with the server.
 * 
 * @author Andrea
 */
public abstract class ClientCommunicator {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static final String DAT_FILE = "GameSaved.dat";

//	protected static final String IP_ADDR = "192.168.1.1"; **for testing
	protected static final String IP_ADDR = "127.0.0.1";
	
	protected Deck deck;
	protected ClientView cView;
	protected String token;
	protected ClientConnectionChecker connectionChecker;
	
	/**
	 * This method check if the connection with the server is Up
	 */
	public abstract boolean checkConnection();
	
	/**
	 * This method try to connect at the server.
	 * 
	 * @throws IOException impossible contact the server
	 * @throws NotBoundException impossibile get the remote stub from server on RMI connection
	 */
	public abstract void connectToServer() throws IOException, NotBoundException;
	
	
	/**
	 * This method set the token
	 */
	public void setToken(String token){
		this.token = token;
	}
	
	/**
	 * This method save the token in a local file.
	 */
	protected void saveToken(String token){
		PrintWriter writer;
		try {
//			writer = new PrintWriter(new BufferedWriter(new FileWriter(DAT_FILE,true)));**
			writer = new PrintWriter(new BufferedWriter(new FileWriter(DAT_FILE)));
			writer.println(token);
			writer.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Errore inatteso durante salvataggio token su disco",e);
		}
	}
	
	/**
	 * This method set the client view.
	 * 
	 * @param view
	 */
	public void setClientView(ClientView view) {
		this.cView = view;
	}
	
	/**
	 * This method set the deck.
	 * 
	 * @param deck
	 */
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	
	/**
	 * This method starts the communications between client and server.
	 */
	public abstract void startCommunications();
	
	/**
	 * This method start the connection checker.
	 */
	protected void runConnectionChecker() {
		connectionChecker = new ClientConnectionChecker(this,cView);
		connectionChecker.startTimer();
	}
	
	/**
	 * This method stop the connection checker.
	 */
	protected void stopConnectionChecker() {
		if(connectionChecker!=null){
			connectionChecker.stopChecking();
		}
	}
	
	/**
	 * This method run the communication when use Socket (RMI do nothing).
	 */
	public abstract void run();
	
	/**
	 * This method returns the wolf string.
	 * 
	 * @return wolf string
	 */
	public String getWolfString() {
		return deck.getWolfString();
	}
	
	/**
	 * This method returns the wolf.
	 * 
	 * @return wolf
	 */
	public Wolf getWolf() {
		return deck.getWolf();
	}

	/**
	 * This method returns the black sheep string.
	 * 
	 * @return black sheep string
	 */
	public String getBlackSheepString() {
		return deck.getBlackSheepString();
	}
	
	/**
	 * This method returns the BlackSheep.
	 * 
	 * @return BlackSheep
	 */
	public BlackSheep getBlackSheep() {
		return deck.getBlackSheep();
	}

	/**
	 * This method returns the current player.
	 * 
	 * @return current player
	 */
	public Player getPlayer() {
		return deck.getCurrentPlayer();
	}
	
	/**
	 * This method returns the current shepherd.
	 * 
	 * @return current shepherd
	 */
	public Shepherd getShepherd() {
		return deck.getCurrentShepherd();
	}
	
	/**
	 * This method returns the list of regions.
	 * 
	 * @return list of regions
	 */
	public List<Region> getRegions() {
		return deck.getRegions();
	}
	
	/**
	 * This method returns the ovine from the id.
	 * 
	 * @param id
	 * @return ovine
	 */
	public Ovine getOvineById(int id) {
		for(Region r: deck.getRegions()) {
			for(Ovine o: r.getListOfOvines()) {
				if(o.getId()==id) {
					return o;
				}
			}
		}
		return null;
	}

	/**
	 * This method returns the list of streets.
	 * 
	 * @return list of streets
	 */
	public List<Street> getAllStreets() {
		return deck.getStreets();
	}
	
	/**
	 * This method delete the file saved with the token.
	 */
	protected void deleteFile(){
		File file = new File(DAT_FILE);
		file.delete();
	}
}
