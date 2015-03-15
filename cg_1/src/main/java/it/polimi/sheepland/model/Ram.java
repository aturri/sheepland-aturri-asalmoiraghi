package it.polimi.sheepland.model;

/**
 * This class represents Rams
 * @author Andrea
 *
 */
public class Ram extends Ovine {
	private static final long serialVersionUID = 5953672778094201420L;
	private static final int VALUE = 1;

	/**
	 * This method is constructor for rams.
	 * 
	 * @param region where to create the animal
	 */
	protected Ram(Region region) {
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