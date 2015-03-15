package it.polimi.sheepland.model;

/**
 * This class represents regions
 * @author Andrea
 *
 */
public class Sheepsburg extends Region {
	private static final long serialVersionUID = -903818216547674994L;
	private static final String NAME = "Sheepsburg";
	
	/**Constructor for the class
	 * @param type
	 * @param id
	 */
	public Sheepsburg(int id) {
		super(null, id);
	}
	
	/**
	 * This is getter for name
	 * @return name
	 */
	@Override
	public String getName() {
		return NAME;
	}
}
