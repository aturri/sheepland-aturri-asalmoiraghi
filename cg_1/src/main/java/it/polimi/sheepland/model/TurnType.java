package it.polimi.sheepland.model;

import java.io.Serializable;

/**
 * This enum represents all availables types of turn.
 * 
 * @author Andrea
 *
 */
public enum TurnType implements Serializable {
	MOVE_SHEPHERD ("Muovi pastore"), MOVE_OVINE ("Muovi ovino"), BUY_CARD ("Acquista tessera"), KILL_OVINE ("Abbatti ovino"), COUPLE1 ("Accoppiamento pecore (dado)"), COUPLE2("Accoppiamento pecora montone");
	
	private final String name;
	
	/**
	 * Constructor for type of turn.
	 * 
	 * @param name
	 */
	private TurnType(String name) {
		this.name = name;
	}
	
	/**
	 * Getter method for name of turn.
	 * 
	 * @return the friendly name string
	 */
	public String getName() {
		return name;
	}
}
