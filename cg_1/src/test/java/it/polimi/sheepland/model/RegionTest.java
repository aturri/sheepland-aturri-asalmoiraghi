package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.BlackSheep;
import it.polimi.sheepland.model.Lamb;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.OvineType;
import it.polimi.sheepland.model.Ram;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Sheep;
import it.polimi.sheepland.model.Sheepsburg;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainType;

import org.junit.Before;
import org.junit.Test;

public class RegionTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private Region region = null;
	private Street street1 = null;
	private Street street2 = null;
	private Street street3 = null;
	private Lamb lamb = null;
	@SuppressWarnings("unused")
	private Ram ram = null;
	private Sheep sheep1 = null;
	@SuppressWarnings("unused")
	private Sheep sheep2 = null;

	@Before
	public void setUp() throws Exception {
		region = new NormalRegion(terrainType, 0);
		
		street1= new Street(1, 1);
		street2= new Street(2, 2);
		street3= new Street(3, 3);
		
		region.addStreet(street1);
		region.addStreet(street2);
		region.addStreet(street3);
		
		street1.setRegion(region);
		street2.setRegion(region);
		street3.setRegion(region);
	}

	@Test
	public void testTypeSheepBurg() {
		Sheepsburg sheepsburg = new Sheepsburg(0);
		assertTrue(sheepsburg.getTerrainType()==null);
		assertEquals(sheepsburg.getName(),"Sheepsburg");
	}
	
	@Test
	public void testRegion() {
		assertEquals(region.getName(),"Foresta");
	}
	
	@Test
	public void testNoStreets() {
		Region regionNoStreets = new NormalRegion(terrainType, 0);
		assertEquals(regionNoStreets.getStreetByNum(0),null);
	}
	
	@Test
	public void testGetID() {
		assertTrue(region.getId()==0);
	}
	
	@Test
	public void testEmptiness() {
		assertTrue(region.isEmpty());
		new Sheep(region);
		assertFalse(region.isEmpty());
	}
	
	@Test
	public void testGrowLambs() {
		try {
			Lamb lamb1 = new Lamb(region);
			region.growLambs();
			assertTrue(region.getListOfOvines(OvineType.LAMB).contains(lamb1));
			Lamb lamb2 = new Lamb(region);
			region.growLambs();
			assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb1));
			assertTrue(region.getListOfOvines(OvineType.LAMB).contains(lamb2));
			region.growLambs();
			assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb1));
			assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb2));
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testIsAllFencedTrue() {
		street1.setFenced();
		street2.setFenced();
		street3.setFenced();
		assertTrue(region.isAllFenced());
	}
	
	@Test
	public void testIsAllFencedFalse() {
		assertFalse(region.isAllFenced());
	}
	
	@Test
	public void testRemoveRandomOvine() {
		lamb = new Lamb(region);
		ram = new Ram(region);
		sheep1 = new Sheep(region);
		sheep2 = new Sheep(region);
		region.removeRandomOvine();
		assertTrue(region.getListOfOvines().size()==3);
		Region regionX = new NormalRegion(terrainType,0);
		regionX.removeRandomOvine();
		assertTrue(regionX.getListOfOvines().isEmpty());
	}
	
	@Test
	public void testGetStreet() {
		Street street = region.getStreetByNum(2);
		assertEquals(street2,street);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddOvine() {
		sheep1 = new Sheep(null);
		region.addOvine(sheep1);
		assertTrue(region.getListOfOvines().contains(sheep1));
	}

	@Test
	public void testRemoveOvine() {
		sheep1 = new Sheep(region);
		region.removeOvine(sheep1);
		assertFalse(region.getListOfOvines().contains(sheep1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddLamb() {
		lamb = new Lamb(null);
		region.addOvine(lamb);
		assertTrue(region.getListOfOvines(OvineType.LAMB).contains(lamb));
	}

	@Test
	public void testRemoveLamb() {
		lamb = new Lamb(region);
		region.removeOvine(lamb);
		assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb));
	}
	
	@Test
	public void testAddRemoveNull() {
		Region regionX = new NormalRegion(terrainType,0);
		sheep1 = new Sheep(regionX);
		assertFalse(region.getListOfOvines().contains(sheep1));
		region.removeOvine(sheep1);
		assertFalse(region.getListOfOvines().contains(lamb));
		lamb = null;
		region.addOvine(lamb);
		region.addOvine(lamb);
		assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb));
		assertFalse(region.getListOfOvines().contains(lamb));
		region.removeOvine(lamb);
		region.removeOvine(lamb);
		assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb));
		assertFalse(region.getListOfOvines().contains(lamb));
	}
	
	@Test
	public void test2Sheeps() {
		assertFalse(region.containsTwoSheeps());
		assertFalse(region.containsSheep());
		assertFalse(region.containsRam());
		sheep1 = new Sheep(region);
		sheep2 = new Sheep(region);
		assertTrue(region.containsTwoSheeps());
	}
	
	@Test
	public void test2Sheeps1() {
		new BlackSheep(region);
		assertTrue(region.containsSheep());
		assertFalse(region.containsRam());
		assertFalse(region.containsTwoSheeps());
		sheep1 = new Sheep(region);
		assertTrue(region.containsTwoSheeps());
	}
	
	@Test
	public void test2Sheeps2() {
		sheep1 = new Sheep(region);
		assertTrue(region.containsSheep());
		assertFalse(region.containsRam());
		ram = new Ram(region);
		assertFalse(region.containsTwoSheeps());
		assertTrue(region.containsRam());
		assertTrue(region.containsSheep());
	}
}
