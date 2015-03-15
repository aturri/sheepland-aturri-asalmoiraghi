package it.polimi.sheepland.controller;

import it.polimi.sheepland.exception.ClientUnreachableException;
import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TerrainType;
import it.polimi.sheepland.model.TurnType;
import it.polimi.sheepland.view.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.sheepland.util.Dice;


/**
 * The controller manages the game, from the initialization to the end.
 * It collects the input from the View class and sends it to the Deck and from the Deck to the View
 * 
 * @author Andrea
 */
public class Controller implements Observer {
	private View view;
	private Deck deck;
	private int numPlayers;
	private List<TurnType> listOfTurns = new ArrayList<TurnType>();
	private TurnType turnType = null;
	private TerrainCard cardForSale = null;

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final String UNR_CLIENT_EX = "Unreachable client";
	
	/**
	 * This is constructor for Controller. It starts the game.
	 * 
	 * @param view
	 * @param deck
	 */
	public Controller(View view, Deck deck) {
		LOGGER.setLevel(Level.INFO);
		//setup MVC
		this.view = view;
		this.deck = deck;
		view.addObserver(this);
	}
	
	/**
	 * This method starts the controller.
	 */
	public void startController() {
		//setup players
		view.askNumPlayers();
		view.update(deck, "Deck");
		
		//play game
		setUpShepherds();
		startGame();
		terminateGame();
	}
	
	/**
	 * This method waits for updates from view.
	 * 
	 * The action depends on the message received
	 */
	public void update(Observable arg0, Object arg) {
		InputMessage message = (InputMessage) arg;
		if(message.getCode()==InputMessageType.NUM_PLAYERS) {
			deck.setNumPlayers((Integer)message.getInput());
			setUpPlayers((Integer)message.getInput());	
		} else if(message.getCode()==InputMessageType.STREET) {
			street(message);
		} else if(message.getCode()==InputMessageType.SHEPHERD) {
			shepherd(message);
		} else if(message.getCode()==InputMessageType.TURN) {
			setTurnType((TurnType)message.getInput());
		} else if(message.getCode()==InputMessageType.MARKET_PUT) {
			putMarket(message);
		} else if(message.getCode()==InputMessageType.MARKET_BUY) {
			buyMarket(message);
		}
	}
	
	/**
	 * This method tries to perform action of sellCardForSale.
	 * 
	 * @param message
	 */
	private void buyMarket(InputMessage message) {
		TerrainCard card = (TerrainCard)message.getInput();
		if(card!=null) {
			try {
				deck.sellCardForSale(card);
			} catch(Exception e) {
				view.showError(e);
				LOGGER.log(Level.FINEST,e.getMessage(),e);
			}
			view.askBuyCardForSale();
		}	
	}

	/**
	 * This method tries to perform action of putCardForSale.
	 * 
	 * @param message
	 */
	private void putMarket(InputMessage message) {
		Integer cost = (Integer)message.getInput();
		if(cost!=null) {
			try {
				deck.putCardForSale(cardForSale, cost);
			} catch(Exception e) {
				view.showError(e);
				LOGGER.log(Level.FINEST,e.getMessage(),e);
			}
		}	
	}

	/**
	 * This method tries to perform action of setCurrentShepherd.
	 * 
	 * @param message
	 */
	private void shepherd(InputMessage message){
		try {
			deck.setCurrentShepherd((Shepherd)message.getInput());
		} catch(Exception e) {
			view.showError(e);
			LOGGER.log(Level.FINEST,e.getMessage(),e);
		}
	}

	/**
	 * This method tries to perform action of setShepherdPosition.
	 * 
	 * @param message
	 */
	private void street(InputMessage message) {
		try {
			deck.setShepherdPosition((Street)message.getInput());
		} catch(Exception e) {
			view.showError(e);
			LOGGER.log(Level.FINEST,e.getMessage(),e);
		}
	}

	/**
	 * This method sets up the players.
	 * 
	 * Asks the view the number of players and sends it to the deck, creating players
	 * This is called when received notify from View
	 */
	private void setUpPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
		for(int i=0;i<numPlayers;i++) {
			String numPlayer = Integer.toString(i+1);
			deck.createPlayer("Giocatore "+numPlayer);
		}
		view.initializePlayers();
	}

	/**
	 * This method sets up the shepherds at the beginning of game.
	 */
	private void setUpShepherds() {
		setUpShepherdForPlayers(0);
		if(numPlayers==2) {
			setUpShepherdForPlayers(1);		
		}
	}
	
	/**
	 * This method sets up a shepherd for every player.
	 * 
	 * @param index of shepherd
	 */
	private void setUpShepherdForPlayers(int index){
		for(int i=0;i<numPlayers;i++) {
			if(!deck.isPlayerEnabled(deck.getPlayers().get(i))) {
				continue;
			}
			deck.setCurrentPlayer(i);
			deck.setCurrentShepherd(index);
			try {
				view.askShepherdInitialPosition();
			} catch (ClientUnreachableException e) {
				LOGGER.log(Level.FINE,UNR_CLIENT_EX,e);
			}
			//if the street is still unset
			if(deck.getCurrentShepherd().getStreet()==null) {
				i--;
			}
		}
	}
	
	/**
	 * This method runs the game.
	 */
	private void startGame() {
		while(deck.getNumFences()>0 && deck.moreThan1Player()) {
			for(int i=0;i<numPlayers;i++) {
				if(!deck.isPlayerEnabled(deck.getPlayers().get(i))) {
					continue;
				}
				deck.setCurrentPlayer(i);
				//this can happen when a player disconnect in the initial phase of setUpShephard
				if(deck.getCurrentShepherd().getStreet()==null) {
					//this situation can happen only with 3 or 4 players
					deck.setCurrentShepherd(0);
					try {
						view.askShepherdInitialPosition();
					} catch (ClientUnreachableException e) {
						LOGGER.log(Level.FINE,UNR_CLIENT_EX,e);
					}
				}
				deck.moveBlackSheep();
				try {
					chooseShepherd();
					threeTurns();
				} catch (ClientUnreachableException e) {
					LOGGER.log(Level.FINE,UNR_CLIENT_EX,e);
				}
				deck.growLambs();
			}
			deck.moveWolf();
			if(deck.moreThan1Player()){
				marketPut();
				marketBuy();
			}
		}
	}
	
	/**
	 * This method sets up current shepherd for this turn.
	 * 
	 * @throws ClientUnreachableException 
	 */
	private void chooseShepherd() throws ClientUnreachableException {
		deck.unsetCurrentShepherd();
		if(numPlayers==2) {
			while(deck.getCurrentShepherd()==null && deck.isPlayerEnabled(deck.getCurrentPlayer())) {
				view.askShepherd();
			}
		} else {
			deck.setCurrentShepherd(0);
		}
	}

	/**
	 * This method asks every player which cards they want to sell.
	 */
	private void marketPut(){
		for(int i=0;i<numPlayers;i++) {
			deck.setCurrentPlayer(i);
			if(!deck.isPlayerEnabled(deck.getCurrentPlayer())) {
				continue;
			}
			if(!deck.getCurrentPlayerCards().isEmpty()) {
				for(TerrainCard card: deck.getCurrentPlayerCards()) {
					cardForSale = card;
					try {
						view.askPutCardForSale(card);
					} catch (ClientUnreachableException e) {
						LOGGER.log(Level.FINE,UNR_CLIENT_EX,e);
					}				
				}
			}
		}
	}
	
	/**
	 * This method let the players buy a card from market.
	 */
	private void marketBuy() {
		int playerNum = new Dice(0,3).throwDice();
		for (int i=0;i<numPlayers;i++) {
			if(!deck.isPlayerEnabled(deck.getCurrentPlayer())) {
				continue;
			}
			if(playerNum==numPlayers) {
				playerNum = 0;
			}
			deck.setCurrentPlayer(playerNum);
				view.askBuyCardForSale();
			playerNum++;
		}
		deck.clearMarketList();
	}

	/**
	 * This method asks the player the three turns ad performs them
	 * 
	 * @throws ClientUnreachableException 
	 */
	private void threeTurns() throws ClientUnreachableException {
		List<TurnType> listOfEffectiveTurns = new ArrayList<TurnType>();
		setListOfTurns();
		for(int j=0;j<3;j++) {
			if(listOfTurns.isEmpty() || !deck.isPlayerEnabled(deck.getCurrentPlayer())) {
				break;
			}
			turnType = null;
			view.askTurnType(listOfTurns);
			try {
				new Turn(turnType, deck, view);
				//if exception isn't thrown
				listOfEffectiveTurns.add(turnType);
				updateListOfTurns(listOfEffectiveTurns);
			} catch(Exception e) {
				//redo the turn
				view.showError(e);
				LOGGER.log(Level.FINEST,e.getMessage(),e);
				j--;
			}
		}
	}
	
	/**
	 * This method sets the turn type.
	 * 
	 * @param turnType
	 */
	private void setTurnType(TurnType turnType) {
		this.turnType = turnType;
	}

	/**
	 * This method returns the updated list of available types of turn.
	 * 
	 * @param list of performed turns
	 * @param last turn type
	 */
	private void updateListOfTurns(List<TurnType> listOfEffectiveTurns) {
		setListOfTurns();
		TurnType moveShepherdTurn = TurnType.MOVE_SHEPHERD;
		if(listOfEffectiveTurns.size()==1 && !listOfEffectiveTurns.contains(moveShepherdTurn)) {
			//if player performed 1 turn, at turn 2 cannot repeat the same turn (unless it was shepherd move)
			listOfTurns.remove(listOfEffectiveTurns.get(0));		
		} else if(listOfEffectiveTurns.size()==2 && !listOfEffectiveTurns.contains(moveShepherdTurn)) {
			//if player performed 2 turns and never moved the shepherd, he has to move it
			listOfTurns.clear();
			listOfTurns.add(moveShepherdTurn);
		} else if(listOfEffectiveTurns.size()==2 && !(listOfEffectiveTurns.get(1)==moveShepherdTurn)) {
			//if player performed 2 turns and already moved the shepherd, at turn 3 cannot repeat the same turn (unless it was shepherd move)
			listOfTurns.remove(listOfEffectiveTurns.get(1));			
		}
	}
	
	/**
	 * This method creates the list of all available list of turns.
	 */
	private void setListOfTurns() {
		if(!listOfTurns.isEmpty()) {
			listOfTurns.clear();
		}
		for(TurnType turn: TurnType.values()) {
			listOfTurns.add(turn);
		}
		//if there are no ovines
		if(!deck.checkOvinesNearShepherd()) {
			listOfTurns.remove(TurnType.MOVE_OVINE);
			listOfTurns.remove(TurnType.KILL_OVINE);
		}
		//if there are no cards available
		if(!deck.isCardAvailable()) {
			listOfTurns.remove(TurnType.BUY_CARD);			
		}
		//if there are < 2 sheeps
		if(!deck.check2Sheeps()) {
			listOfTurns.remove(TurnType.COUPLE1);
		}
		//if there are not a sheep and a ram
		if(!deck.checkSheepAndRam()) {
			listOfTurns.remove(TurnType.COUPLE2);
		}
		//if shepherd cannot move
		if(!deck.canMoveShepherd()) {
			listOfTurns.remove(TurnType.MOVE_SHEPHERD);
		}
	}

	/**
	 * This method ends the game and calculates points.
	 */
	private void terminateGame() {
		//for each region type count the total value
		Map<TerrainType,Integer> scoreByTerrainType = createMapTerrainType();
		//for each player count score
		Map<String,Integer> playersScores = createPlayeScores(scoreByTerrainType);
		//show the scores
		view.showTotalScores(playersScores);
		view.showEnd();
	}

	/**
	 * This method computes final score for every player.
	 * 
	 * @param scoreByTerrainType
	 * @return playersScores map
	 */
	private Map<String, Integer> createPlayeScores(Map<TerrainType, Integer> scoreByTerrainType) {
		Map<String,Integer> playersScores = new HashMap<String,Integer>();
		//for each player
		for(int i=0;i<numPlayers;i++){
			deck.setCurrentPlayer(i);
			//count the number of card for each type
			Map<TerrainType,Integer> numCardsByTerrainType = new HashMap<TerrainType,Integer>();
			for(TerrainType t: TerrainType.values()){
				numCardsByTerrainType.put(t, 0);
			}
			Iterator<TerrainCard> it = deck.getCurrentPlayerCards().iterator();
			while(it.hasNext()){
				TerrainType curType = it.next().getTerrainType();
				numCardsByTerrainType.put(curType, numCardsByTerrainType.get(curType)+1);
			}
			//map player, total score
			int score = 0;
			for(TerrainType t: TerrainType.values()){
				score = score + scoreByTerrainType.get(t)*numCardsByTerrainType.get(t);
			}
			score = score + deck.getCurrentPlayer().getMoney();
			playersScores.put(deck.getCurrentPlayer().getName(),score);
		}
		return playersScores;
	}

	/**
	 * This method calculates the total value for every terrain type.
	 * 
	 * @return a map scoreByTerrainType <TerrainType, Integer>
	 */
	private Map<TerrainType, Integer> createMapTerrainType() {
		Map<TerrainType,Integer> scoreByTerrainType = new HashMap<TerrainType,Integer>();
		for(TerrainType t: TerrainType.values()){
			scoreByTerrainType.put(t, 0);
		}
		for(Region r: deck.getRegions()){
			if(r.getTerrainType()!=null) {
				int totRegion = 0;
				for(Ovine o: r.getListOfOvines()){
					totRegion = totRegion + o.getValue();
				}
				int old = scoreByTerrainType.get(r.getTerrainType());
				totRegion = totRegion+old;
				scoreByTerrainType.put(r.getTerrainType(), totRegion);
			}
		}
		return scoreByTerrainType;
	}
	
}