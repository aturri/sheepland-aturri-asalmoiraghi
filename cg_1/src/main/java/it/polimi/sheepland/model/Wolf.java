package it.polimi.sheepland.model;

import it.polimi.sheepland.util.Dice;

/**
 * This class represents Wolfs.
 * 
 * @author Andrea
 *
 */
public class Wolf extends Animal implements BlackAnimal {	
	private static final long serialVersionUID = 2798374516463677536L;

	/**
	 * This method is constructor for wolfs.
	 * 
	 * @param region where to create the animal
	 */
	public Wolf(Region region) {
		if(region!=null) {
			this.region = region;
		} else {
			throw new IllegalArgumentException("Regione non valida");
		}
	}
	
	/**
	 * This method moves the wolf, in a random street.
	 * 
	 * <p>The wolf can move only if the random street (1-6) is not null and is empty.
	 * <br/>But if all the streets of the region are fenced, he can skip the street corresponding to the random number.</p>
	 */
	public void autoMove() {
		int random = new Dice(1,6).throwDice();
		Street street = region.getStreetByNum(random);
		if(street!=null && (region.isAllFenced() || street.isEmpty())) {
			this.move(street);
		}
	}

	/**
	 * This method moves the wolf from a region to another, through a street.
	 * 
	 * <p>If the street is not null, it moves the wolf from a region to another, otherwise it throws an exception.
	 * <br/>When the Wolf moves in a new region, he eats a random ovine, but if he doesen't move, he doesen't eat any ovine.</p>
	 * 
	 * @param street
	 */
	private void move(Street street) {
		if(street!=null) {
			Region newRegion = street.getOppositeRegion(this.region);
			this.region = newRegion;
			//eats an ovine
			newRegion.removeRandomOvine();
		}
	}
}
