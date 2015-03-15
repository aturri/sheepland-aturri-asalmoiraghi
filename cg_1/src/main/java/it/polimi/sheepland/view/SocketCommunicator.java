package it.polimi.sheepland.view;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents Socket Communicator View
 * SLP (SheepLandProtocol)
 * 
 * 		Server->Client
 * 		
 * 		CONNECTED:
 * 		WAIT:playerName
 * 		REQUEST:STREET
 * 		REQUEST:SHEPHERD
 * 		REQUEST:TURN
 * 		REQUEST:OVINE
 * 		REQUEST:CARD
 * 		REQUEST:REGION
 * 		REQUEST:MARKET:PUT
 * 		REQUEST:MARKET:PRICE
 * 		REQUEST:MARKET:BUY
 * 		ERROR:message
 * 		SCORE:
 * 		SCORE:END
 * 		TERMINATE:
 * 		DICE:
 * 		BEGIN:SEND:OBJECT
 * 		END:SEND:OBJECT
 * 		BEGIN:SEND:UPDATE:ANIMALS
 * 		BEGIN:SEND:UPDATE:BLACKSHEEP
 * 		BEGIN:SEND:UPDATE:WOLF
 * 		BEGIN:SEND:UPDATE:SHEPHERD
 * 		BEGIN:DECK
 * 		BEGIN:PLAYER
 * 		BEGIN:SHEPHERD
 * 		
 * 
 * 
 * @author Andrea
 *
 */
public class SocketCommunicator extends Communicator {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private SocketClient socketClient;
	private Player player;
	
	private static final String BEGIN_SEND_OBJECT = "BEGIN:SEND:OBJECT";
	private static final String END_SEND_OBJECT = "END:SEND:OBJECT";
	private static final String CONNECTED = "CONNECTED:";
	private static final String WAIT = "WAIT:";
	private static final String REQUIRE_STREET = "REQUIRE:STREET";
	private static final String REQUIRE_SHEPHERD = "REQUIRE:SHEPHERD";
	private static final String REQUIRE_TURN = "REQUIRE:TURN";
	private static final String REQUIRE_OVINE = "REQUIRE:OVINE";
	private static final String REQUIRE_CARD = "REQUIRE:CARD";
	private static final String REQUIRE_REGION = "REQUIRE:REGION";
	private static final String REQUIRE_MARKET_PUT = "REQUIRE:MARKET:PUT";
	private static final String REQUIRE_MARKET_PRICE = "REQUIRE:MARKET:PRICE";
	private static final String REQUIRE_MARKET_BUY = "REQUIRE:MARKET:BUY";
	private static final String ERROR  = "ERROR:";
	private static final String SCORE = "SCORE:";
	private static final String SCORE_END = "SCORE:END";
	private static final String TERMINATE = "TERMINATE:";
	private static final String BEGIN_DECK = "BEGIN:DECK";
	private static final String BEGIN_SEND_UPDATE_WOLF = "BEGIN:SEND:UPDATE:WOLF";
	private static final String BEGIN_SEND_UPDATE_BLACKSHEEP= "BEGIN:SEND:UPDATE:BLACKSHEEP";
	private static final String BEGIN_SEND_UPDATE_SHEPHERD = "BEGIN:SEND:UPDATE:SHEPHERD";
	private static final String BEGIN_SEND_UPDATE_ANIMALS = "BEGIN:SEND:UPDATE:ANIMALS";
	private static final String BEGIN_PLAYER = "BEGIN:PLAYER";
	private static final String DICE = "DICE:";
	
	/**
	 * This is constructor for SocketCommunicator
	 * @param socketClient
	 * @param Player
	 */
	public SocketCommunicator(SocketClient socketClient, Player player) {
		LOGGER.setLevel(Level.INFO);
		this.socketClient = socketClient;
		this.player = player;
	}
	
	/**
	 * This method initialize the object stream for this socket
	 * @param socket
	 */
	public void initializeConnection() throws IOException  {
		socketClient.getOutput().writeObject(CONNECTED);
		socketClient.getOutput().flush();
	}

	/**
	 * This method shows a message in every client, except the one of the current player
	 * @throws IOException 
	 */
	public void waitingMessage(Player currentPlayer) throws IOException {
		socketClient.getOutput().writeObject(WAIT+currentPlayer.getName());
	}
	
	/**
	 * This method asks for input
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private int askInput(String str, int min, int max) throws IOException{
		int response = -1;
		String protocolString = str+":"+Integer.toString(min)+":"+Integer.toString(max);
		socketClient.getOutput().writeObject(protocolString);
		socketClient.getOutput().flush();
		try {
			response = (Integer) socketClient.getInput().readObject();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.INFO, "Error while reading the received object: class not found",e);
		}
		return response;
	}
	
	/**
	 * This method resets the output buffer
	 * @throws IOException 
	 */
	private void resetOutput() throws IOException {
		socketClient.getOutput().reset();
	}
	
	/**
	 * This methods ask for the street where to move the shepherd
	 */
	public int askShepherdNewStreet(int minStreet, int maxStreet) throws IOException{
		return askInput(REQUIRE_STREET,minStreet,maxStreet);
	}

	/**
	 * This method asks for shepherd
	 * @throws IOException 
	 */
	public void showShepherdForSelection(List<Shepherd> playerSepherds) throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_OBJECT);
		for(Shepherd shepherd: playerSepherds){
			socketClient.getOutput().writeObject(shepherd);
		}
		socketClient.getOutput().writeObject(END_SEND_OBJECT);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method asks for shepherd.
	 */
	public int askSelectedShepherd(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_SHEPHERD,minIndex,maxIndex);
	}

	/**
	 * This method asks for turn type
	 * @throws IOException 
	 */
	public void showTurnType(List<TurnType> listOfTurns) throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_OBJECT);
		for (int i=0;i<listOfTurns.size();i++) {
			TurnType t = listOfTurns.get(i);
			socketClient.getOutput().writeObject(t);
		}
		socketClient.getOutput().writeObject(END_SEND_OBJECT);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method asks for turn.
	 */
	public int askSelectedTurn(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_TURN,minIndex,maxIndex);
	}

	/**
	 * This method asks for ovine
	 * @throws IOException 
	 */
	public void showNeighbourOvines(List<Ovine> listOvines) throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_OBJECT);
		for (int i=0;i<listOvines.size();i++) {
			Ovine o = listOvines.get(i);
			socketClient.getOutput().writeObject(o);
		}
		socketClient.getOutput().writeObject(END_SEND_OBJECT);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method asks for ovine.
	 */
	public int askSelectedOvine(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_OVINE,minIndex,maxIndex);
	}

	/**
	 * This method asks for card
	 * @throws IOException 
	 */
	public void showAffordableCards(List<TerrainCard> listOfTerrainCards) throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_OBJECT);
		for (int i=0;i<listOfTerrainCards.size();i++) {
			TerrainCard t = listOfTerrainCards.get(i);
			socketClient.getOutput().writeObject(t);
		}
		socketClient.getOutput().writeObject(END_SEND_OBJECT);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method asks for card.
	 */
	public int askSelectedCard(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_CARD,minIndex,maxIndex);
	}

	/**
	 * This method asks for region
	 * @throws IOException 
	 */
	public void showNeighbourRegions(List<Region> listOfRegions) throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_OBJECT);
		for (int i=0;i<listOfRegions.size();i++) {
			Region r = listOfRegions.get(i);
			socketClient.getOutput().writeObject(r);
		}
		socketClient.getOutput().writeObject(END_SEND_OBJECT);
		socketClient.getOutput().flush();	
	}
	
	/**
	 * This method asks for region
	 */
	public int askSelectedRegion(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_REGION,minIndex,maxIndex);
	}
	
	/**
	 * This method asks if player wants to sell the card
	 * @param card
	 * @throws IOException 
	 */
	public void showCardToPutForSale(TerrainCard card) throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_OBJECT);
		socketClient.getOutput().writeObject(card);
		socketClient.getOutput().writeObject(END_SEND_OBJECT);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method asks for card, if player wants to sell it
	 */
	public int askCardToPutForSale(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_MARKET_PUT,minIndex,maxIndex);
	}
	
	/**
	 * This method asks the price of the card.
	 */
	public int askPriceForCardOnSale(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_MARKET_PRICE,minIndex,maxIndex);
	}

	/**
	 * This method asks if player wants to buy the card
	 * @param card
	 * @throws IOException 
	 */
	public void showCardsForSale(List<TerrainCard> listOfCardsForSale) throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_OBJECT);
		for (int i=0;i<listOfCardsForSale.size();i++) {
			TerrainCard t = listOfCardsForSale.get(i);
			socketClient.getOutput().writeObject(t);
		}
		socketClient.getOutput().writeObject(END_SEND_OBJECT);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method asks for the card to buy on market.
	 */
	public int askCardOnSaleToBuy(int minIndex, int maxIndex) throws IOException{
		return askInput(REQUIRE_MARKET_BUY,minIndex,maxIndex);
	}

	/**
	 * This method shows an error
	 * @throws IOException 
	 */
	public void showError(Exception e) throws IOException {
		socketClient.getOutput().writeObject(ERROR+e.getMessage());
		socketClient.getOutput().flush();
	}

	/**
	 * This method shows total score at the and of the game
	 * @throws IOException 
	 */
	public void showTotalScores(Map<String, Integer> scores) throws IOException{
		socketClient.getOutput().writeObject(SCORE);
		Iterator<Entry<String, Integer>> it = scores.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Integer> score = it.next();
			socketClient.getOutput().writeObject(score.getKey()+":"+score.getValue());
		}
		socketClient.getOutput().writeObject(SCORE_END);
		socketClient.getOutput().writeObject(TERMINATE);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method ends the game
	 * @throws IOException 
	 */
	public void showEnd() throws IOException{
		socketClient.getOutput().writeObject(TERMINATE);
		socketClient.getOutput().flush();
	}

	/**
	 * This method shows message about dice
	 * @throws IOException 
	 */
	public void showDice() throws IOException {
		socketClient.getOutput().writeObject(DICE);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method shows all Animals
	 * @throws IOException 
	 */
	public void showAnimals() throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_UPDATE_ANIMALS);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method shows current Player
	 * @throws IOException 
	 */
	public void showPlayer() throws IOException {
		socketClient.getOutput().writeObject(BEGIN_PLAYER);
		socketClient.getOutput().flush();
	}

	
	/**
	 * This method shows to everyone the updated current shepherd
	 * @throws IOException 
	 */
	public void showShepherd() throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_UPDATE_SHEPHERD);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method shows BalckSheep
	 * @throws IOException 
	 */
	public void showBlackSheep() throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_UPDATE_BLACKSHEEP);
		socketClient.getOutput().flush();
	}
	
	/**
	 * This method shows Wolf
	 * @throws IOException 
	 */
	public void showWolf() throws IOException {
		socketClient.getOutput().writeObject(BEGIN_SEND_UPDATE_WOLF);
		socketClient.getOutput().flush();

	}
	
	/**
	 * This method send the deck
	 * @throws IOException 
	 */
	public void sendDeck(Deck deck) throws IOException {
		resetOutput();
		socketClient.getOutput().writeObject(BEGIN_DECK);
		socketClient.getOutput().writeObject(deck);
		socketClient.getOutput().flush();
	}

	/**
	 * This client check if the connection with the client is up
	 * @return true it the connection is up
	 */
	public boolean checkClientConnected() {
		return socketClient.checkConnected();
	}

	/**
	 * This method disconnect the client
	 */
	public void disconnectClient() {
		socketClient.disconnect();
	}

	/**
	 * This method return the string of the client
	 * @return the string of the client
	 */
	public String getClientString() {
		return socketClient.toString();
	}

	/**
	 * This method check if the player associated at the communicator is enabled
	 * @return true if the associated player is enabled
	 */
	public boolean isEnabled() {
		return player.isEnabled();
	}

	/**
	 * This method show a message at the client when another client is disconnected
	 * @param string
	 */
	public void showDisconnection(String string) throws IOException {
		socketClient.getOutput().writeObject(ERROR+string);
	}

	/**
	 * This method set an observer on the client object
	 * @param remoteView
	 */
	@Override
	public void setObserverOfClient(Observer observer) {
		socketClient.addObserver(observer);
	}

	/**
	 * This method send the token to the client
	 */
	@Override
	public void sendTokenToClient() {
		socketClient.sendToken();
	}

	/**
	 * This method set the player of the client object
	 * @param player
	 */
	@Override
	public void setClientPlayer(Player player) {
		socketClient.setPlayer(player);
	}

	/**
	 * This method return the client object
	 * @return Client
	 */
	@Override
	public Client getClient() {
		return socketClient;
	}
}