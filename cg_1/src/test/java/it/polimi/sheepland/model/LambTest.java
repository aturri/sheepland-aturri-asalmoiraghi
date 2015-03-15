package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.Lamb;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.OvineType;
import it.polimi.sheepland.model.Ram;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Sheep;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainType;

import org.junit.Before;
import org.junit.Test;

public class LambTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private Lamb lamb = null;
	private Region region = null;
	private Region region1 = null;
	private Street street1 = null;

	
	@Before
	public void setUp() throws Exception {
		region = new NormalRegion(terrainType, 0);
		region1 = new NormalRegion(terrainType, 1);
		
		street1= new Street(1, 1);
		
		street1.setRegion(region);
		street1.setRegion(region1);

		region.addStreet(street1);
		region1.addStreet(street1);
		
		lamb = new Lamb(region);
	}

	@Test
	public void kill() {
		lamb.kill();
		assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb));
		assertFalse(region.getListOfOvines().contains(lamb));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createWithNullRegion() {
		new Lamb(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void move() {
		lamb.move(street1);
		assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb));
		assertFalse(region.getListOfOvines().contains(lamb));
		assertTrue(region1.getListOfOvines(OvineType.LAMB).contains(lamb));
		assertTrue(region1.getListOfOvines().contains(lamb));
		lamb.move(null);
		assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb));
		assertFalse(region.getListOfOvines().contains(lamb));
		assertTrue(region1.getListOfOvines(OvineType.LAMB).contains(lamb));
		assertTrue(region1.getListOfOvines().contains(lamb));
	}
	
	@Test
	public void grow1dayAsLamb() {
		try {
			lamb.grow();
		} catch (Exception e) {
		}
		assertTrue(region.getListOfOvines(OvineType.LAMB).contains(lamb));
	}
	
	@Test
	public void grow1dayAsOvine() {
		try {
			lamb.grow();
		} catch (Exception e) {
		}
		assertTrue(region.getListOfOvines().contains(lamb));
	}

	@Test
	public void grow2day() {
		try {
			lamb.grow();
			lamb.grow();
		} catch (Exception e) {
		}
		assertFalse(region.getListOfOvines(OvineType.LAMB).contains(lamb));
	}

	@Test
	public void grow2dayVerifyNewOvine() {
		try {
			lamb.grow();
			lamb.grow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(region.getListOfOvines().size()==1);
	}
	
	@Test
	public void grow2dayVerifyNewOvineType() {
		try {
			lamb.grow();
			lamb.grow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue((region.getListOfOvines().get(0) instanceof Sheep) || (region.getListOfOvines().get(0) instanceof Ram));
	}
	
	@Test
	public void testGetValue() {
		assertTrue(lamb.getValue()==1);
	}
}
