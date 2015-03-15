package it.polimi.sheepland.model;

/**
 * This class represents represents all types of ovine
 * 
 * @author Andrea
 *
 */
public class Ovine extends Animal {	
	private static final long serialVersionUID = -1495450273054574288L;
	private static int counter;
	private int id;

	/**
	 * This is the constructor method for ovines.
	 * 
	 * <p>If the region is not null, it creates the ovine there, otherwise it throws an exception.</p>
	 * 
	 * @param region 
	 */
	public Ovine(Region region) {
		if(region!=null) {
			id = counter;
			counter++;
			this.region = region;
			this.region.addOvine(this);
		} else {
			throw new IllegalArgumentException("Regione non valida");
		}
	}
	
	/**
	 * This method moves the ovine from a region to another, through a street.
	 * 
	 * <p>If the street is not null, it moves the ovine from a region to another, otherwise it throws an exception</p>
	 * 
	 * @param street
	 */
	public void move(Street street) {
		if(street!=null) {
			region.removeOvine(this);
			Region newRegion = street.getOppositeRegion(region);
			region = newRegion;
			region.addOvine(this);
		}  else {
			throw new IllegalArgumentException("Strada non valida");
		}
	}
	
	/**
	 * This metod kills the ovine by removing it from the region.
	 */
	public void kill() {
		region.removeOvine(this);
	}
	
	/**
	 * This method returns the value of the ovine, to calculate final score.
	 * 
	 * @return the int value
	 */
	public int getValue() {
		return 0;
	}

	/**
	 * This method returns the id of the ovine.
	 * 
	 * @return the int id
	 */
	public int getId() {
		return id;
	}
}
