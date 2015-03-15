package it.polimi.sheepland.model;

import it.polimi.sheepland.util.Dice;

/**
 * This class represents Black Sheeps.
 * 
 * @author Andrea
 *
 */
public class BlackSheep extends Ovine implements BlackAnimal {
	private static final long serialVersionUID = -5329430585366647690L;
	private static final int VALUE = 2;
	
	/**
	 * This method is the constructor for black sheep.
	 * 
	 * @param region where to create the animal
	 */
	public BlackSheep(Region region) {
		super(region);
	}

	/**
	 * This method returns the value of the ovine, to calculate final score.
	 * 
	 * @return the int value
	 */
	public int getValue() {
		return VALUE;
	}
	
	/**
	 * This method moves the blacksheep, in a random street.
	 * 
	 * <p>The blacksheep can move only if the random street (1-6) is not null and is empty</p>
	 */
	public void autoMove() {
		int random = new Dice(1,6).throwDice();
		Street street = region.getStreetByNum(random);
		if(street!=null && street.isEmpty()) {
			move(street);
		}
	}
}
