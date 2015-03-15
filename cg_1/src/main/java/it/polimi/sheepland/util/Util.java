/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 23/mag/2014
 *
 */

package it.polimi.sheepland.util;

import java.util.Collection;


/**
 * This class is an utility
 * @author Andrea
 * 
 */
public class Util {

	/**
	 * This is private constructor to hide the public one.
	 */
	private Util() {
		return;
	}
	
	/**
	 * This class says if a collection contains an element of a given class.
	 * 
	 * @param class
	 * @param collection
	 * @return true if in the collection there is at least an element of the class 
	 */
	public static <T> boolean containsSubClassElement(Class<?> subclass, Collection<T> collection){
		for(T e: collection) {
			if(subclass.isInstance(e)){
				return true;
			}
		}
		return false;
	}
}
