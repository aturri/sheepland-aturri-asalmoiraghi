package it.polimi.sheepland.util;

import java.io.Serializable;
import java.util.Random;

/**
 * This class represents the dice
 * @author Andrea
 *
 */
public class Dice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2268126221815823685L;
	private int min;
	private int max;
	
	/**
	 * This is constructor for dice
	 * @param min
	 * @param max
	 */
	public Dice(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	/**
	 * This method throws the dice
	 * @return int
	 */
	public Integer throwDice() {
	    Random random = new Random();
	    return random.nextInt((max-min)+1)+min;
	}
}
