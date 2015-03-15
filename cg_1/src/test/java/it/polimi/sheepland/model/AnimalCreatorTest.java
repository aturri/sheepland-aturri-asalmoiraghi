package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.AnimalCreator;
import it.polimi.sheepland.model.BlackSheep;
import it.polimi.sheepland.model.Lamb;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Ram;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Sheep;
import it.polimi.sheepland.model.TerrainType;

import org.junit.Before;
import org.junit.Test;

public class AnimalCreatorTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private Region region = null;
	private AnimalCreator animalCreator = null;
	
	@Before
	public void setUp() throws Exception {
		region = new NormalRegion(terrainType, 0);
		animalCreator = new AnimalCreator();
	}

	@Test
	public void testSRL() {
		Ovine ovine = null;
		try {
			ovine = animalCreator.factoryOvine(region);
		} catch (Exception e) {
		}
		assertTrue((ovine instanceof Sheep) || (ovine instanceof Lamb) || (ovine instanceof Ram));
		assertFalse(ovine instanceof BlackSheep);
		assertEquals(region,ovine.getRegion());
	}

	@Test
	public void testSR() throws NoSuchMethodException, SecurityException {
		Ovine ovine = null;
		try {
			ovine = animalCreator.factoryOvineNoLamb(region);
		} catch (Exception e) {
		}
		assertTrue((ovine instanceof Sheep) || (ovine instanceof Ram));
		assertFalse(ovine instanceof BlackSheep);
		assertFalse(ovine instanceof Lamb);
		assertEquals(region,ovine.getRegion());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFactoryWolf() {
		animalCreator.factoryWolf(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFactoryBlackSheep() {
		animalCreator.factoryBlackSheep(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFactoryOvine() {
		animalCreator.factoryOvine(null);
	}
}
