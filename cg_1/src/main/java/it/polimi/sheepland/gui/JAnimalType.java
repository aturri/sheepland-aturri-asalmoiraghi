package it.polimi.sheepland.gui;

/**
 * This enum contain all possible animals with a name used for example to get images.
 * 
 * @author Andrea
 *
 */
public enum JAnimalType {
	SHEEP("sheep"),RAM("ram"),LAMB("lamb"),BLACKSHEEP("blacksheep"),WOLF("wolf");
	
	private final String name;
	
	/**
	 * Constructor for type of animal.
	 * 
	 * @param name
	 */
	private JAnimalType(String name) {
		this.name = name;
	}
	
	/**
	 * Getter method for the filename of animal.
	 * 
	 * @return the friendly name string
	 */
	public String getName() {
		return name;
	}
}
