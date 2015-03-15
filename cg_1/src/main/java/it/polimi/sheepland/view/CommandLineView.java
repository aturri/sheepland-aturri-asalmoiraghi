package it.polimi.sheepland.view;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * This class represents the Command Line Interface View
 * @author Andrea
 *
 */
public class CommandLineView extends View {	
	private Scanner scanner;
	private PrintStream output;
	
	private static final String SEPARATOR = "----------------------------------";

	/**
	 * Constructor for Command Line View
	 * @param deck
	 */
	public CommandLineView(Deck deck) {
		super(deck);
		 this.scanner = new Scanner(System.in);
		 this.output = System.out;
	}
	
	/**
	 * This method receives the notify from model (deck)
	 */
	public void update(Observable o, Object arg) {
		String message = (String)arg;
		if("BlackSheep".equals(message)) {
			output.println("La pecora nera si e' spostata! "+deck.getBlackSheepString());
		} else if("Wolf".equals(message)) {
			output.println("Il lupo si e' spostato! "+deck.getWolfString());			
		} else if("Animals".equals(message)) {
			showAnimals();
		} else if("Player".equals(message)) {
			showPlayer();
		} else if("Shepherd".equals(message)) {
			showShepherd();
		}
	}
	
	/**
	 * This method prints all the ovines
	 */
	private void showAnimals() {
		output.println(SEPARATOR+"\nLista Animali:\n");
		List<Region> listRegions = deck.getRegions();
		output.println(deck.getWolfString());			
		for(Region r: listRegions) {
			List<Ovine> listOvines = r.getListOfOvines();
			for(Ovine ovine: listOvines) {
				output.println(ovine.toString());
			}
		}
		output.println(SEPARATOR);
	}
	
	/**
	 * This method prints the current player data
	 */
	private void showPlayer() {
		output.println(SEPARATOR+"\n"+deck.getCurrentPlayer().getName()+":\n");
		output.println(Integer.toString(deck.getCurrentPlayer().getMoney())+" $");
		List<TerrainCard> listCards = deck.getCurrentPlayerCards();
		for(TerrainCard t: listCards) {
			output.println("Tessera "+t.getTerrainType().getName());
		}
		output.println(SEPARATOR);
	}
	
	/**
	 * This method prints the current shepherd data
	 */
	private void showShepherd() {
		String string = deck.getCurrentShepherd().getName();
		if(deck.getCurrentShepherd().getStreet()!=null) {
			string = string+" @ Strada "+Integer.toString(deck.getCurrentShepherd().getStreet().getId());
		}
		output.println(string);
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
		int num = askInput("Inserisci il numero di giocatori:",MIN_PLAYERS,MAX_PLAYERS);
		changedState(new InputMessage(messageType,num));
		showAnimals();
	}
	
	/**
	 * This method requires user input
	 */
	private int askInput(String message,int min, int max) {
		int num = SETUP_INT;
		do {
			output.println(message);
			if(scanner.hasNextInt()) {
				num = scanner.nextInt();
			} else {
				scanner.next();
			}
		} while(num<min||num>max);	
		return num;
	}

	/**
	 * This method asks the street where the shepherd will be placed
	 */
	@Override
	public void askShepherdPosition() {
		askShepherd(InputMessageType.MOVE_SHEPHERD);
	}
	
	/**
	 * This method asks the street where the shepherd will be placed at the beginning of game
	 */
	@Override
	public void askShepherdInitialPosition() {
		askShepherd(InputMessageType.STREET);	
	}
	
	/**
	 * This method asks for the street
	 * @param messageType
	 */
	private void askShepherd(InputMessageType messageType) {
		int num = askInput("["+getPlayer()+"#"+getShepherd()+"]: inserisci l'ID della strada su cui vuoi muovere il pastore:",MIN_STREET,MAX_STREET);
		Street street = deck.getStreetById(num);
		changedState(new InputMessage(messageType,street));
	}

	/**
	 * This method asks to choose the shepherd
	 */
	@Override
	public void askShepherd() {
		output.println("0: "+deck.getShepherdById(0).getName());
		output.println("1: "+deck.getShepherdById(1).getName());
		InputMessageType messageType = InputMessageType.SHEPHERD;
		int num = askInput("["+getPlayer()+"]: inserisci l'ID del tuo pastore:",MIN_INDEX,MAX_INDEX);
		Shepherd shepherd = deck.getShepherdById(num);
		changedState(new InputMessage(messageType,shepherd));		
	}

	/**
	 * This method asks to choose the turn type
	 * @param listOfTurns
	 */
	@Override
	public void askTurnType(List<TurnType> listOfTurns) {
		for (int i=0;i<listOfTurns.size();i++) {
			TurnType t = listOfTurns.get(i);
			String code = Integer.toString(i);
			output.println(code+": "+t.getName());
		}
		InputMessageType messageType = InputMessageType.TURN;
		int num = askInput("["+getPlayer()+"#"+getShepherd()+"]: inserisci il numero corrispondente al numero di mossa:",MIN_INDEX,listOfTurns.size()-1);
		TurnType turnType = listOfTurns.get(num);
		changedState(new InputMessage(messageType,turnType));
	}

	/**
	 * This method asks to choose an ovine to be moved/killed
	 */
	@Override
	public void askOvine(TurnType turnType) {
		List<Ovine> listOvines = deck.getListOfNeighbourOvines(); 
		for (int i=0;i<listOvines.size();i++) {
			Ovine o = listOvines.get(i);
			String code = Integer.toString(i);
			output.println(code+": "+o.toString());
		}
		InputMessageType messageType = null;
		if(turnType.equals(TurnType.MOVE_OVINE)) {
			messageType = InputMessageType.MOVE_OVINE;
		} else {
			messageType = InputMessageType.KILL_OVINE;
		}
		int num = askInput("["+getPlayer()+"#"+getShepherd()+"]: inserisci l'ID dell'ovino:",MIN_INDEX,listOvines.size()-1);
		Ovine ovine = listOvines.get(num);
		changedState(new InputMessage(messageType,ovine));		
	}

	/**
	 * This method asks to choose a card
	 */
	@Override
	public void askCard() {
		List<TerrainCard> listOfTerrainCards = deck.getListOfAffordableCards();
		for (int i=0;i<listOfTerrainCards.size();i++) {
			TerrainCard t = listOfTerrainCards.get(i);
			String code = Integer.toString(i);
			output.println(code+": "+t.getTerrainType().getName()+" "+Integer.toString(t.getCost())+"$");
		}
		InputMessageType messageType = InputMessageType.BUY_CARD;
		int num = askInput("["+getPlayer()+"#"+getShepherd()+"]: inserisci l'ID della card:",MIN_INDEX,listOfTerrainCards.size()-1);
		TerrainCard terrainCard = listOfTerrainCards.get(num);
		changedState(new InputMessage(messageType,terrainCard));
	}
	
	/**
	 * This method asks to choose a region for copupling
	 */
	@Override
	public void askRegion(TurnType turnType) {
		List<Region> listOfRegions = deck.getNeighbourRegions();
		for (int i=0;i<listOfRegions.size();i++) {
			Region r = listOfRegions.get(i);
			String code = Integer.toString(i);
			output.println(code+": Regione "+Integer.toString(r.getId())+" ("+r.getName()+")");
		}
		InputMessageType messageType = null;
		if(turnType.equals(TurnType.COUPLE1)) {
			messageType = InputMessageType.COUPLE1;			
		} else {
			messageType = InputMessageType.COUPLE2;			
		}
		int num = askInput("["+getPlayer()+"#"+getShepherd()+"]: inserisci l'ID della regione:",MIN_INDEX,listOfRegions.size()-1);
		Region region = listOfRegions.get(num);
		changedState(new InputMessage(messageType,region));		
	}

	/**
	 * This method shows error on CLI
	 */
	@Override
	public void showError(Exception e) {
		output.println(e.getMessage());
	}
	
	/**
	 * This method returns the name of current player
	 * @return name of player
	 */
	private String getPlayer() {
		return deck.getCurrentPlayer().getName();
	}
	
	/**
	 * This method returns the name of current shepherd
	 * @return name of shepherd
	 */
	private String getShepherd() {
		return deck.getCurrentPlayer().getCurrentShepherd().getName();
	}
	
	/**
	 * This method show the total scores of the players at the end of game	 
	 * @param scores 
	 */
	public void showTotalScores(Map<String, Integer> scores){
		output.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
		Iterator<Entry<String, Integer>> it = scores.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Integer> score = it.next();
			output.println(score.getKey()+": "+score.getValue()+" points");
		}
	}
	
	/**
	 * This method says that player didn't get the number of its shepherd street with the dice
	 */
	public void showDice() {
		output.println("Non hai ottenuto il numero della strada sul dado!");
	}

	/**
	 * This method initializes player
	 * It shows a message
	 */
	@Override
	public void initializePlayers() {
		output.println("Giocatori pronti");
	}

	/**
	 * This method asks if player wants to sell the card
	 * @param card
	 */
	@Override
	public void askPutCardForSale(TerrainCard card) {
		output.println("0: No");
		output.println("1: Si");
		int num = askInput("["+getPlayer()+"]: Vuoi mettere in vendita la tessera "+card.getTerrainType().getName()+"?",MIN_INDEX,1);
		Integer response = null;
		if(num==1) {
			int cost = askInput("["+getPlayer()+"]: A quale prezzo?",MIN_INDEX,60);
			response = cost;
		}
		InputMessageType messageType = InputMessageType.MARKET_PUT;
		changedState(new InputMessage(messageType,response));		
	}

	/**
	 * This method asks if player wants to buy the card
	 * @param card
	 */
	@Override
	public void askBuyCardForSale() {
		List<TerrainCard> listOfTerrainCards = deck.getCardsForSale();
		int num = 0;
		if(!listOfTerrainCards.isEmpty()) {
			for (int i=0;i<listOfTerrainCards.size();i++) {
				TerrainCard t = listOfTerrainCards.get(i);
				String code = Integer.toString(i);
				output.println(code+": "+t.getTerrainType().getName()+" "+Integer.toString(t.getCost())+"$");
			}
			output.println(Integer.toString(listOfTerrainCards.size())+": Non acquistare");
			num = askInput("["+getPlayer()+"]: Vuoi acquistare una tessera?",MIN_INDEX,listOfTerrainCards.size());
		}
		InputMessageType messageType = InputMessageType.MARKET_BUY;
		TerrainCard card = null;
		if(num!=listOfTerrainCards.size()) {
			card = listOfTerrainCards.get(num);
		}
		changedState(new InputMessage(messageType,card));		
	}

	/**
	 * This method says that game is ended.
	 */
	@Override
	public void showEnd() {
		output.println("\nParita terminata! Ciao! :)");
	}
}
