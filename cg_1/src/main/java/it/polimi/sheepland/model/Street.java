package it.polimi.sheepland.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents streets.
 * 
 * @author Andrea
 *
 */
public class Street implements Serializable {
	private static final long serialVersionUID = 664626997872026691L;
	
	private Region region1 = null;
	private Region region2 = null;
	private int number;
	private int id;
	private boolean fenced;
	private Shepherd shepherd = null;
	private List<Street> listNextStreets = new ArrayList<Street>();
	
	/**
	 * This is constructor for street.
	 * 
	 * It sets up the id and num.
	 * 
	 * @param id
	 * @param num
	 */
	public Street(int id, int num) {
		this.id = id;
		this.number = num;
		this.fenced = false;
	}

	/**
	 * This is getter for id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * This is setter for regions.
	 * 
	 * Every streets has two regions.
	 * 
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		if(region!=null) {
			if(region1==null) {
				this.region1 = region;
			} else {
				this.region2 = region;
			}
		}
	}

	/**
	 * This is getter for number.
	 * 
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * This says if street is fenced.
	 * 
	 * @return true if fenced
	 */
	public boolean isFenced() {
		return fenced;
	}

	/**
	 * This method fences the street.
	 */
	public void setFenced() {
		this.fenced = true;
		this.shepherd = null;
	}

	/**
	 * This method returns the region opposite to the parameter region.
	 * 
	 * @param region
	 * @return the opposite region
	 */
	public Region getOppositeRegion(Region region) {
		if(region1.equals(region)) {
			return region2;
		}
		return region1;
	}

	/**
	 * This method says if there a shepherd on the street.
	 * 
	 * @return true if there is a shepherd on the street
	 */
	public boolean isShepherd() {
		return this.shepherd!=null;
	}

	/**
	 * This method is setter for shepherd.
	 * 
	 * @param shepherd
	 */
	public void setShepherd(Shepherd shepherd) {
		if(shepherd!=null) {
			this.shepherd = shepherd;
		}
	}
	
	/**
	 * This method is getter for the sheperd in this street.
	 * 
	 * @return shepherd
	 */
	public Shepherd getShepherd() {
		return shepherd;
	}

	/**
	 * This method says if the street is empty (no fenced and no shepherd).
	 * 
	 * @return true if there is no shepherd and no fence on the street
	 */
	public boolean isEmpty() {
		return !isFenced() && !isShepherd();
	}

	/**
	 * This method, given a region, says if the street is neighobur to this region.
	 * 
	 * @param region
	 * @return true if region is neighbour the street
	 */
	public boolean isNeighbour(Region region){
		if(region!=null) {
			if(region1==null || region2==null) {
				return false;
			} else {
				return region1.equals(region) || region2.equals(region);
			}
		}
		return false;
	}
	
	/**
	 * Getter for list of regions.
	 * 
	 * @return list neighbour of regions
	 */
	public List<Region> getListOfRegions(){
		List<Region> listRegions = new ArrayList<Region>();
		listRegions.add(region1);
		listRegions.add(region2);
		return listRegions; 
	}
	
	/**
	 * Getter for list of next streets.
	 * 
	 * @return list of next streets
	 */
	public List<Street> getListOfStreets(){
		return listNextStreets; 
	}
	
	/**
	 * This method adds a street in the list.
	 * 
	 * @param street
	 */
	public void addNextStreet(Street street){
		if(street!=null) {
			listNextStreets.add(street);
		}
	}
	
	/**
	 * This method says if a given street is next to the current.
	 * 
	 * @param street
	 * @return true if parameter street is next to the current
	 */
	public boolean isNext(Street street){
		if(street!=null) {
			return listNextStreets.contains(street);
		} else {
			return false;
		}
	}
}
