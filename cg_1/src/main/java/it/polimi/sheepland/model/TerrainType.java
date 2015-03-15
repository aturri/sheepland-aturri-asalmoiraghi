package it.polimi.sheepland.model;

import java.io.Serializable;

/**
 * This enum represents all terrain's type of the regions.
 */
public enum TerrainType implements Serializable {
	FIELD("Campo"),RIVER("Fiume"),WOOD("Foresta"),LAKE("Lago"),MOUNTAIN("Montagna"),DESERT("Deserto");
	
	private final String name;
	
	/**
	 * Constructor for type of terrain.
	 * 
	 * @param name
	 */
	private TerrainType(String name) {
		this.name = name;
	}
	
	/**
	 * Getter method for name of terrain type.
	 * 
	 * @return the friendly name string
	 */
	public String getName() {
		return name;
	}
}
