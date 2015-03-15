package it.polimi.sheepland.model;

import java.io.Serializable;

/**
 * This class represents the card of terrain.
 * 
 * @author Andrea
 *
 */
public class TerrainCard implements Serializable {
	private static final long serialVersionUID = 5523469194669504648L;
	private int cost;
	private TerrainType terrainType;
	private Player owner =  null;
	
	/**
	 * This method is constructor for TerrainCard.
	 * 
	 * It sets up the cost and terrain type.
	 * 
	 * @param int cost
	 * @param terrainType
	 */
	public TerrainCard(int cost,TerrainType terrainType) {
		if(terrainType!=null) {
			this.cost = cost;
			this.terrainType = terrainType;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * This method is getter for cost.
	 * 
	 * @return the cost of the card
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * This method is getter for terrain type.
	 * 
	 * @return terrainType
	 */
	public TerrainType getTerrainType() {
		return terrainType;
	}

	/**
	 * This method is getter for owner
	 * @return idCard
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * This method is setter for owner.
	 * 
	 * @param the owner
	 */
	public void setOwner(Player owner) {
		if(owner!=null) {
			this.owner = owner;
		}
	}

	/**
	 * This methods is the setter for the cost of the card.
	 * 
	 * @param the cost of the card
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

}
