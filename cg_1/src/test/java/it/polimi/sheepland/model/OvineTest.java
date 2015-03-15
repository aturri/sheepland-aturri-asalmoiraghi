package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.Lamb;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Ram;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Sheep;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainType;

import org.junit.Before;
import org.junit.Test;

public class OvineTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private Ovine ovine = null;
	private Region region0 = null;
	private Region region1 = null;
	private Street street1 = null;

	@Before
	public void setUp() throws Exception {
		region0 = new NormalRegion(terrainType, 0);
		region1 = new NormalRegion(terrainType, 1);
		
		street1= new Street(1, 1);
		
		street1.setRegion(region0);
		street1.setRegion(region1);

		region0.addStreet(street1);
		region1.addStreet(street1);
		
		ovine = new Ovine(region0);
	}
	
	@Test
	public void testToString() {
		Sheep sheep = new Sheep(region0);
		Ram ram = new Ram(region0);
		Lamb lamb = new Lamb(region0);
		assertEquals(sheep.toString(),"Pecora@Regione "+Integer.toString(region0.getId())+" ("+region0.getName()+")");
		assertEquals(ram.toString(),"Montone@Regione "+Integer.toString(region0.getId())+" ("+region0.getName()+")");
		assertEquals(lamb.toString(),"Agnello@Regione "+Integer.toString(region0.getId())+" ("+region0.getName()+")");
	}

	@Test
	public void testKill() {
		assertTrue(region0.getListOfOvines().contains(ovine));
		ovine.kill();
		assertFalse(region0.getListOfOvines().contains(ovine));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void moveThroughNullStreetAndIsInList() {
		ovine.move(null);
		assertTrue(region0.getListOfOvines().contains(ovine));
	}

	@Test(expected = IllegalArgumentException.class)
	public void moveThroughNullStreetAndChangesRegion() {
		ovine.move(null);
		assertTrue(region0==ovine.getRegion());
	}
	
	@Test
	public void moveAndIsInList() {
		ovine.move(street1);
		assertTrue(region1.getListOfOvines().contains(ovine));
	}

	@Test
	public void moveAndChangesRegion() {
		ovine.move(street1);
		assertTrue(region1==ovine.getRegion());
	}
	
	@Test
	public void testGetValue() {
		Ovine ovine = new Ovine(region1);
		Sheep sheep = new Sheep(region1);
		Ram ram = new Ram(region1);
		assertTrue(ovine.getValue()==0);
		assertTrue(sheep.getValue()==1);
		assertTrue(ram.getValue()==1);
	}
	
	@Test
	public void testGetId() {
		@SuppressWarnings("unused")
		Ovine ovine = new Ovine(region1);
		Sheep sheep = new Sheep(region1);
		assertTrue(sheep.getId()>=2);
	}
}
