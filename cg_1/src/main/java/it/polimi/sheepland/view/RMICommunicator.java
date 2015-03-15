package it.polimi.sheepland.view;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents RMI Communicator View 
 * 
 * @author Andrea
 *
 */
public class RMICommunicator extends Communicator {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private RMIClient rmiClient;
	private Player player;
	
	/**
	 * This is constructor for RMICommunicator
	 * @param RMIClient
	 * @param Player
	 */
	public RMICommunicator(RMIClient client, Player player){
		LOGGER.setLevel(Level.INFO);
		this.rmiClient = client;
		this.player = player;
	}

	/**
	 * This method send the deck to the client
	 * (If the client use RMI, this method does nothing)
	 * @param deck 
	 * @throws IOException 
	 */
	public void sendDeck(Deck deck) throws RemoteException {
		rmiClient.getRemoteClient().setRemoteDeck(deck);
	}

	/**
	 * This method send to the client the command to show the shepherd
	 * @throws IOException 
	 * 
	 */
	public void showShepherd() throws RemoteException {
		rmiClient.getRemoteClient().showShepherd();
	}

	/**
	 * This method send to the client the command to show the player
	 * @throws IOException 
	 * 
	 */
	public void showPlayer() throws RemoteException {
		rmiClient.getRemoteClient().showPlayer();
	}

	/**
	 * This method send to the client the command to show the animals position
	 * @throws IOException 
	 * 
	 */
	public void showAnimals() throws RemoteException{
		rmiClient.getRemoteClient().showAnimals();
	}

	/**
	 * This method send to the client the command to show the wolf position
	 * @throws IOException 
	 * 
	 */
	public void showWolf() throws RemoteException{
		rmiClient.getRemoteClient().showWolf();
	}

	/**
	 * This method send to the client the command to show the black sheep position
	 * @throws IOException 
	 * 
	 */
	public void showBlackSheep() throws RemoteException{
		rmiClient.getRemoteClient().showBlackSheep();
	}

	/**
	 * This method send to the client the command to show the result of the dice
	 * @throws IOException 
	 * 
	 */
	public void showDice() throws RemoteException{
		rmiClient.getRemoteClient().showError("Non hai ottenuto il numero della strada sul dado!");
	}

	/**
	 * This method notify to the client that the game is over
	 * @throws IOException 
	 * 
	 */
	public void showEnd() throws RemoteException{
		rmiClient.getRemoteClient().terminateGame();
	}

	/**
	 * This method send to the client the command to show the scores of the players
	 * @param scores
	 * @throws IOException 
	 */
	public void showTotalScores(Map<String, Integer> scores) throws RemoteException{
		rmiClient.getRemoteClient().showTotalScores(scores);
	}

	/**
	 * This method send to the client the command to show an error
	 * @param e
	 * @throws IOException 
	 */
	public void showError(Exception error) throws RemoteException{
		rmiClient.getRemoteClient().showError(error.getMessage());
	}

	/**
	 * This method send to the client the command to show the available shepherds for the player
	 * @param playerSepherds
	 * @throws IOException 
	 */
	public void showShepherdForSelection(List<Shepherd> playerSepherds) throws RemoteException {
		rmiClient.getRemoteClient().showShepherdForSelection(playerSepherds);
	}

	/**
	 * This method send to the client the command to show the available turns for the player
	 * @param listOfTurns
	 * @throws IOException 
	 */
	public void showTurnType(List<TurnType> listOfTurns) throws RemoteException {	
		rmiClient.getRemoteClient().showTurnType(listOfTurns);
	}

	/**
	 * This method send to the client the command to show the available ovines to move for the player
	 * @param ovines
	 * @throws IOException 
	 */
	public void showNeighbourOvines(List<Ovine> ovines) throws RemoteException {
		rmiClient.getRemoteClient().showNeighbourOvines(ovines);
	}

	/**
	 * This method send to the client the command to show the affordable cards
	 * @param listOfTerrainCards
	 * @throws IOException 
	 */
	public void showAffordableCards(List<TerrainCard> listOfTerrainCards) throws RemoteException {
		rmiClient.getRemoteClient().showAffordableCards(listOfTerrainCards);
	}

	/**
	 * This method send to the client the command to show the adjacent regions
	 * @param listOfRegions
	 * @throws IOException 
	 */
	public void showNeighbourRegions(List<Region> listOfRegions) throws RemoteException {		
		rmiClient.getRemoteClient().showNeighbourRegions(listOfRegions);
	}

	/**
	 * This method send to the client the command to show the card to put for sale
	 * @param card
	 * @throws IOException 
	 */
	public void showCardToPutForSale(TerrainCard card) throws RemoteException {
		rmiClient.getRemoteClient().showCardToPutForSale(card);
	}

	/**
	 * This method send to the client the command to show the cards for sale
	 * @param listOfCardsForSale
	 * @throws IOException 
	 */
	public void showCardsForSale(List<TerrainCard> listOfCardsForSale) throws RemoteException {
		rmiClient.getRemoteClient().showCardsForSale(listOfCardsForSale);
		
	}

	/**
	 * This method send to the client the command to ask the street where place the shepherd
	 * @param minStreet
	 * @param maxStreet
	 * @return the index of the street
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public int askShepherdNewStreet(int minStreet, int maxStreet) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("STREET",minStreet,maxStreet);
	}

	/**
	 * This method send to the client the command to ask which shepherd to use
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the shepherd
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public int askSelectedShepherd(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("SHEPHERD",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to ask which turn to do
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the selected turn
	 * @throws IOException 
	 */
	public int askSelectedTurn(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("TURN",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to ask which ovine wants the player
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the ovine selected
	 * @throws IOException 
	 */
	public int askSelectedOvine(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("OVINE",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to ask which region wants the player
	 * @param minIndex
	 * @param maxIndex
	 * @return the index of the region
	 * @throws IOException 
	 */
	public int askSelectedCard(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("CARD",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to ask if he want to put for sale the card
	 * @param minIndex
	 * @param maxIndex
	 * @return 0/1 no/yes
	 * @throws IOException 
	 */
	public int askSelectedRegion(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("REGION",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to ask if he want to put for sale the card
	 * @param minIndex
	 * @param maxIndex
	 * @return 0/1 no/yes
	 * @throws IOException 
	 */
	public int askCardToPutForSale(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("CARD_FOR_SALE",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to ask the price for the card putted for sale
	 * @param minIndex
	 * @param maxIndex
	 * @return the price of the card
	 * @throws IOException 
	 */
	public int askPriceForCardOnSale(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("PRICE_FOR_SALE",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to ask if buy the card on sale
	 * @param minIndex
	 * @param maxIndex
	 * @return the card bought
	 * @throws IOException 
	 */
	public int askCardOnSaleToBuy(int minIndex, int maxIndex) throws RemoteException{
		return rmiClient.getRemoteClient().askInput("BUY_CARD",minIndex,maxIndex);
	}

	/**
	 * This method send to the client the command to show to players that are not the current player a message of waiting
	 * @param currentPlayer
	 * @throws IOException 
	 */
	public void waitingMessage(Player currentPlayer) throws RemoteException{
		rmiClient.getRemoteClient().showWaitingMessage(currentPlayer.getName());
	}

	/**
	 * This method tell the client that it's connected at the game
	 * @throws IOException
	 */
	public void initializeConnection() throws RemoteException {
		rmiClient.getRemoteClient().showConnected();
	}

	/**
	 * This client check if the connection with the client is up
	 * @return true it the connection is up
	 */
	public boolean checkClientConnected() {
		return rmiClient.checkConnected();
	}

	/**
	 * This method disconnect the client
	 */
	public void disconnectClient() {
		rmiClient.disconnect();
	}

	/**
	 * This method return the string of the client
	 * @return the string of the client
	 */
	public String getClientString() {
		return rmiClient.toString();
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
		rmiClient.getRemoteClient().showError(string);
	}

	/**
	 * This method set an observer on the client object
	 * @param remoteView
	 */
	@Override
	public void setObserverOfClient(Observer observer) {
		rmiClient.addObserver(observer);
	}

	/**
	 * This method send the token to the client
	 */
	@Override
	public void sendTokenToClient() {
		rmiClient.sendToken();
	}
	
	/**
	 * This method set the player of the client object
	 * @param player
	 */
	@Override
	public void setClientPlayer(Player player) {
		rmiClient.setPlayer(player);
	}
	
	/**
	 * This method return the client object
	 * @return Client
	 */
	@Override
	public Client getClient() {
		return rmiClient;
	}
}