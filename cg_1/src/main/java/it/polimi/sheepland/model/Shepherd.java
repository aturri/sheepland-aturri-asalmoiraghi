package it.polimi.sheepland.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the shepherds
 * @author Andrea
 *
 */
public class Shepherd implements Serializable {
	private static final long serialVersionUID = -6240630324755348274L;
	private String name;
	private Street street = null;
	private Player player = null;
	
	private static final int PAY_FOR_SILENCE = 2;
	
	private static final String ILLEGAL_STREET = "Strada non valida";
	private static final String ILLEGAL_ANIMAL = "Animale non valido";
	private static final String ILLEGAL_REGION = "Regione non valida";
	
	/**
	 * This method is constructor for Shepherd
	 * @param name
	 */
	public Shepherd(String name) {
		if(name!=null) {
			this.name = name;
		} else {
			this.name = "Untitled Shepherd";
		}
	}
	
	/**
	 * This method is getter for name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method chekcs if shepherd can move to a street and performs move
	 * If the street is illegal, it throws an exception
	 * @param street
	 */
	public void move(Street street) {
		if(street!=null && street.isEmpty() && street!=this.street) {
			street.setShepherd(this);
			if(this.street!=null) {
				this.street.setFenced();
			}
			this.street = street;
		} else {
			throw new IllegalArgumentException(ILLEGAL_STREET);
		}
	}
	
	/**
	 * This method checks if shepherd can move the selected ovine and performs move
	 * If the chosen ovine is not in a region near the shepherd, it throws an exception 
	 * @param ovine
	 */
	public void moveOvine(Ovine ovine) {
		if(ovine!=null && this.street.isNeighbour(ovine.getRegion())) {
			ovine.move(this.street);
		} else {
			throw new IllegalArgumentException(ILLEGAL_ANIMAL);
		}
	}
	
	/**
	 * This method checks if shepherd can kill the selected ovine and performs it
	 * If the chosen ovine is not in a region near the shepherd, it throws an exception 
	 * @param ovine
	 */
	public void killOvine(Ovine ovine) {
		if(ovine!=null && this.street.isNeighbour(ovine.getRegion())) {
			ovine.kill();
		} else {
			throw new IllegalArgumentException(ILLEGAL_ANIMAL);
		}
	}
	
	/**
	 * This method checks if shepherd can couple 2 sheeps and performs it
	 * @param region
	 */
	public void couple1(Region region) {
		if(region!=null && region.containsTwoSheeps() && street.isNeighbour(region)) {
			new Sheep(region);
		} else {
			throw new IllegalArgumentException(ILLEGAL_REGION);
		}
	}
	
	/**
	 * This method checks if shepherd can couple a sheep and a ram and performs it
	 * @param region
	 */
	public void couple2(Region region) {
		if(region!=null && region.containsRam() && region.containsSheep() && street.isNeighbour(region)) {
			new Lamb(region);
		} else {
			throw new IllegalArgumentException(ILLEGAL_REGION);
		}
	}
	
	/**
	 * This method returns a list of TerrainType, corresponding to the regions near the shepherd
	 * @return listOfTerrainTypes
	 */
	public List<TerrainType> getListOfNearTerrainTypes() {
		List<TerrainType> listOfTerrainTypes = new ArrayList<TerrainType>();
		List<Region> listOfRegions = street.getListOfRegions();
		if(!listOfRegions.isEmpty()) {
			for(Region r: listOfRegions) {
				if(r!=null && !listOfTerrainTypes.contains(r.getTerrainType())) {
					listOfTerrainTypes.add(r.getTerrainType());
				}
			}
		}
		return listOfTerrainTypes;
	}
	
	/**
	 * This method returns the list of neighobour shephers to be payed to get silence while killing an ovine
	 * 
	 * It doesen't get the random int, this is made by Player!
	 * 
	 * @return listOfNeighboursToPay
	 */
	public List<Shepherd> getListOfNeighboursToPay() {
		List<Street> listOfStreets = this.street.getListOfStreets();
		List<Shepherd> listOfNeighboursToPay = new ArrayList<Shepherd>();
		if(!listOfStreets.isEmpty()) {
			listOfNeighboursToPay = createListNeighboursToPay(listOfStreets);
		}
		return listOfNeighboursToPay;
	}
	
	/**
	 * This method creates the list of neighbours to pay, starting from list of streets.
	 * 
	 * @param listOfStreets
	 * @return listOfNeighboursToPay
	 */
	private List<Shepherd> createListNeighboursToPay(List<Street> listOfStreets) {
		Shepherd neighbour = null;
		List<Shepherd> listOfNeighboursToPay = new ArrayList<Shepherd>();
		for(Street s: listOfStreets) {
			if(s.isShepherd()) {
				neighbour = s.getShepherd();
				if(neighbour!=null) {
					listOfNeighboursToPay.add(neighbour);
				}
			}
		}
		return listOfNeighboursToPay;
	}

	/**
	 * This method returns the cost he has to pay to get silence of neighobour shephers
	 * @param list of all neighbours shepherds to be payed
	 * @return cost
	 */
	public int getCostToKillOvine(List<Shepherd> listOfNeighboursToPay) {
		return PAY_FOR_SILENCE * listOfNeighboursToPay.size();
	}
	
	/**
	 * This method says if the shepherd can move in a street next to the current one
	 * @return true if can move on near streets
	 */
	public boolean canMoveNextStreet() {
		List<Street> listOfStreets = this.street.getListOfStreets();
		if(!listOfStreets.isEmpty()) {
			for(Street s: listOfStreets) {
				if(s.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method is getter for street
	 * @return street
	 */
	public Street getStreet() {
		return street;
	}

	/**
	 * Getter method for player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Setter method for player
	 * @param player
	 */
	public void setPlayer(Player player) {
		if(player!=null) {
			this.player = player;
		}
	}
	
	/**
	 * Getter method for PAY_FOR_SILENCE
	 * @return price to pay for silence
	 */
	public int getSumToPayForSilence() {
		return PAY_FOR_SILENCE;
	}
}
