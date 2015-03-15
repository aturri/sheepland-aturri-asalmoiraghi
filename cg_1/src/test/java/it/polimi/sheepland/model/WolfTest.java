package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Sheep;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainType;
import it.polimi.sheepland.model.Wolf;

import org.junit.Before;
import org.junit.Test;

public class WolfTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private Wolf wolf = null;
	private Region region0 = null;
	private Region region1 = null;
	private Region region2 = null;
	private Region region3 = null;
	private Region region4 = null;
	private Region region5 = null;
	private Region region6 = null;
	private Street street1 = null;
	private Street street2 = null;
	private Street street3 = null;
	private Street street4 = null;
	private Street street5 = null;
	private Street street6 = null;
	@SuppressWarnings("unused")
	private Ovine ovine1 = null;
	@SuppressWarnings("unused")
	private Ovine ovine2 = null;
	@SuppressWarnings("unused")
	private Ovine ovine3 = null;
	@SuppressWarnings("unused")
	private Ovine ovine4 = null;
	@SuppressWarnings("unused")
	private Ovine ovine5 = null;
	@SuppressWarnings("unused")
	private Ovine ovine6 = null;

	@Before
	public void setUp() throws Exception {
		region0 = new NormalRegion(terrainType, 0);
		region1 = new NormalRegion(terrainType, 1);
		region2 = new NormalRegion(terrainType, 2);
		region3 = new NormalRegion(terrainType, 3);
		region4 = new NormalRegion(terrainType, 4);
		region5 = new NormalRegion(terrainType, 5);
		region6 = new NormalRegion(terrainType, 6);
		
		street1= new Street(1, 1);
		street2= new Street(2, 2);
		street3= new Street(3, 3);
		street4= new Street(4, 4);
		street5= new Street(5, 5);
		street6= new Street(6, 6);
		
		region0.addStreet(street1);
		region0.addStreet(street2);
		region0.addStreet(street3);
		region0.addStreet(street4);
		region0.addStreet(street5);
		region0.addStreet(street6);

		region1.addStreet(street1);
		region2.addStreet(street2);
		region3.addStreet(street3);
		region4.addStreet(street4);
		region5.addStreet(street5);
		region6.addStreet(street6);
		
		street1.setRegion(region0);
		street2.setRegion(region0);
		street3.setRegion(region0);
		street4.setRegion(region0);
		street5.setRegion(region0);
		street6.setRegion(region0);
		
		street1.setRegion(region1);
		street2.setRegion(region2);
		street3.setRegion(region3);
		street4.setRegion(region4);
		street5.setRegion(region5);
		street6.setRegion(region6);
		
		street1.addNextStreet(street2);
		street1.addNextStreet(street6);
		
		street2.addNextStreet(street1);
		street2.addNextStreet(street3);		
		
		street3.addNextStreet(street2);
		street3.addNextStreet(street4);
		
		street4.addNextStreet(street3);
		street4.addNextStreet(street5);

		street5.addNextStreet(street4);
		street5.addNextStreet(street6);
		
		street6.addNextStreet(street1);
		street6.addNextStreet(street5);
		
		wolf = new Wolf(region0);
		
		ovine1 = new Sheep(region1);
		ovine2 = new Sheep(region2);
		ovine3 = new Sheep(region3);
		ovine4 = new Sheep(region4);
		ovine5 = new Sheep(region5);
		ovine6 = new Sheep(region6);
	}

	@Test
	public void move() {
		assertEquals(wolf.toString(),"Lupo@Regione "+Integer.toString(region0.getId())+" ("+region0.getName()+")");
		wolf.autoMove();
		Region newRegion = wolf.getRegion();
		assertFalse(newRegion==region0);
	}
	
	@Test
	public void eatASheep() {		
		wolf.autoMove();
		Region newRegion = wolf.getRegion();
		assertTrue(newRegion.getListOfOvines().isEmpty());
	}
	
	@Test
	public void moveAllFenced() {
		Shepherd shepherd = new Shepherd("Pastore1");
		street1.setShepherd(shepherd);
		street2.setFenced();
		street3.setFenced();
		street4.setFenced();
		street5.setFenced();
		street6.setFenced();
		
		wolf.autoMove();
		Region newRegion = wolf.getRegion();
		assertTrue(newRegion==region0);
	}
	
	@Test
	public void cannotMove() {
		Region regionX = new NormalRegion(terrainType, 100);
		Wolf wolfX = new Wolf(regionX);
		
		wolfX.autoMove();
		Region newRegion = wolfX.getRegion();
		assertTrue(newRegion==regionX);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNull() {
		new Wolf(null);
	}
}
