/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 31/mag/2014
 *
 */

package it.polimi.sheepland.view;

import it.polimi.sheepland.exception.ClientUnreachableException;
import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the view for remote clients.
 * 
 * @author Andrea
 */
public class RemoteView extends View {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final int RECONNECT_WAITING_TIME = 5000;
	
	private static final String UNABLE_MEX = "Unable to contact client ";
	private static final String DISABLE_MEX = ". The client has been disabled.";
	private static final String WAITING_MEX = ". Waiting " + RECONNECT_WAITING_TIME + "ms for reconnection.";
	
	private List<Client> listClients;
	private Map<Player,Communicator> communicatorsMap = new HashMap<Player,Communicator>();
		
	/**
	 * This is the constructor for the class.
	 * Set the deck and the list of the clients
	 * @param deck
	 * @param listClients 
	 */
	public RemoteView(Deck deck, List<Client> listClients) {
		super(deck);
		this.listClients = listClients;
		LOGGER.setLevel(Level.INFO);
	}
	
	/**
	 * This method initialize the players.
	 * For each player create the associated communicator.
	 */
	public void initializePlayers() {
		List<Player> listPlayers = deck.getPlayers();
		for(Player player: listPlayers) {
			int index = listPlayers.indexOf(player);
			Communicator communicator = createCommunicator(listClients.get(index), player);
			communicator.setClientPlayer(player);
		}
	}
	
	/**
	 * This method create a new communicator for the player
	 * @param player 
	 * @param client of the player 
	 * 
	 */
	private Communicator createCommunicator(Client client, Player player) {
		Communicator communicator = getCommunicator(client,player);
		communicator.setObserverOfClient(this);
		communicatorsMap.put(player, communicator);
		communicator.sendTokenToClient();
		showConnection(communicator);
		return communicator;
	}

	/**
	 * This method tells the client that are connectede at the game
	 */
	private void showConnection(Communicator communicator) {
		try {
			communicator.initializeConnection();
		} catch (IOException e) {
			LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
			try {
				waitReconnection(RECONNECT_WAITING_TIME);
				communicator.initializeConnection();
			} catch (IOException e1) {
				disconnectClient(communicator);
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
			}
		}
	}
	
	/**
	 * This method wait a tot time
	 * @param waitingTime
	 */
	private void waitReconnection(int waitingTime){
		try {
			TimeUnit.MILLISECONDS.sleep(waitingTime);
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, "Unexpected error while waiting for reconnection of the client",e);
		}
	}

	/**
	 * This method return an instance of communicator (socket or rmi)
	 * @param object
	 * @return communicator
	 */
	private Communicator getCommunicator(Client client, Player player) {
		if(client instanceof SocketClient){
			return new SocketCommunicator((SocketClient) client, player);
		}else{
			return new RMICommunicator((RMIClient) client, player);
		}
	}
	
	/**
	 * This method reconnect a client
	 * @param reconnectedClient
	 */
	private void reconnectClient(Client reconnectedClient){
		Player player = reconnectedClient.getPlayer();
		createCommunicator(reconnectedClient, player);
		listClients.add(reconnectedClient);
		enablePlayer(player);
		showMessage(player.getName() + " si è riconnesso");
	}

	/**
	 * This method receives the notify from model (deck) or old clients that try to reconnect
	 */
	public void update(Observable o, Object arg) {
		if(o instanceof Client){
			reconnectClient((Client) arg);
		} else {
			String message = (String)arg;
			if("BlackSheep".equals(message)) {
				showBlackSheep();
			} else if("Wolf".equals(message)) {
				showWolf();			
			} else if("Animals".equals(message)) {
				showAnimals();
			} else if("Player".equals(message)) {
				showPlayer();
			} else if("Shepherd".equals(message)) {
				showShepherd();
			}
		}
	}
	
	/**
	 * This method send to client the player's waiting message
	 */
	private void sendWaitingMessage() {
		Communicator currentComunicator = communicatorsMap.get(deck.getCurrentPlayer());
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null && !communicator.equals(currentComunicator)){
				try {
					communicator.waitingMessage(deck.getCurrentPlayer());
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.waitingMessage(deck.getCurrentPlayer());
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	
	/**
	 * This method send the updated client to all clients
	 */
	private void updateClientsDeck() {
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.sendDeck(deck);
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.sendDeck(deck);
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	
	/**
	 * This method prints the current shepherd data
	 */
	private void showShepherd() {
		updateClientsDeck();
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.showShepherd();
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.showShepherd();
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	
	/**
	 * This method prints the current player data
	 */
	private void showPlayer() {
		updateClientsDeck();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				communicator.showPlayer();
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					communicator.showPlayer();
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}
			}
		}
	}
	
	/**
	 * This method prints all the ovines
	 */
	private void showAnimals() {
		updateClientsDeck();
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.showAnimals();
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.showAnimals();
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	
	/**
	 * This method shows the wolf
	 */
	private void showWolf() {
		updateClientsDeck();
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.showWolf();
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.showWolf();
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	
	/**
	 * This method shows the black sheep
	 */
	private void showBlackSheep() {
		updateClientsDeck();
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.showBlackSheep();
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.showBlackSheep();
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	
	/**
	 * This method says that player didn't get the number of its shepherd street with the dice
	 */
	@Override
	public void showDice() {
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				communicator.showDice();
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					communicator.showDice();
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}	
			}
		}
	}
	
	/**
	 * This method tell the client that the game is end
	 */
	@Override
	public void showEnd(){
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.showEnd();
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.showEnd();
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	

	/**
	 * This method show the total scores of the players at the end of game
	 * @param playersScores 
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void showTotalScores(Map<String, Integer> scores){
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.showTotalScores(scores);
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.showTotalScores(scores);
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}	
	
	/**
	 * This method shows error
	 * @param exception
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void showError(Exception error){
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				communicator.showError(error);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					communicator.showError(error);
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}
			}
		}
	}
	
	/**
	 * This method send a message to the client
	 * @param string
	 */
	private void showMessage(String string) {
		for(Communicator communicator: communicatorsMap.values()){
			if(communicator!=null){
				try {
					communicator.showDisconnection(string);
				} catch (IOException e) {
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
					try {
						waitReconnection(RECONNECT_WAITING_TIME);
						communicator.showDisconnection(string);
					} catch (IOException e1) {
						disconnectClient(communicator);
						LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					}
				}
			}
		}
	}
	
	/**
	 * This method sets the status changed and notifies observers
	 * @param message
	 */
	private void changedState(InputMessage message) {
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * This method asks the number of players and changes status
	 */
	@Override
	public void askNumPlayers() {
		InputMessageType messageType = InputMessageType.NUM_PLAYERS;
		changedState(new InputMessage(messageType,listClients.size()));
		showAnimals();
	}

	/**
	 * This method asks for shepherd position
	 * @throws ClientUnreachableException 
	 */
	public void askShepherdPosition() {
		askShepherdNewStreet(InputMessageType.MOVE_SHEPHERD);
	}
	
	/**
	 * This method asks for shepherd initial position
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askShepherdInitialPosition(){
		askShepherdNewStreet(InputMessageType.STREET);	
	}

	/**
	 * This method asks for street to put there the shepherd
	 * @throws ClientUnreachableException 
	 */
	private void askShepherdNewStreet(InputMessageType messageType){
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				askShepherdNewStreetToCommunicator(messageType);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					askShepherdNewStreetToCommunicator(messageType);
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}
			}
		}
	}
	
	/**
	 * This method asks for street to move shepherd
	 * @param messageType
	 * @throws IOException
	 */
	private void askShepherdNewStreetToCommunicator(InputMessageType messageType) throws IOException{
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			int num;
			num = communicator.askShepherdNewStreet(MIN_STREET,MAX_STREET);
			Street street = deck.getStreetById(num);
			changedState(new InputMessage(messageType,street));
		}
	}
	
	/**
	 * This method asks for shepherd
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askShepherd() throws ClientUnreachableException {
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				askShepherdToCommunicator();
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					askShepherdToCommunicator();
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					throw new ClientUnreachableException();
				}
			}
		}
	}
	
	/**
	 * This method asks the shepherd to communicator.
	 * @throws IOException
	 */
	private void askShepherdToCommunicator() throws IOException{
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			List<Shepherd> playerSepherds = deck.getCurrentPlayerShepherds();
			communicator.showShepherdForSelection(playerSepherds);
			InputMessageType messageType = InputMessageType.SHEPHERD;
			int num = communicator.askSelectedShepherd(MIN_INDEX,MAX_INDEX);
			Shepherd shepherd = deck.getShepherdById(num);
			changedState(new InputMessage(messageType,shepherd));
		}
	}

	/**
	 * This method asks for turn type
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askTurnType(List<TurnType> listOfTurns) throws ClientUnreachableException {
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				askTurnTypeToCommunicator(listOfTurns);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					askTurnTypeToCommunicator(listOfTurns);
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					throw new ClientUnreachableException();
				}
			}
		}
	}
	
	/**
	 * This method asks turn type to communicator.
	 * @param listOfTurns
	 * @throws IOException
	 */
	private void askTurnTypeToCommunicator(List<TurnType> listOfTurns) throws IOException{
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			communicator.showTurnType(listOfTurns);
			InputMessageType messageType = InputMessageType.TURN;
			int num = communicator.askSelectedTurn(MIN_INDEX,listOfTurns.size()-1);
			TurnType turnType = listOfTurns.get(num);
			changedState(new InputMessage(messageType,turnType));
		}
	}

	/**
	 * This method asks for ovine
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askOvine(TurnType turnType){
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				askOvineToCommunicator(turnType);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					askOvineToCommunicator(turnType);
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}
			}
		}	
	}
	
	/**
	 * This method asks ovine to communicator
	 * @param turnType
	 * @throws IOException
	 */
	private void askOvineToCommunicator(TurnType turnType) throws IOException{
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			List<Ovine> listOvines = deck.getListOfNeighbourOvines(); 
			communicator.showNeighbourOvines(listOvines);
			InputMessageType messageType = null;
			if(turnType.equals(TurnType.MOVE_OVINE)) {
				messageType = InputMessageType.MOVE_OVINE;
			} else {
				messageType = InputMessageType.KILL_OVINE;
			}
			int num = communicator.askSelectedOvine(MIN_INDEX,listOvines.size()-1);
			Ovine ovine = listOvines.get(num);
			changedState(new InputMessage(messageType,ovine));
		}
	}

	/**
	 * This method asks for card
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askCard(){
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				askCardToCommunicator();
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					askCardToCommunicator();
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}
			}
		}
	}
	
	/**
	 * This method asks card to communicator.
	 * @throws IOException
	 */
	private void askCardToCommunicator() throws IOException{
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			List<TerrainCard> listOfTerrainCards = deck.getListOfAffordableCards();
			communicator.showAffordableCards(listOfTerrainCards);
			InputMessageType messageType = InputMessageType.BUY_CARD;
			int num = communicator.askSelectedCard(MIN_INDEX,listOfTerrainCards.size()-1);
			TerrainCard terrainCard = listOfTerrainCards.get(num);
			changedState(new InputMessage(messageType,terrainCard));
		}
	}
	
	/**
	 * This method asks for region
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askRegion(TurnType turnType){
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				askRegionToCommunicator(turnType);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					askRegionToCommunicator(turnType);
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}
			}
		}
	}
	
	/**
	 * This method asks region to communicator
	 * @param turnType
	 * @throws IOException
	 */
	private void askRegionToCommunicator(TurnType turnType) throws IOException{
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			List<Region> listOfRegions = deck.getNeighbourRegions();
			communicator.showNeighbourRegions(listOfRegions);
			InputMessageType messageType = null;
			if(turnType.equals(TurnType.COUPLE1)) {
				messageType = InputMessageType.COUPLE1;			
			} else {
				messageType = InputMessageType.COUPLE2;			
			}
			int num = communicator.askSelectedRegion(MIN_INDEX,listOfRegions.size()-1);
			Region region = listOfRegions.get(num);
			changedState(new InputMessage(messageType,region));	
		}
	}

	/**
	 * This method asks if player wants to sell the card
	 * @param card
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askPutCardForSale(TerrainCard card) throws ClientUnreachableException {
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			try {
				askPutCardForSaleToCommunicator(card);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					askPutCardForSaleToCommunicator(card);
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
					throw new ClientUnreachableException();
				}
			}
		}
	}
	
	/**
	 * This method asks card to put for sale to communicator
	 * @param card
	 * @throws IOException
	 */
	private void askPutCardForSaleToCommunicator(TerrainCard card) throws IOException{
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			communicator.showCardToPutForSale(card);
			int num = communicator.askCardToPutForSale(MIN_INDEX,1);
			Integer response = null;
			if(num==1) {
				int cost = communicator.askPriceForCardOnSale(MIN_INDEX,60);
				response = cost;
			}
			InputMessageType messageType = Enum.valueOf(InputMessageType.class,"MARKET_PUT");
			changedState(new InputMessage(messageType,response));	
		}
	}

	/**
	 * This method asks if player wants to buy the card
	 * @param card
	 * @throws ClientUnreachableException 
	 */
	@Override
	public void askBuyCardForSale(){
		updateClientsDeck();
		sendWaitingMessage();
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		TerrainCard card = null;
		if(communicator!=null){
			try {
				card = askBuyCardForSale(card);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +WAITING_MEX,e);
				try {
					waitReconnection(RECONNECT_WAITING_TIME);
					card = askBuyCardForSale(card);
				} catch (IOException e1) {
					disconnectClient(communicator);
					LOGGER.log(Level.INFO, UNABLE_MEX+ communicator.getClientString() +DISABLE_MEX,e1);
				}
			}
			InputMessageType messageType = InputMessageType.MARKET_BUY;
			changedState(new InputMessage(messageType,card));
		}
	}	
	
	/**
	 * This method asks to buy a card for sale
	 * @param receivedCard
	 * @return
	 * @throws IOException
	 */
	private TerrainCard askBuyCardForSale(TerrainCard receivedCard) throws IOException{
		TerrainCard card = receivedCard;
		Communicator communicator = communicatorsMap.get(deck.getCurrentPlayer());
		if(communicator!=null){
			List<TerrainCard> listOfCardsForSale = deck.getCardsForSale();
			if(!listOfCardsForSale.isEmpty()) {
				int num = 0;
				communicator.showCardsForSale(listOfCardsForSale);
				num = communicator.askCardOnSaleToBuy(MIN_INDEX,listOfCardsForSale.size());					
				if(num!=listOfCardsForSale.size()) {
					card = listOfCardsForSale.get(num);
				}
			}
		}
		return card;
	}
	
	/**
	 * This method disconnects a client
	 * @param ObjectOutputStream
	 */
	private void disconnectClient(Communicator diconnectedCommunicator) {
		Player player = null;
		Iterator<Entry<Player, Communicator>> mapIterator = communicatorsMap.entrySet().iterator();
		while(mapIterator.hasNext()){
			Entry<Player, Communicator> setPlayerCommunicator = mapIterator.next();
			if(diconnectedCommunicator.equals(setPlayerCommunicator.getValue())){
				player = setPlayerCommunicator.getKey();
				disablePlayer(player);
				mapIterator.remove();
			}
		}	
		if(player!=null){
			showMessage(player.getName() + " si è disconnesso");
		}
	}

	/**
	 * This method disable the player
	 * @param player
	 */
	private void disablePlayer(Player player) {
		player.setEnabled(false);
	}
	
	/**
	 * This method enable the player
	 * @param player
	 */
	private void enablePlayer(Player player){
		player.setEnabled(true);
	}
}
