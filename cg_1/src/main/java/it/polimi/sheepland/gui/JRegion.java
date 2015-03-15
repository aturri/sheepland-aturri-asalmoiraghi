package it.polimi.sheepland.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLayeredPane;

/**
 * This class represents all regions on the island.
 * 
 * @author Andrea
 *
 */
public class JRegion extends JLayeredPane {
	private static final long serialVersionUID = -1356334282484791590L;
	private Map<JAnimalType,Point> offset = new HashMap<JAnimalType,Point>();
	private List<JAnimal> listAnimals = new ArrayList<JAnimal>();
	
	/**
	 * This is constructor for region.
	 * 
	 * <p>The offset is a point where to draw in the region all animals of a certain type.</p>
	 * 
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 * @param offset: a map, the key is JAnimalType and the value is a point
	 */
	public JRegion(int width, int height, int x, int y, Map<JAnimalType,Point> offset) {
		super();
		setSize(new Dimension(width,height));
		setLocation(new Point(x,y));
		this.offset = offset;
	}
	
	/**
	 * This method adds the animal to the list.
	 * 
	 * @param animal
	 */
	public void addAnimal(JAnimal animal) {
		listAnimals.add(animal);
	}
	
	/**
	 * This method removes the animal from the list.
	 * 
	 * @param animal
	 */
	public void removeAnimal(JAnimal animal) {
		listAnimals.remove(animal);
	}

	/**
	 * This method returns X position of the point where to draw the animal.
	 * 
	 * @param JAnimalType
	 * @return x
	 */
	public int getAnimalX(JAnimalType type) {
		int animalOffset = (int) offset.get(type).getX();
		return getX()+animalOffset;
	}
	
	/**
	 * This method returns Y position of the point where to draw the animal.
	 * 
	 * @param JAnimalType
	 * @return y
	 */
	public int getAnimalY(JAnimalType type) {
		int animalOffset = (int) offset.get(type).getY();
		return getY()+animalOffset;
	}

	/**
	 * This method counts the animals of given type in current region.
	 * 
	 * @param type
	 * @return number of animals
	 */
	private int getNumberOf(JAnimalType type) {
		int num = 0;
		for(JAnimal animal: listAnimals) {
			if(animal.getType().equals(type)) {
				num++;
			}
		}
		return num;
	}

	/**
	 * This method updates the counter of number of this type of ovines in the region.
	 * 
	 * @param type
	 */
	public void updateCounter(JAnimalType type) {
		int num = getNumberOf(type);
		String string = null;
		if(num>1) {
			string = Integer.toString(num);
		} else {
			string = "";
		}
		setAnimalString(type, string);
	}

	/**
	 * This method sets the label for all animals of a type in current region
	 * @param type
	 * @param string
	 */
	private void setAnimalString(JAnimalType type, String string) {
		for(JAnimal animal: listAnimals) {
			if((animal instanceof JOvine) && animal.getType().equals(type)) {
				JOvine ovine = (JOvine) animal;
				ovine.setLabel(string);
			}
		}
	}
}