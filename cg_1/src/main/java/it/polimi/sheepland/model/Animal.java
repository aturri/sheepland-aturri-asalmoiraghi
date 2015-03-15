package it.polimi.sheepland.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents all animals.
 * 
 * @author Andrea
 *
 */
public class Animal implements Serializable {
	private static final long serialVersionUID = -137143925439311493L;
	protected Region region;
	//Map to translate the name of the animal from english to italian
	private static final Map<String,String> LANGUAGE;
	static {
		Map<String,String> aMap = new HashMap<String,String>();
		aMap.put("Wolf", "Lupo");
		aMap.put("BlackSheep", "Pecora nera");
		aMap.put("Sheep", "Pecora");
		aMap.put("Ram", "Montone");
		aMap.put("Lamb", "Agnello");
		LANGUAGE = Collections.unmodifiableMap(aMap);
	}
		
	/**
	 * This method is getter for region.
	 * 
	 * @return region of the animal
	 */
	public Region getRegion() {
		return region;
	}
	
	/**
	 * This method returns the formatted string of the object.
	 * 
	 * <p>The string is composed by:
	 * <ul>
	 * 	<li>Animal type</li>
	 * 	<li>Region Id</li>
	 * 	<li>Region type</li>
	 * </ul>
	 * Example: Lupo@Regione 0 (Sheepsburg)</p>
	 * 
	 * @return string
	 */
	public String toString() {
		String regionId = Integer.toString(region.getId());
		String animalName = LANGUAGE.get(this.getClass().getSimpleName());
		return animalName+"@Regione "+regionId+" ("+region.getName()+")";
	}
}
