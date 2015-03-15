/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 18/mag/2014
 *
 */

package it.polimi.sheepland.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.sheepland.util.Util;

/**
 * This is an abstract representation of Regions.
 * 
 * @author Andrea
 */
public abstract class Region implements Serializable {
	private static final long serialVersionUID = 3555235208334151740L;
	private int id;
	private TerrainType terrainType;
	
	private List<Ovine> listOvines;
	private List<Street> listStreets;
	
	/**
	 * Constructor for region
	 * @param type
	 * @param id
	 */
	public Region(TerrainType type, int id) {
		this.id = id;
		this.terrainType = type;
		
		listOvines = new ArrayList<Ovine>();
		listStreets = new ArrayList<Street>();	
	}
	
	/**
	 * This method removes the ovine from the list
	 * @param ovine
	 */
	public void removeOvine(Ovine ovine) {
		if(ovine!=null && listOvines.contains(ovine)) {
			listOvines.remove(ovine);
		}
	}

	/**
	 * This method add the ovine in the list
	 * @param ovine
	 */
	public void addOvine(Ovine ovine) {
		if(ovine!=null) {
			listOvines.add(ovine);
		}
	}

	/**
	 * This method returns the street which has the given number
	 * If there is not the number, it returns null
	 * @param num
	 * @return street
	 */
	public Street getStreetByNum(int num) {
		if(!listStreets.isEmpty()) {
			for(Street s: listStreets){
				if(num == s.getNumber()) {
					return s;
				}
			}
		}
		return null;
	}

	/**
	 * This method removes one ovine casually from the list
	 */
	public void removeRandomOvine()  {
		if(!listOvines.isEmpty()) {
			Ovine randomOvine = listOvines.get(random(listOvines.size()-1));
			removeOvine(randomOvine);
		}
	}
	
	/**
	 * This method returns a random int, from 0 to n
	 * @param n
	 * @return radnom int
	 */
	private int random(int max) {
		Random random = new Random();
	    return random.nextInt((max-0)+1)+0;
	}

	/**
	 * This method says if the region is completely closed by fences
	 * @return true if fenced
	 */
	public boolean isAllFenced() {
		if(!listStreets.isEmpty()) {
			for(Street s: listStreets){
				if(!s.isFenced()){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method adds a street in the list
	 * @param street
	 */
	public void addStreet(Street street) {
		if(street!=null) {
			listStreets.add(street);
		}
	}
	
	/**
	 * Getter for list of ovines
	 * @return the list of ovines
	 */
	public List<Ovine> getListOfOvines() {
		return listOvines;
	}
	/**
	 * This method return a list of ovines that corresponds at the type of white ovine passed
	 * @param type the type of white ovine
	 * @return the list of ovines
	 */
	public List<Ovine> getListOfOvines(OvineType type){
		List<Ovine> list = new ArrayList<Ovine>();
		for(Ovine o: listOvines){
			String oClassName = o.getClass().getSimpleName();
			if(oClassName.equals(type.getName())){
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * This method grows all lambs in this region
	 * @throws Exception 
	 */
	public void growLambs() {
		if(this.containsLamb()) {
			List<Ovine> listLambs = getListOfOvines(OvineType.LAMB);
			for(Ovine l: listLambs) {
				((Lamb) l).grow();
			}
		}
	}
	
	/**This method check if there are no ovines in the region
	 * @return true if the region is empty
	 */
	public boolean isEmpty() {
		return listOvines.isEmpty();
	}
	
	/**
	 * getter for terrain type
	 * @return the type
	 */
	public TerrainType getTerrainType() {
		return terrainType;
	}

	/**
	 * This is getter for ID
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * This is getter for name of TerrainType
	 * @return name of terrain type
	 */
	public String getName() {
		return terrainType.getName();
	}

	/**
	 * This method says if in this region there are at least 2 sheeps
	 * @return true if there are >= 2 sheeps
	 */
	public boolean containsTwoSheeps() {
		if(listOvines.size()>=2) {
			int num = 0;
			if(this.containsOvine(BlackSheep.class)){
				num++;
			}
			if((num + getListOfOvines(OvineType.SHEEP).size())>=2){
				return true;
			}
		}
		return false;
	}

	/**
	 * This method says if in this region there is at least a sheep
	 * @return true if there is a sheep
	 */
	public boolean containsSheep() {
		return containsOvine(Sheep.class) || containsOvine(BlackSheep.class);
	}
	
	/**
	 * This method says if in this region there is at least a ram
	 * @return true if there is a ram
	 */
	public boolean containsRam() {
		return containsOvine(Ram.class);
	}
	/**
	 * This method says if in this region there is at least a lamb
	 * @return true if there is a lamb
	 */
	public boolean containsLamb() {
		return containsOvine(Lamb.class);
	}
	
	/**
	 * This method says if in this region there is at least an ovine of the type passed as parameter
	 * @return true if there is an ovine
	 */
	private boolean containsOvine(Class<?> clazz){
		return Util.containsSubClassElement(clazz, listOvines);
	}
}
