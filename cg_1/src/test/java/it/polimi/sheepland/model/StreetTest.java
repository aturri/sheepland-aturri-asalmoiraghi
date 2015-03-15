package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainType;

import org.junit.Before;
import org.junit.Test;

public class StreetTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private Region region0 = null;
	private Region region1 = null;
	private Region region2 = null;
	private Street street1 = null;
	private Street street2 = null;
	private Street street3 = null;
	
	@Before
	public void setUp() throws Exception {
		region0 = new NormalRegion(terrainType, 0);
		region1 = new NormalRegion(terrainType, 1);
		region2 = new NormalRegion(terrainType, 2);
		
		street1= new Street(1, 1);
		street2= new Street(2, 2);
		street3= new Street(3, 3);
		
		region0.addStreet(street1);
		region0.addStreet(street2);
		region0.addStreet(street3);

		region1.addStreet(street1);
		region2.addStreet(street2);
		
		street1.setRegion(region0);
		street2.setRegion(region0);
		street3.setRegion(region0);
		
		street1.setRegion(region1);
		street2.setRegion(region2);
		
		street1.addNextStreet(street2);
		
		street2.addNextStreet(street1);
		street2.addNextStreet(street3);		
		
		street3.addNextStreet(street2);
	}

	@Test
	public void testAddNextNull() {
		Street street4 = null;
		street3.addNextStreet(street4);
		assertFalse(street1.getListOfStreets().contains(street4));
	}
	
	@Test
	public void testSetRegion() {
		assertTrue(street1.getListOfRegions().contains(region1));
		assertTrue(street1.getListOfRegions().contains(region0));
		assertFalse(street1.getListOfRegions().contains(region2));
	}
	
	@Test
	public void testSetRegion1() {
		street1.setRegion(null);
		assertTrue(street1.getListOfRegions().contains(region1));
		assertTrue(street1.getListOfRegions().contains(region0));
	}
	
	@Test
	public void testGetOppositeRegion() {
		assertEquals(street1.getOppositeRegion(region0),region1);
		assertEquals(street1.getOppositeRegion(region1),region0);
	}
	
	@Test
	public void testIsNeighbour() {
		assertTrue(street1.isNeighbour(region0));
		assertFalse(street1.isNeighbour(region2));
		assertFalse(street1.isNeighbour(null));
	}

	@Test
	public void testIsNext() {
		assertTrue(street1.isNext(street2));
		assertFalse(street1.isNext(street3));
		assertFalse(street1.isNext(null));
	}
	
	@Test
	public void testSetShepherd() {
		assertTrue(street1.getShepherd()==null);
		Shepherd shepherd1 = new Shepherd(null);
		street1.setShepherd(shepherd1);
		assertEquals(shepherd1,street1.getShepherd());
		street1.setShepherd(null);
		assertEquals(shepherd1,street1.getShepherd());
	}
	
	@Test
	public void testGetId() {
		assertTrue(street1.getId()==1);
		assertTrue(street2.getId()==2);
		assertTrue(street3.getId()==3);
	}
}
