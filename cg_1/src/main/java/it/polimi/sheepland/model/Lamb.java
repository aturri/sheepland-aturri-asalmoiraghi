package it.polimi.sheepland.model;

/**
 * This class represents lambs
 * 
 * @author Andrea
 *
 */
public class Lamb extends Ovine {
	private static final long serialVersionUID = -9136688281288052208L;
	private static final int VALUE = 1;
	private static final int DEFAULT_INITIAL_DAYS = 1;
	private static final int DEFAULT_DAYS_TO_GROW = 2;
	private int remainingDays;

	/**
	 * This method is constructor for lambs.
	 * 
	 * @param region where to create the animal
	 */
	protected Lamb(Region region) {
		super(region);
		this.remainingDays = DEFAULT_INITIAL_DAYS;
	}
	
	/**
	 * This method allow lambs to grow if possible, otherwise increments the counter for days of life.
	 * 
	 * <p>It checks if the lamb is ready to grow and in that case it becomes a sheep or a ram. If the lamb is not ready, it increments the number of day of life.</p>
	 */
	public void grow() {
		if(isReadyToGrow()) {
			region.removeOvine(this);
			new AnimalCreator().factoryOvineNoLamb(region);
		} else {
			remainingDays++;
		}
	}

	/**
	 * This method checks if the lamb is ready to grow.
	 * 
	 * @return true if ready to grow
	 */
	private boolean isReadyToGrow() {
		return remainingDays==DEFAULT_DAYS_TO_GROW;
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
