/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 15/mag/2014
 *
 */

package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.IslandCreator;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Andrea
 */
public class IslandCreatorTest {

	private IslandCreator islandCreator;
	
	/**
	 * Test method for {@link sheepland.model.IslandCreator#IslandCreator(java.lang.String)}.
	 * @throws IOException 
	 */
	@Before
	public void setUp() throws IOException {
		islandCreator = new IslandCreator("src/main/java/media/SheeplandTest.xml");
	}

	/**
	 * Test method for {@link sheepland.model.IslandCreator#getHashMapRegions()}.
	 */
	@Test
	public void testGetHashMapRegions() {
		assertTrue(islandCreator.getHashMapRegions().size()==6);
	}

	/**
	 * Test method for {@link sheepland.model.IslandCreator#getHashMapStreets()}.
	 */
	@Test
	public void testGetHashMapStreets() {
		assertTrue(islandCreator.getHashMapStreets().size()==6);
	}
	
	/**
	 * Test method for {@link sheepland.model.IslandCreator#getSheepsburg()}.
	 */
	@Test
	public void testSheepsburg() {
		assertTrue(islandCreator.getSheepsburg().getId()==0);
	}
	
	@Test(expected = RuntimeException.class)
	public void testNoFile() {
		new IslandCreator(null);
	}

}
