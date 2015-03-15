package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TerrainType;

import org.junit.Before;
import org.junit.Test;

public class TerrainCardTest {
	private TerrainType terrainType = null;
	private Player player = null;
	private TerrainCard terrainCard = null;

	@Before
	public void setUp() throws Exception {
		terrainType = TerrainType.valueOf("WOOD");
		terrainCard = new TerrainCard(4, terrainType);
	}

	@Test
	public void test() {
		assertTrue(terrainCard.getCost()==4);
		assertEquals(terrainType,terrainCard.getTerrainType());
		assertTrue(terrainCard.getOwner()==null);
		terrainCard.setOwner(player);
		assertEquals(player,terrainCard.getOwner());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNull() {
		new TerrainCard(0,null);
	}
}
