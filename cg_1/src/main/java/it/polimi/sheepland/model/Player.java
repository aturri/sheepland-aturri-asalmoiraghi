package it.polimi.sheepland.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import it.polimi.sheepland.util.Dice;

/**
 * This class represents the players
 * @author Andrea
 *
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 2862450012815628206L;
	
	private List<Shepherd> listShepherds;
	private Map<TerrainType,List<TerrainCard>> boughtCards = new HashMap<TerrainType,List<TerrainCard>>();

	private int money;
	private Shepherd currentShepherd = null;
	private String name;
	private Integer score;
	private boolean enabled = true;
	
	private static final String ILLEGAL_SHEPHERD = "Indice del pastore fuori range!";
	private static final String ILLEGAL_SHEPHERD_1 = "Pastore non valido";
	private static final String NO_MONEY = "Non ci sono soldi a sufficienza";
	private static final String ILLEGAL_CARD = "Carta terreno non valida";
	
	/**
	 * This is constructor for player
	 * @param playerName the name of the player
	 * @param money the initial money of the player
	 * @param listShepherds the list of the shepherds
	 * @param the initial terrain card
	 */
	public Player(String playerName, int money, List<Shepherd> listShepherds, TerrainCard initialCard) {
		this.name = playerName;
		this.money = money;
		this.listShepherds = listShepherds;
		initialCard.setOwner(this);
		
		for(TerrainType t: TerrainType.values()){
			List<TerrainCard> listCards = new ArrayList<TerrainCard>();
			boughtCards.put(t,listCards);
		}
			
		boughtCards.get(initialCard.getTerrainType()).add(initialCard);
	}

	/**
	 * This method is the getter for the player's name
	 * @param playerName
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method adds a card to the list of bought cards of the player
	 * @param terrainCard
	 */
	private void addBoughtCard(TerrainCard terrainCard) {
		boughtCards.get(terrainCard.getTerrainType()).add(terrainCard);
	}
	
	/**
	 * This method removes a card to the list of bought cards of the player
	 * @param terrainCard
	 */
	private void removeBoughtCard(TerrainCard terrainCard) {
		if(boughtCards.get(terrainCard.getTerrainType()).contains(terrainCard)){
			boughtCards.get(terrainCard.getTerrainType()).remove(terrainCard);
		}
	}
	
	/**
	 * This is getter for listBoughtCards
	 * @return list of bought cards
	 */
	public List<TerrainCard> getListBoughtCards() {
		List<TerrainCard> listBoughtCards = new ArrayList<TerrainCard>();
		Iterator<Entry<TerrainType, List<TerrainCard>>> it = boughtCards.entrySet().iterator();
		while (it.hasNext()){
			Iterator<TerrainCard> it2 = it.next().getValue().iterator();
			while(it2.hasNext()){
				listBoughtCards.add(it2.next());
			}
		}
		return listBoughtCards;
	}
	
	/**
	 * This method is getter for the shepherd chosen by player
	 * @return the currentShepherd
	 */
	public Shepherd getCurrentShepherd() {
		return currentShepherd;
	}
	
	/**
	 * This method is setter for the shepherd chosen by player
	 * @param currentShepherd the currentShepherd to set
	 */
	public void setCurrentShepherd(Shepherd shepherd) {
		if(listShepherds.contains(shepherd)) {
			this.currentShepherd = shepherd;
		} else if(listShepherds.size()==1) {
			this.currentShepherd = listShepherds.get(0);
		} else {
			this.currentShepherd = null;
		}
	}
	
	/**
	 * This method is setter for the shepherd chosen by player
	 * @param index
	 */
	public void setCurrentShepherd(int index) {
		if(index >= 0 && index < listShepherds.size()){
			setCurrentShepherd(listShepherds.get(index));
		}else{
			throw new IllegalArgumentException(ILLEGAL_SHEPHERD);
		}
	}
	
	/**This method gets the shepherd by the id
	 * @param index the index of the shepherd in the list of shepherds
	 * @return the shepherd
	 */
	public Shepherd getShepherd(Integer index) {
		if(index >= 0 && index < listShepherds.size()){
			return listShepherds.get(index);
		}else{
			throw new IllegalArgumentException(ILLEGAL_SHEPHERD);
		}
	}
	
	/**
	 * This method is getter for money
	 * @return money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * This method checks if there is enough money to pay
	 * In this case returns true
	 * @param cost
	 * @return true if payed
	 */
	private boolean pay(int cost) {
		if(canAfford(cost)) {
			money = money - cost;
			return true;
		}
		return false;
	}
	
	/**
	 * This method checks if there is enough money to pay
	 * In this case returns true
	 * It gives the money to another player
	 * @param cost
	 * @return true if payed
	 */
	public boolean payToPlayer(int cost, Player player) {
		if(player!=null && pay(cost)) {
			player.recieveMoney(cost);
			return true;
		}
		return false;
	}
	
	/**
	 * This method adds money to player
	 * @param cost
	 */
	private void recieveMoney(int cost) {
		money = money + cost;
	}

	/**
	 * This method says if a palyer can afford something
	 * @param cost
	 * @return true if can afford
	 */
	private boolean canAfford(int cost) {
		if(money>=cost) {
			return true;
		}
		return false;
	}

	/**
	 * This method kills an ovine if possible
	 * Exception thrown if ovine is blacksheep
	 * @param ovine
	 * @throws Exception
	 */
	public void killOvine(Ovine ovine) {
		List<Shepherd> listOfNeighboursToPay = currentShepherd.getListOfNeighboursToPay();
		//launch the dice and remove the shepherd that doesn't reach 5 or 6
		Iterator<Shepherd> it = listOfNeighboursToPay.iterator();
		while(it.hasNext()){
			it.next();
			int num = new Dice(1,6).throwDice();
			if(num<5) {
				it.remove();
			}
		}
		//checks if can pay
		if(canAfford(currentShepherd.getCostToKillOvine(listOfNeighboursToPay))){
			try {
				currentShepherd.killOvine(ovine);
				//if exception isn't thrown
				if(!listOfNeighboursToPay.isEmpty()) {
					for(Shepherd s: listOfNeighboursToPay){
						payToPlayer(s.getSumToPayForSilence(),s.getPlayer());
					}
				}
			} catch(Exception e) {
				throw new IllegalArgumentException(e.getMessage(),e);
			}
		} else {
			throw new IllegalArgumentException(NO_MONEY);
		}
	}
	
	/**
	 * This method determines the cost of placement and performs it if possible
	 * @param street
	 * @throws Exception
	 */
	public void moveShepherd(Street street) {
		int cost = 0;
		if(currentShepherd==null) {
			throw new IllegalArgumentException(ILLEGAL_SHEPHERD_1);
		}
		//if the shepherd doesn't move to near street or it moves for the first time
		if(currentShepherd.getStreet()!=null && !currentShepherd.getStreet().isNext(street)) {
			cost = 1;
		}
		if(pay(cost)) {
			try {
				currentShepherd.move(street);
			} catch(Exception e) {
				this.recieveMoney(cost);
				//pays back
				throw new IllegalArgumentException(e.getMessage(),e);
			}
		} else {
			throw new IllegalArgumentException(NO_MONEY);
		}
	}
	
	/**
	 * This method pays the money to get the terrain card selected
	 * @param terrainCard
	 * @return true if buy the card
	 */
	public boolean buyTerrain(TerrainCard terrainCard) {
		if(terrainCard!=null) {
			int cost = terrainCard.getCost();
			if(pay(cost)) {
				addBoughtCard(terrainCard);
				return true;
			} else {
				throw new IllegalArgumentException(NO_MONEY);
			}
		} else {
			throw new IllegalArgumentException(ILLEGAL_CARD);
		}
	}

	/**
	 * This method remove the sold card
	 * @param terrainCard 
	 */
	public void soldCard(TerrainCard terrainCard) {
		if(terrainCard!=null) {
			recieveMoney(terrainCard.getCost());
			removeBoughtCard(terrainCard);
		}
	}

	/**
	 * This method returns the score of the player
	 * return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * This method sets the score of the player
	 * @param score the score of the player 
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	/**
	 * This method returns list of shepherds.
	 * 
	 * @return list of player's shepherds
	 */
	public List<Shepherd> getShepherds() {
		return listShepherds;
	}

	/**
	 * This method says id player is enabled.
	 * 
	 * @return true if enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * This method says enables or disables player.
	 * 
	 * @param boolean enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
