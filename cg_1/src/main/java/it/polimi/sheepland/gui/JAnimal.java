package it.polimi.sheepland.gui;

/**
 * This interface provides an abstract view for JAnimals (the graphical component which represents the animal).
 * 
 * @author Andrea
 *
 */
public interface JAnimal {
	/**
	 * This method moves the animal on the parameter JRegion.
	 * 
	 * @param region - the destination
	 */
	void move(JRegion region);
	
	/**
	 * This method returns the current JRegion of the animal.
	 * 
	 * @return region
	 */
	JRegion getRegion();
	
	/**
	 * This method returns the type of animal.
	 * 
	 * @return type
	 */
	JAnimalType getType();
}
