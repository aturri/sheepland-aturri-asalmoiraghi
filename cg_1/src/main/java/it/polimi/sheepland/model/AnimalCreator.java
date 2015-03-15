package it.polimi.sheepland.model;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the factory class to create animals.
 * 
 * @author Andrea
 *
 */
public class AnimalCreator implements  Serializable {
	private static final long serialVersionUID = -2028664107548357058L;

	private List<Class<? extends Ovine>> listOvineTypes = new ArrayList<Class<? extends Ovine>>();

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * This is constructor for AnimalCreator.
	 * 
	 * <p>It sets up the logger to level Info and adds elements to the list of possible classes of animals</p>
	 */
	public AnimalCreator(){
		LOGGER.setLevel(Level.INFO);

		listOvineTypes.add(Sheep.class);
		listOvineTypes.add(Ram.class);
		listOvineTypes.add(Lamb.class);
	}
	
	/**
	 * This method creates casually a sheep, a ram or a lamb.
	 * 
	 * @param region the region where to create the ovine
	 * @return the ovine created 
	 */
	public Ovine factoryOvine(Region region) {
		return createOvine(listOvineTypes,region);
	}

	/**
	 * This method creates casually a sheep or a ram.
	 * 
	 * @param region the region where to create the ovine
	 * @return the ovine created
	 */
	public Ovine factoryOvineNoLamb(Region region) {
		List<Class<? extends Ovine>> listOvineTypesNoLamb = listOvineTypes;
		listOvineTypesNoLamb.remove(Lamb.class);
		return createOvine(listOvineTypesNoLamb,region);
	}
	
	/**
	 * This method creates casually an ovine from the list of ovines type.
	 * 
	 * <p>It creates an ovine catching a random class in the list of possible ovine classes.</p>
	 * 
	 * @param list of the classes which represent the types of ovine to create
	 * @param the region of the ovine
	 * @return the ovine created
	 */
	private Ovine createOvine(List<Class<? extends Ovine>> listOvines, Region region) {
		Ovine ovine = null;
		try {
			int random = new Random().nextInt((listOvines.size()-1-0)+1)+0;
			Class<?> myClass = listOvines.get(random);
			Constructor<?> myConstructor;
			myConstructor = myClass.getDeclaredConstructor(Region.class);
			ovine = (Ovine) myConstructor.newInstance(region);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to create ovine",e);
			throw new IllegalArgumentException(e);
		}		
		return ovine;
	}
	
	/**
	 * This method creates a wolf only.
	 * 
	 * @param region the region where to create the wolf
	 * @return the wolf created
	 */
	public Wolf factoryWolf(Region r) {
		try{
			return new Wolf(r);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to create wolf",e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * This method creates a black sheep.
	 * 
	 * @param region the region where to create the black sheep
	 * @return the black sheep created
	 */
	public BlackSheep factoryBlackSheep(Region r) {
		try{
			return new BlackSheep(r);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to create black sheep",e);
			throw new IllegalArgumentException(e);
		}
	}
}
