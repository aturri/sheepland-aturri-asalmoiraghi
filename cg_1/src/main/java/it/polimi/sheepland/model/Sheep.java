package it.polimi.sheepland.model;

/**
 * This class represents Sheeps.
 * @author Andrea
 *
 */
public class Sheep extends Ovine {
	private static final long serialVersionUID = 3580212437443682601L;
	private static final int VALUE = 1;

	/**
	 * This method is constructor for sheeps.
	 * 
	 * @param region where to create the animal
	 */
	protected Sheep(Region region) {
		super(region);
	}

	/**
	 * This method returns the value of the ovine, to calculate final score.
	 * 
	 * @return the int value
	 */
	@Override
	public int getValue() {
		return VALUE;
	}
}
