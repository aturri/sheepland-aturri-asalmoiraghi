package it.polimi.sheepland.client;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.TerrainCard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents client communicator for Socket.
 * @author Andrea
 */
public class ClientSocketCommunicator extends ClientCommunicator{
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final int PORT = 13758;
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	private List<Object> listItems = new ArrayList<Object>();
	private boolean run = false;

	/**
	 * This is constructor for client.
	 */
	public ClientSocketCommunicator() {
		LOGGER.setLevel(Level.INFO);
	}
	
	/**
	 * This method starts the client.
	 */
	@Override
	public void startCommunications() {
		//setup connection and I/O
		try {
			connectToServer();
			LOGGER.log(Level.INFO, "Attendo che si connettano altri giocatori...");
			//important: run before runConnectionChecker
			run();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Cannot connect to the server",e);
		}
	}
	
	/**
	 * This method try to connect at the server.
	 * 
	 * @throws IOException impossible contact the server
	 */
	@Override
	public void connectToServer() throws IOException {
		socket = new Socket(IP_ADDR, PORT);
		createStream();
		run = true;
	}
	
	/**
	 * This method create the object output and input stream of the socket.
	 * 
	 * @throws IOException 
	 */
	private void createStream() throws IOException {
		socketOut = new ObjectOutputStream(socket.getOutputStream());
		socketOut.flush();
		socketIn = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * This method run the communication when use Socket,
	 */
	@Override
	public void run()  {
		while(run) {
			try {
				Object input = socketIn.readObject();
				if(input instanceof String) {
					String str = (String)input;
					if("CONNECTED:".equals(str)) {
						cView.showConnected();
					} else if(str.startsWith("WAIT:")) {
						String playerName = str.substring("WAIT:".length());
						cView.showWait(playerName);
					} else if(str.startsWith("ERROR:")) {
						String message = str.substring("ERROR:".length());
						cView.showError(message);
					}  else if(str.startsWith("BEGIN:DECK")) {
						setDeck((Deck) socketIn.readObject());
						cView.showDeck();
					} else if(str.startsWith("BEGIN:PLAYER")) {
						cView.showPlayer();
					} else if(str.startsWith("BEGIN:SEND:UPDATE:")) {
						String substr = str.substring("BEGIN:SEND:UPDATE:".length());
						parseUpdate(substr);					
					} else if(str.startsWith("REQUIRE:")) {
						String substr = str.substring("REQUIRE:".length());
						parseRequire(substr);
					} else if(str.startsWith("BEGIN:SEND:OBJECT")) {
						listItems.clear();
						Object object = socketIn.readObject();
						while(!(object instanceof String)) {
							listItems.add(object);
							object = socketIn.readObject();
						}						
					} else if(str.startsWith("DICE:")) {
						cView.showError("Non hai ottenuto il numero della strada sul dado!");
					} else if(str.startsWith("SCORE:")) {
						listItems.clear();
						String string = (String) socketIn.readObject();
						while(!"SCORE:END".equals(string)) {
							listItems.add(string);
							string = (String) socketIn.readObject();
						}		
						cView.showFinalScore(listItems);
					} else if(str.startsWith("TERMINATE:")) {
						terminateGame();
					} else if(str.startsWith("CHECK_CONNECTED")){
						socketOut.writeObject("CONNECTED");
					} else if(str.startsWith("TOKEN")){
						String substr = str.substring("TOKEN:".length());
						setToken(token);
						saveToken(substr);
						socketOut.writeObject(substr);
					} else if(str.startsWith("GET_TOKEN")){
						socketOut.writeObject(token);
					} else {
						LOGGER.log(Level.FINEST, str);
					}
				}
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Errore inatteso nel comunicare col server",e);
				stop();
				runConnectionChecker();
			} catch(ClassNotFoundException e){
				LOGGER.log(Level.WARNING, "Errore inatteso: class not found exception",e);
			}
		}
		closeConnection();
	}

	/**
	 * This method terminate the game
	 */
	private void terminateGame() {
		stopConnectionChecker();
		stop();
		deleteFile();
	}

	/**
	 * This method stop the listening on the socket.
	 */
	private void stop() {
		run = false;
	}

	/**
	 * This method analyzes the request.
	 * 
	 * @param substr
	 * @throws IOException
	 */
	private void parseRequire(String substr) throws IOException {
		if(substr.startsWith("STREET:")) {
			String message = "ASK:STREET";
			String range = substr.substring("STREET:".length());
			requireInput(message, range);
		} else if(substr.startsWith("SHEPHERD:")) {
			cView.showListShepherds(listItems);
			String range = substr.substring("SHEPHERD:".length());
			String message = "ASK:SHEPHERD";
			requireInput(message, range);
		} else if(substr.startsWith("TURN:")) {
			cView.showListTurns(listItems);
			String range = substr.substring("TURN:".length());
			String message = "ASK:TURN";
			requireInput(message, range);
		} else if(substr.startsWith("OVINE:")) {
			cView.showListOvines(listItems);
			String range = substr.substring("OVINE:".length());
			String message = "ASK:OVINE";
			requireInput(message, range);
		} else if(substr.startsWith("CARD:")) {
			cView.showListCards(listItems);
			String range = substr.substring("CARD:".length());
			String message = "ASK:CARD";
			requireInput(message, range);
		} else if(substr.startsWith("REGION:")) {
			cView.showListRegions(listItems);
			String range = substr.substring("REGION:".length());
			String message = "ASK:REGION";
			requireInput(message, range);
		} else if(substr.startsWith("MARKET:PUT:")) {
			TerrainCard card = (TerrainCard) listItems.get(0);
			String range = substr.substring("MARKET:PUT:".length());
			String message = "ASK:PUT:"+card.getTerrainType().name();
			requireInput(message, range);
		} else if(substr.startsWith("MARKET:PRICE:")) {
			String range = substr.substring("MARKET:PRICE:".length());
			String message = "ASK:PRICE";
			requireInput(message, range);
		} else if(substr.startsWith("MARKET:BUY:")) {
			cView.showListCardsMarket(listItems);
			String range = substr.substring("MARKET:BUY:".length());
			String message = "ASK:BUY:"+Integer.toString(listItems.size());
			requireInput(message, range);
		}	
	}

	/**
	 * This method requires input from view.
	 * 
	 * @param message string
	 * @param range string
	 * @throws IOException
	 */
	private void requireInput(String message, String range) throws IOException {
		String minStr = range.substring(0,range.indexOf(':'));
		String maxStr = range.substring(range.indexOf(':')+1);
		int min = Integer.parseInt(minStr);
		int max = Integer.parseInt(maxStr);
		int num = cView.askInput(message,min,max);
		socketOut.writeObject(Integer.valueOf(num));
		socketOut.flush();
	}

	/**
	 * This method analyzes the update.
	 * 
	 * @param substr
	 */
	private void parseUpdate(String substr) {
		if("ANIMALS".equals(substr)) {
			cView.showAnimals();
		} else if("SHEPHERD".equals(substr)) {
			cView.showShepherd();
		} else if("BLACKSHEEP".equals(substr)) {
			cView.showBlackSheep();
		} else if("WOLF".equals(substr)) {
			cView.showWolf();
		}		
	}
	
	/**
	 * This method closes connetion.
	 */
	private void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Impossibile chiudere la connesione socket",e);
		}
		
	}

	/**
	 * This method check if the connection with the server is Up.
	 */
	@Override
	public boolean checkConnection() {
		try {
			socketOut.writeObject("CHECK_CONNECTED");
			socketIn.readObject();
			return true;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "The connection is down",e);
			return false;
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "Errore inatteso: class not found exception",e);
			return true;
		}
	}
}