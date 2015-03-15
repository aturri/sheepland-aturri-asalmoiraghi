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

public class ControllerTesterView extends View {	
	private List<Integer> listInputs;
	private int curInput = 0;
	
	private Map<String, Integer> scores;
	private int countErrors = 0;
	private int moveBlackSheep = 0;
	private int moveWolf = 0;

	public ControllerTesterView(Deck deck, List<Integer> listInputs) {
		super(deck);
		this.listInputs = listInputs;
	}
	
	public void update(Observable o, Object arg) {
		String message = (String)arg;
		if("BlackSheep".equals(message)) {
			moveBlackSheep++;
		} else if("Wolf".equals(message)) {
			moveWolf++;		
		} else if("Animals".equals(message)) {
			return;
		} else if("Player".equals(message)) {
			return;
		} else if("Shepherd".equals(message)) {
			return;
		}
	}
	
	private void changedState(InputMessage message) {
		setChanged();
		notifyObservers(message);
	}
	
	@Override
	public void askNumPlayers() {
		InputMessageType messageType = InputMessageType.valueOf("NUM_PLAYERS");		
		int num = askInput();
		changedState(new InputMessage(messageType,num));
	}
	
	private int askInput() {
		int num = listInputs.get(curInput);
		curInput++;
		return num;
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
		int num = askInput();
		Street street = deck.getStreetById(num);
		changedState(new InputMessage(messageType,street));
	}
	
	@Override
	public void askShepherd() {
		InputMessageType messageType = InputMessageType.valueOf("SHEPHERD");
		int num = askInput();
		Shepherd shepherd = deck.getShepherdById(num);
		changedState(new InputMessage(messageType,shepherd));		
	}

	@Override
	public void askTurnType(List<TurnType> listOfTurns) {
		InputMessageType messageType = InputMessageType.valueOf("TURN");
		int num = askInput();
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
		int num = askInput();
		Ovine ovine = listOvines.get(num);
		changedState(new InputMessage(messageType,ovine));		
	}

	@Override
	public void askCard() {
		List<TerrainCard> listOfTerrainCards = deck.getListOfAffordableCards();
		InputMessageType messageType = InputMessageType.valueOf("BUY_CARD");
		int num = askInput();
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
		int num = askInput();
		Region region = listOfRegions.get(num);
		changedState(new InputMessage(messageType,region));		
	}
	
	@Override
	public void showError(Exception e) {
		countErrors++;
	}
	
	public void showTotalScores(Map<String, Integer> scores){
		this.scores = scores;
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
		int num = askInput();
		Integer response = null;
		if(num==1) {
			int cost = askInput();
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
			num = askInput();
		}
		InputMessageType messageType = InputMessageType.valueOf("MARKET_BUY");
		TerrainCard card = null;
		if(num!=listOfTerrainCards.size()) {
			card = listOfTerrainCards.get(num);
		}
		changedState(new InputMessage(messageType,card));		
	}

	public int getSingleScores(String player) {
		return scores.get(player);
	}

	public int getCountErrors() {
		return countErrors;
	}

	public int getMoveBlackSheep() {
		return moveBlackSheep;
	}

	public int getMoveWolf() {
		return moveWolf;
	}

	/* (non-Javadoc)
	 * @see sheepland.view.View#showEnd()
	 */
	@Override
	public void showEnd() {
	}
}