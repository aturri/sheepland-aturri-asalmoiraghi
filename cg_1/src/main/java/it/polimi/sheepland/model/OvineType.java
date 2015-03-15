package it.polimi.sheepland.model;

import java.io.Serializable;

/**
 * This enum contains the possible ovine with its class name
 * 
 * @author Andrea
 */
public enum OvineType implements Serializable {
	SHEEP("Sheep"),RAM("Ram"),LAMB("Lamb"),BLACKSHEEP("BlackSheep");
	
	private final String name;
	
	/**
	 * Constructor for type of animal.
	 * 
	 * @param name
	 */
	private OvineType(String name) {
		this.name = name;
	}
	
	/**
	 * Getter method for the class name of ovine.
	 * 
	 * @return the friendly name string
	 */
	public String getName() {
		return name;
	}
}
