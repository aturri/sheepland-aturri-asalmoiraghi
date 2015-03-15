/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 17/mag/2014
 *
 */

package it.polimi.sheepland.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class represents the dealer
 * @author Andrea
 */
public class Dealer implements Serializable {
	private static final long serialVersionUID = -6431369764793623201L;
	private Map<TerrainType,List<TerrainCard>> cards = new HashMap<TerrainType,List<TerrainCard>>();
	private List<TerrainCard> listCardsForSale = new ArrayList<TerrainCard>();
	private List<TerrainCard> initialCards = new ArrayList<TerrainCard>();
	/**
	 * This is constructor for dealer
	 * @param cards the cards available
	 * @param initialCards the available initial cards to give to players
	 */
	public Dealer(Map<TerrainType,List<TerrainCard>> cards, List<TerrainCard> initialCards){
		this.cards = cards;
		this.initialCards = initialCards;
	}


	/**
	 * This method check if there is a card available of a terrain type
	 * @param terrainType the type of terrain
	 * @return true if the card is available
	 */
	public boolean isCardAvailable(TerrainType terrainType) {
		if(terrainType!=null) {
			return !cards.get(terrainType).isEmpty();
		}
		return false;
	}


	/**
	 * This method returns the first affordable card of a terrain type
	 * @param listOfNearTerrainTypes
	 * @return the card
	 */
	public TerrainCard getAffordableCard(TerrainType terrainType) {
		if(terrainType!=null && cards.get(terrainType).size()>0) {
			return cards.get(terrainType).get(0);
		}
		return null;
	}


	/**
	 * This method add the terrain card to the player and remove it from the available cards
	 * @param terrainCard the sold card
	 * @param player the player that bought the card 
	 */
	public void sellCard(TerrainCard terrainCard, Player player){
		if(player==null) {
			throw new IllegalArgumentException("Giocatore non valido");
		}
		try {
			player.buyTerrain(terrainCard);
			//if exception isn't thrown
			terrainCard.setOwner(player);
			cards.get(terrainCard.getTerrainType()).remove(terrainCard);
		} catch(Exception e){
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}

	/**
	 * This method add the terrain card to the buyer and remove it from seller
	 * @param terrainCard the sold card
	 * @param player the player that bought the card 
	 */
	public void sellCardForSale(TerrainCard terrainCard, Player player) {
		if(terrainCard==null || !listCardsForSale.contains(terrainCard)) {
			throw new IllegalArgumentException("Carta terreno venduta non valida");
		}
		if(player==null) {
			throw new IllegalArgumentException("Cliente non valido");
		}
		if(player.buyTerrain(terrainCard)){
			terrainCard.getOwner().soldCard(terrainCard);
			terrainCard.setOwner(player);
			listCardsForSale.remove(terrainCard);
		}
	}

	/**
	 * This method returns the cards for sale
	 * @return
	 */
	public List<TerrainCard> getCardsForSale() {
		return listCardsForSale;
	}


	/**
	 * This method puts the card for sale
	 * @param terrainCard
	 */
	public void putCardForSale(TerrainCard terrainCard, int cost) {
		if(terrainCard!=null) {
			terrainCard.setCost(cost);
			listCardsForSale.add(terrainCard);
		} else {
			throw new IllegalArgumentException("Carta terreno non valida");
		}
	}


	/**
	 * This method return a random initial card for the player
	 * @return random card
	 */
	public TerrainCard getRandomInitialCard() {
		TerrainCard initCard = initialCards.get(random(0,initialCards.size()-1));
		initialCards.remove(initCard);
		return initCard;
	}
	
	/**
	 * This method returns a random integer between min and max
	 * @param min The minimum number
	 * @param max The maximum number
	 * @return a random int
	 */
	private int random(int min, int max) {
	    Random random = new Random();
	    return random.nextInt((max-min)+1)+min;
	}

	/**
	 * This method clears the list of cards for market.
	 */
	public void clearListMarket() {
		listCardsForSale.clear();
	}
	
	
}
