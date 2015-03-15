package it.polimi.sheepland.view;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

public class RandomView extends View {	

	public RandomView(Deck deck) {
		super(deck);
	}
	
	public void update(Observable o, Object arg) {
		return;
	}
	
	private void changedState(InputMessage message) {
		setChanged();
		notifyObservers(message);
	}
	
	@Override
	public void askNumPlayers() {
		InputMessageType messageType = InputMessageType.valueOf("NUM_PLAYERS");		
		int num = askInput("num_players",MIN_PLAYERS,MAX_PLAYERS);
		changedState(new InputMessage(messageType,num));
	}

	private int askInput(String message,int min, int max) {
		int num = SETUP_INT;
		do {
			num = randomNum(min,max);
		} while(num<min||num>max);	
		return num;
	}
	
	private int randomNum(int min, int max) {
	    Random random = new Random();
	    return random.nextInt((max-min)+1)+min;
	}

	@Override
	public void askShepherdPosition() {
		askShepherd(InputMessageType.valueOf("MOVE_SHEPHERD"));
	}
	
	@Override
	public void askShepherdInitialPosition() {
		askShepherd(InputMessageType.valueOf("STREET"));	
	}
	
	private void askShepherd(InputMessageType messageType) {
		int num = askInput("inserisci_strada",MIN_STREET,MAX_STREET);
		Street street = deck.getStreetById(num);
		changedState(new InputMessage(messageType,street));
	}

	@Override
	public void askShepherd() {
		InputMessageType messageType = InputMessageType.valueOf("SHEPHERD");
		int num = askInput("inserisci_pastore",MIN_INDEX,MAX_INDEX);
		Shepherd shepherd = deck.getShepherdById(num);
		changedState(new InputMessage(messageType,shepherd));		
	}
	
	@Override
	public void askTurnType(List<TurnType> listOfTurns) {
		InputMessageType messageType = InputMessageType.valueOf("TURN");
		int num = askInput("inserisci_mossa",MIN_INDEX,listOfTurns.size()-1);
		TurnType turnType = listOfTurns.get(num);
		changedState(new InputMessage(messageType,turnType));
	}

	@Override
	public void askOvine(TurnType turnType) {
		List<Ovine> listOvines = deck.getListOfNeighbourOvines(); 
		InputMessageType messageType = null;
		if(turnType.equals(TurnType.valueOf("MOVE_OVINE"))) {
			messageType = InputMessageType.valueOf("MOVE_OVINE");
		} else {
			messageType = InputMessageType.valueOf("KILL_OVINE");
		}
		int num = askInput("inserisci_ovino",MIN_INDEX,listOvines.size()-1);
		Ovine ovine = listOvines.get(num);
		changedState(new InputMessage(messageType,ovine));		
	}

	@Override
	public void askCard() {
		List<TerrainCard> listOfTerrainCards = deck.getListOfAffordableCards();
		InputMessageType messageType = InputMessageType.valueOf("BUY_CARD");
		int num = askInput("inserisci_tessera",MIN_INDEX,listOfTerrainCards.size()-1);
		TerrainCard terrainCard = listOfTerrainCards.get(num);
		changedState(new InputMessage(messageType,terrainCard));
	}

	@Override
	public void askRegion(TurnType turnType) {
		List<Region> listOfRegions = deck.getNeighbourRegions();
		InputMessageType messageType = null;
		if(turnType.equals(TurnType.valueOf("COUPLE1"))) {
			messageType = InputMessageType.valueOf("COUPLE1");			
		} else {
			messageType = InputMessageType.valueOf("COUPLE2");			
		}
		int num = askInput("inserisci_regione",MIN_INDEX,listOfRegions.size()-1);
		Region region = listOfRegions.get(num);
		changedState(new InputMessage(messageType,region));		
	}

	@Override
	public void showError(Exception e) {
		return;
	}

	public void showTotalScores(Map<String, Integer> scores){
		return;
	}
	
	public void showDice() {
		return;
	}

	@Override
	public void initializePlayers() {
		return;
	}

	@Override
	public void askPutCardForSale(TerrainCard card) {
		int num = askInput("market_put",MIN_INDEX,1);
		Integer response = null;
		if(num==1) {
			int cost = askInput("market_price",MIN_INDEX,60);
			response = cost;
		}
		InputMessageType messageType = InputMessageType.valueOf("MARKET_PUT");
		changedState(new InputMessage(messageType,response));		
	}

	@Override
	public void askBuyCardForSale() {
		List<TerrainCard> listOfTerrainCards = deck.getCardsForSale();
		int num = 0;
		if(!listOfTerrainCards.isEmpty()) {
			num = askInput("market_buy",MIN_INDEX,listOfTerrainCards.size());
		}
		InputMessageType messageType = InputMessageType.valueOf("MARKET_BUY");
		TerrainCard card = null;
		if(num!=listOfTerrainCards.size()) {
			card = listOfTerrainCards.get(num);
		}
		changedState(new InputMessage(messageType,card));		
	}

	/* (non-Javadoc)
	 * @see sheepland.view.View#showEnd()
	 */
	@Override
	public void showEnd() {
	}
}
