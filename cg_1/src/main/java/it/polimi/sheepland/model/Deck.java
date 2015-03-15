package it.polimi.sheepland.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import it.polimi.sheepland.util.Dice;

/**
 * This class represents the Deck (the board)
 * @author Andrea
 *
 */
public class Deck extends Observable implements Serializable {
	private static final long serialVersionUID = 3504720319260274672L;

	private Dealer dealer;
	
	private Map<Integer,Region> regions;
	private Map<Integer,Street> streets;
	private Region sheepsburg;
	
	private BlackSheep blackSheep;
	private Wolf wolf;
	
	private Dice dice;
	
	private Player currentPlayer;
	private List<Player> listPlayers = new ArrayList<Player>();	
	private Map<Player,Boolean> enablePlayers = new HashMap<Player,Boolean>();

	private List<TerrainCard> listProposedCards = new ArrayList<TerrainCard>();
		
	private int numFences;
	private int numPlayers;
	
	private static final String ISLAND_XML = "src/main/java/media/Sheepland.xml";
	
	private static final int INITIAL_MONEY = 20;
	private static final int INITIAL_MONEY_2_PLAYERS = 30;
	private static final int INITIAL_NUM_FENCES = 20;
	private static final int NUM_OF_CARDS_FOR_TYPE = 5;
	private static final int MIN_DICE = 1;
	private static final int MAX_DICE = 6;
	
	private static final String PLAYER = "Player";
	private static final String SHEPHERD = "Shepherd";
	private static final String ANIMALS = "Animals";
	private static final String BLACKSHEEP = "BlackSheep";
	private static final String WOLF = "Wolf";

	/**
	 * This is constructor for deck
	 * @throws Exception 
	 */
	public Deck() {
		createIsland();
		createAnimals();
		createDealer();
		this.dice = new Dice(MIN_DICE,MAX_DICE);
		this.numFences = INITIAL_NUM_FENCES;	
	}
	
	/**
	 * This method creates the island of the game
	 */
	private void createIsland(){
		IslandCreator islandCreator = new IslandCreator(ISLAND_XML);
		this.regions = islandCreator.getHashMapRegions();
		this.streets = islandCreator.getHashMapStreets();
		this.sheepsburg = islandCreator.getSheepsburg();
	}
	
	/**
	 * this method creates all the animals
	 */
	private void createAnimals(){
		AnimalCreator animalCreator = new AnimalCreator();		
		Iterator<Entry<Integer, Region>> it = regions.entrySet().iterator();
		while (it.hasNext()) {
			Region r = it.next().getValue();
			animalCreator.factoryOvine(r);
		}		 
		//creating black sheep and wolf in sheepsburg
		this.blackSheep = animalCreator.factoryBlackSheep(sheepsburg);
		this.wolf = animalCreator.factoryWolf(sheepsburg);
	}
	
	
	/**
	 * This method creates the dealer
	 */
	private void createDealer(){
		//creating the initial card to give to players
			List<TerrainCard> initCards = new ArrayList<TerrainCard>();		
			//creating the terrain cards and the dealer
			Map<TerrainType,List<TerrainCard>> cards = new HashMap<TerrainType,List<TerrainCard>>();
			for(TerrainType t: TerrainType.values()){
				initCards.add(new TerrainCard(0,t));
				List<TerrainCard> listCards = new ArrayList<TerrainCard>();
				for (int i=0; i<NUM_OF_CARDS_FOR_TYPE; i++){
					listCards.add(new TerrainCard(i,t));
				}
				cards.put(t, listCards);
			}
			this.dealer = (new Dealer(cards,initCards));
	}

	/**
	 * This method return the number of remaining fences
	 * @return the numFences
	 */
	public int getNumFences() {
		return numFences;
	}

	/**
	 * This method sets the number of fences
	 * @param numFences the numFences to set
	 */
	public void setNumFences(int numFences) {
		this.numFences = numFences;
	}
	
	/**
	 * This method sells the card to the player
	 * @param terrainCard
	 */
	public void sellTerrainCard(TerrainCard terrainCard) {
		try {
			dealer.sellCard(terrainCard, currentPlayer);
			changedState(PLAYER);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}
	
	/**
	 * This method puts the card for sale for market
	 * @param terrainCard
	 */
	public void putCardForSale(TerrainCard terrainCard, int cost) {
		try {
			dealer.putCardForSale(terrainCard, cost);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}
	
	/**
	 * This method sells the card for sale for market
	 * @param terrainCard
	 */
	public void sellCardForSale(TerrainCard terrainCard) {
		try {
			dealer.sellCardForSale(terrainCard, currentPlayer);
			changedState(PLAYER);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}
	
	/**
	 * This method gets the cards for sale
	 * @return list of cards for sale
	 */
	public List<TerrainCard> getCardsForSale() {
		listProposedCards = dealer.getCardsForSale();
		return listProposedCards;
	}

	/**
	 * This method returns current player
	 * @return current player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * This method sets current player from index
	 * @param index
	 */
	public void setCurrentPlayer(int index) {
		if(index<listPlayers.size()) {
			currentPlayer = listPlayers.get(index);
			changedState(PLAYER);
		}
	}

	/**
	 * This method creates the player and sets the name
	 * @param playerName
	 */
	public void createPlayer(String playerName) {
		List<Shepherd> listShepherd = new ArrayList<Shepherd>();
		int money = INITIAL_MONEY;
		Shepherd shepherd1 = new Shepherd("Pastore 1");
		Shepherd shepherd2 = null;
		listShepherd.add(shepherd1);
		if(numPlayers==2){
			money = INITIAL_MONEY_2_PLAYERS;
			shepherd2 = new Shepherd("Pastore 2");
			listShepherd.add(shepherd2);
		}
		Player player = new Player(playerName,money,listShepherd,dealer.getRandomInitialCard());
		listPlayers.add(player);
		enablePlayers.put(player, true);
		shepherd1.setPlayer(player);
		if(numPlayers==2) {
			shepherd2.setPlayer(player);
		}
	}

	/**
	 * This method gets the current shepherd
	 * @return current shepherd
	 */
	public Shepherd getCurrentShepherd() {
		return currentPlayer.getCurrentShepherd();
	}
	
	/**
	 * This method sets the current shepherd for current player from index
	 * @param index
	 */
	public void setCurrentShepherd(int index) {
		currentPlayer.setCurrentShepherd(index);
		changedState(SHEPHERD);
	}

	/**
	 * This method sets the current shepherd for current player from object shepherd
	 * @param shepherd
	 */
	public void setCurrentShepherd(Shepherd shepherd) {
		currentPlayer.setCurrentShepherd(shepherd);
		changedState(SHEPHERD);
	}
	
	/**
	 * This method unsets the current shepherd for player
	 */
	public void unsetCurrentShepherd() {
		currentPlayer.setCurrentShepherd(null);
	}
	
	/**
	 * This method moves the shepherd to a street
	 * @param street
	 */
	public void setShepherdPosition(Street street) {
		try {
			Street oldStreet = currentPlayer.getCurrentShepherd().getStreet();
			currentPlayer.moveShepherd(street);
			//if exception isn't thrown
			if(oldStreet!=null) {
				decrementFences();
			}
			changedState(SHEPHERD);
			changedState(PLAYER);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}
	
	/**
	 * This method decrement the number of fences
	 */
	private void decrementFences() {
		this.numFences--;
	}

	/**
	 * This method notify to Observers the change of state
	 */
	private void changedState(String message){
		setChanged();
		notifyObservers(message);
	}

	/**
	 * This method moves the wolf
	 */
	public void moveWolf() {
		if(wolf!=null) {
			wolf.autoMove();
			if(!isBlackSheep()) {
				blackSheep = null;
			}
			changedState(WOLF);
		}
	}
	
	/**
	 * This method says if the black sheep is still present.
	 * 
	 * @return true if black sheep is still present
	 */
	private Boolean isBlackSheep() {
		List<Region> listRegions = getRegions();
		for(Region r: listRegions) {
			if(isHereBlackSheep(r.getListOfOvines())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method says if there is a blacksheep in the list.
	 * 
	 * @param listOvines
	 * @return true if black sheep is in the list
	 */
	private Boolean isHereBlackSheep(List<Ovine> listOvines) {
		for(Ovine ovine: listOvines) {
			if(ovine instanceof BlackSheep) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method moves the black sheep
	 */
	public void moveBlackSheep() {
		if(blackSheep!=null) {
			blackSheep.autoMove();
			changedState(BLACKSHEEP);
		}
	}
	
	/**
	 * This method grows all lambs
	 * @throws Exception 
	 */
	public void growLambs() {
		Iterator<Entry<Integer, Region>> it = regions.entrySet().iterator();
		while (it.hasNext()) {
			Region r = it.next().getValue();
			r.growLambs();
		}	
		changedState(ANIMALS);
	}
	
	/**
	 * This method gets the shepherds of the current player
	 */
	public List<Shepherd> getCurrentPlayerShepherds() {
		return currentPlayer.getShepherds();
	}

	/**
	 * This method gets the shepherd by its id
	 * @param index the id of the shepherd
	 */
	public Shepherd getShepherdById(Integer index) {
		return currentPlayer.getShepherd(index);
	}

	/**
	 * This method gets the street by its id
	 * @param id the id of the street
	 */
	public Street getStreetById(Integer id) {
		return streets.get(id);
	}

	/**
	 * This method checks if there are ovines in the regions near the shepherd
	 * @param shepherd the shepherd
	 * @return true if there are ovines in neighbour regions
	 */
	public boolean checkOvinesNearShepherd() {
		boolean notEmpty = false;
		List<Region> listRegions = getCurrentShepherd().getStreet().getListOfRegions();
		if(!listRegions.isEmpty()) {
			for(Region r: listRegions) {
				notEmpty = notEmpty || !r.isEmpty();
			}
		}
		return notEmpty;
	}

	/**
	 * This method checks if the are available cards of the terrain types near the shepherd
	 * @return true if there are available cards
	 */
	public boolean isCardAvailable() {
		boolean notAvailable = false;
		List<TerrainType> listTypes = getCurrentShepherd().getListOfNearTerrainTypes();
		if(!listTypes.isEmpty()) {
			for(TerrainType t: listTypes) {
				notAvailable = notAvailable || dealer.isCardAvailable(t);
			}
		}
		return notAvailable;
	}
	
	/**
	 * This method gets the list of affordable cards by the current shepherd
	 * @return the list of affordable cards
	 */
	public List<TerrainCard> getListOfAffordableCards(){
		listProposedCards = new ArrayList<TerrainCard>();
		List<TerrainType> listTerrainTypes = getCurrentShepherd().getListOfNearTerrainTypes();
		if(!listTerrainTypes.isEmpty()) {
			for(TerrainType t: listTerrainTypes) {
				if(t!=null && dealer.isCardAvailable(t)) {
					listProposedCards.add(dealer.getAffordableCard(t));
				}
			}
		}
		return listProposedCards;
	}

	/**
	 * This method gets the card proposed by the index (0 / 1)
	 * @param index the index of the selected card
	 * @return the card
	 */
	public TerrainCard getCardById(int index) {
		return listProposedCards.get(index);
	}

	/**
	 * This method returns the list of ovines neighbour to the street of shepherd
	 * @return list of ovines
	 */
	public List<Ovine> getListOfNeighbourOvines() {
		List<Region> listNeighbourRegions = getCurrentShepherd().getStreet().getListOfRegions(); 
		List<Ovine> listOfNeighbourOvines = new ArrayList<Ovine>();
		if(!listNeighbourRegions.isEmpty()) {
			for(Region r: listNeighbourRegions) {
				listOfNeighbourOvines.addAll(r.getListOfOvines());
			}
		}
		return listOfNeighbourOvines;
	}
	
	/**
	 * This method moves the given ovine
	 * @param ovine the ovine to move
	 */
	public void moveOvine(Ovine ovine) {
		try {
			getCurrentShepherd().moveOvine(ovine);
			changedState(ANIMALS);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}

	/**
	 * This method kills the given ovine
	 * @param ovine the ovine to kill
	 */
	public void killOvine(Ovine ovine) {
		try {
			currentPlayer.killOvine(ovine);
			//if exception isn't thrown
			if(ovine instanceof BlackSheep) {
				blackSheep = null;
			}
			changedState(ANIMALS);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}		
	}

	/**
	 * This method couples 2 sheeps (dice)
	 * @param region
	 * @return true if the number of the dice is the same of the current shepherd street, so duplicates sheeps
	 */
	public boolean couple1(Region region) {
		if(region==null || !region.containsTwoSheeps()) {
			throw new IllegalArgumentException("Regione non valida");		
		}
		int num = dice.throwDice();
		if(num==getCurrentShepherd().getStreet().getNumber()) {
			try {
				getCurrentShepherd().couple1(region);
				changedState(ANIMALS);
				return true;
			} catch(Exception e) {
				throw new IllegalArgumentException(e.getMessage(),e);
			}
		}
		return false;
	}

	/**
	 * This method couples a sheep and a ram
	 * @param region
	 */
	public void couple2(Region region) {
		try {
			getCurrentShepherd().couple2(region);
			changedState(ANIMALS);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}
	
	/**
	 * This method sets the number of players
	 * @param numPlayers the number of players
	 */
	public void setNumPlayers(Integer numPlayers) {
		this.numPlayers = numPlayers;
	}

	/**
	 * This method checks if one of the neighbour regions has at least 2 sheeps
	 * @return true if in at least a region there are >= 2 sheeps
	 */
	public boolean check2Sheeps() {
		List<Region> listOfRegions = getNeighbourRegions();
		if(!listOfRegions.isEmpty()) {
			for(Region r: listOfRegions) {
				if(r.containsTwoSheeps()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method checks if one of the neighbour regions has a sheep and a ram
	 * @return true if in at least a region there are a sheep and a ram
	 */
	public boolean checkSheepAndRam() {
		List<Region> listOfRegions = getNeighbourRegions();
		if(!listOfRegions.isEmpty()) {
			for(Region r: listOfRegions) {
				if(r.containsSheep() && r.containsRam()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method returns a list of regions neighbour to the current shepherd
	 * @return list of regions
	 */
	public List<Region> getNeighbourRegions() {
		return getCurrentShepherd().getStreet().getListOfRegions();
	}

	/**
	 * This method returns the list of cards for current player
	 * @return list of bought cards
	 */
	public List<TerrainCard> getCurrentPlayerCards() {
		return currentPlayer.getListBoughtCards();
	}

	/**
	 * This method returns the string of blacksheep
	 * @return string
	 */
	public String getBlackSheepString() {
		return blackSheep.toString();
	}

	/**
	 * This method returns the string of wolf
	 * @return string
	 */
	public String getWolfString() {
		return wolf.toString();
	}

	/**
	 * This method returns the list of all regions
	 * @return list of regions
	 */
	public List<Region> getRegions() {
		List<Region> listRegions = new ArrayList<Region>();
		listRegions.add(sheepsburg);
		listRegions.addAll(regions.values());
		return listRegions;
	}
	
	/**
	 * This method returns the list of all streets
	 * @return list of streets
	 */
	public List<Street> getStreets() {
		List<Street> listStreets = new ArrayList<Street>();
		listStreets.addAll(streets.values());
		return listStreets;
	}

	/**
	 * This method says if shepherd can move
	 * @return true if can move
	 */
	public boolean canMoveShepherd() {
		if(currentPlayer.getMoney()<=0 && !currentPlayer.getCurrentShepherd().canMoveNextStreet()) {
			return false;
		}
		return true;
	}

	/**
	 * This method returns the list of players
	 * @return list of players
	 */
	public List<Player> getPlayers() {
		return listPlayers;
	}

	/**
	 * This method disables the player
	 * @param player
	 */
	public void disablePlayer(Player player) {
		player.setEnabled(false);
	}
	
	/**
	 * This method says if player is enabled
	 * @param player
	 * @return true if is enabled
	 */
	public boolean isPlayerEnabled(Player player) {
		return player.isEnabled();
	}
	
	/**
	 * This method returns the map Player,Enabled
	 * @return map
	 */
	public Map<Player,Boolean> getEnablePlayers() {
		return enablePlayers;
	}
	
	/**
	 * This method says if there are still enough player to continue the game
	 * @return true if there is at least one player
	 */
	public boolean moreThan1Player() {
		int cont = 0;
		for(Player player: listPlayers) {
			if(player.isEnabled()) {
				cont++;
			}
		}
		if(cont>1) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method is getter for the wolf
	 * @return wolf
	 */
	public Wolf getWolf() {
		return wolf;
	}
	
	/**
	 * This method is getter for the balcksheep
	 * @return blackSheep
	 */
	public BlackSheep getBlackSheep() {
		return blackSheep;
	}

	/**
	 * This method clears the list of cards for sale on market.
	 */
	public void clearMarketList() {
		listProposedCards.clear();
		dealer.clearListMarket();
	}
}