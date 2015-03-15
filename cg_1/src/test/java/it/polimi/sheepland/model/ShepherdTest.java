package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.BlackSheep;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.OvineType;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Ram;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Sheep;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TerrainType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ShepherdTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private TerrainType terrainType1 = Enum.valueOf(TerrainType.class, "RIVER");
	private Region region0 = null;
	private Region region1 = null;
	private Region region2 = null;
	private Street street1 = null;
	private Street street2 = null;
	private Street street3 = null;
	private Shepherd shepherd1 = null;
	private Shepherd shepherd2 = null;
	private Player player1 = null;
	private Player player2 = null;

	@Before
	public void setUp() throws Exception {
		region0 = new NormalRegion(terrainType, 0);
		region1 = new NormalRegion(terrainType1, 1);
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
		street3.addNextStreet(street1);

		shepherd1 = new Shepherd("Pastore1");
		shepherd2 = new Shepherd("Pastore2");
		
		ArrayList<Shepherd> list1 = new ArrayList<Shepherd>();
		list1.add(shepherd1);
		player1 = new Player("Giocatore1", 100, list1,new TerrainCard(0,TerrainType.MOUNTAIN));
		shepherd1.setPlayer(player1);
		ArrayList<Shepherd> list2 = new ArrayList<Shepherd>();
		list2.add(shepherd2);
		player2 = new Player("Giocatore2", 100, list2,new TerrainCard(0,TerrainType.DESERT));
		shepherd2.setPlayer(player2);
	}


	@Test
	public void testGetName() {
		assertTrue(shepherd1.getName()=="Pastore1");
		assertTrue(shepherd2.getName()=="Pastore2");
	}

	@Test
	public void testGetPlayer() {
		assertEquals(shepherd1.getPlayer(),player1);
		assertEquals(shepherd2.getPlayer(),player2);
	}
	
	@Test
	public void testGetListShepherdsToPay1() {
		shepherd1.move(street2);
		shepherd2.move(street1);
		street3.setFenced();
		List<Shepherd> list = shepherd1.getListOfNeighboursToPay();
		assertTrue(list.contains(shepherd2));
		assertFalse(list.contains(shepherd1));
		assertTrue(list.size()==1);
	}
	
	@Test
	public void testGetListShepherdsToPay2() {
		shepherd1.move(street1);
		List<Shepherd> list = shepherd1.getListOfNeighboursToPay();
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testGetListTerrainTypes1() {
		shepherd1.move(street1);
		List<TerrainType> list = shepherd1.getListOfNearTerrainTypes();
		assertTrue(list.contains(terrainType));
		assertTrue(list.contains(terrainType1));
		assertTrue(list.size()==2);
	}

	@Test
	public void testGetListTerrainTypes2() {
		shepherd1.move(street2);
		List<TerrainType> list = shepherd1.getListOfNearTerrainTypes();
		assertTrue(list.contains(terrainType));
		assertFalse(list.contains(terrainType1));
		assertTrue(list.size()==1);
	}
	
	@Test
	public void testGetListTerrainTypes3() {
		shepherd1.move(street3);
		List<TerrainType> list = shepherd1.getListOfNearTerrainTypes();
		assertTrue(list.contains(terrainType));
		assertFalse(list.contains(terrainType1));
		assertTrue(list.size()==1);
	}
	
	@Test
	public void testMoveOvine1() {
		Sheep sheep = new Sheep(region0);
		shepherd1.move(street1);
		shepherd1.moveOvine(sheep);
		assertEquals(sheep.getRegion(),region1);
		assertTrue(region1.getListOfOvines().contains(sheep));
		assertFalse(region0.getListOfOvines().contains(sheep));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveOvine2() {
		shepherd1.move(street1);
		shepherd1.moveOvine(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveOvine3() {
		Sheep sheep = new Sheep(region1);
		shepherd1.move(street3);
		shepherd1.moveOvine(sheep);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveOvine4() {
		Sheep sheep = new Sheep(region1);
		shepherd1.move(street2);
		shepherd1.moveOvine(sheep);
	}
	
	@Test
	public void testKillOvine1() {
		Sheep sheep = new Sheep(region0);
		shepherd1.move(street1);
		shepherd1.killOvine(sheep);
		assertFalse(region1.getListOfOvines().contains(sheep));
		assertFalse(region0.getListOfOvines().contains(sheep));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testKillOvine2() {
		shepherd1.move(street1);
		shepherd1.killOvine(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testKillOvine3() {
		Sheep sheep = new Sheep(region1);
		shepherd1.move(street3);
		shepherd1.killOvine(sheep);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testKillOvine4() {
		Sheep sheep = new Sheep(region1);
		shepherd1.move(street2);
		shepherd1.killOvine(sheep);
	}
	
	@Test
	public void testMove1() {
		shepherd1.move(street1);
		assertEquals(street1,shepherd1.getStreet());
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testMove2() {
		shepherd1.move(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMove3() {
		street1.setFenced();
		shepherd1.move(street1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMove4() {
		shepherd2.move(street1);
		shepherd1.move(street1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMove5() {
		shepherd1.move(street1);
		shepherd1.move(street2);
		shepherd1.move(street1);
	}
	
	@Test
	public void testCanMoveNextStreet() {
		shepherd1.move(street1);
		assertTrue(shepherd1.canMoveNextStreet());
		shepherd1.move(street2);
		assertTrue(shepherd1.canMoveNextStreet());
		shepherd1.move(street3);
		assertFalse(shepherd1.canMoveNextStreet());
	}
	
	@Test
	public void testGetCostToKill() {
		street3.setShepherd(shepherd2);
		shepherd1.move(street2);
		int cost = shepherd1.getCostToKillOvine(shepherd1.getListOfNeighboursToPay());
		assertTrue(cost==shepherd2.getSumToPayForSilence());
	}

	@Test
	public void testGetCostToKilFreel() {
		shepherd1.move(street2);
		int cost = shepherd1.getCostToKillOvine(shepherd1.getListOfNeighboursToPay());
		assertTrue(cost==0);
	}
	
	@Test
	public void testGetCostToKill2() {
		street3.setShepherd(shepherd2);
		street1.setShepherd(shepherd2);
		shepherd1.move(street2);
		int cost = shepherd1.getCostToKillOvine(shepherd1.getListOfNeighboursToPay());
		assertTrue(cost==2*shepherd2.getSumToPayForSilence());
	}
	
	@Test
	public void testCouple1() {
		new Sheep(region0);
		new BlackSheep(region0);
		shepherd1.move(street1);
		assertTrue(region0.getListOfOvines().size()==2);
		shepherd1.couple1(region0);
		assertTrue(region0.getListOfOvines().size()==3);
	}
	
	@Test
	public void testCouple12() {
		new Sheep(region0);
		new Sheep(region0);
		shepherd1.move(street1);
		assertTrue(region0.getListOfOvines().size()==2);
		shepherd1.couple1(region0);
		assertTrue(region0.getListOfOvines().size()==3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCouple1NoNeighbour() {
		new Sheep(region1);
		new BlackSheep(region1);
		shepherd1.move(street3);
		shepherd1.couple1(region1);
		assertTrue(region1.getListOfOvines().size()==2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCouple1NoSheeps() {
		new Sheep(region0);
		new Ram(region0);
		shepherd1.move(street1);
		shepherd1.couple1(region0);
		assertTrue(region0.getListOfOvines().size()==2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCouple1Null() {
		shepherd1.move(street1);
		shepherd1.couple1(null);
	}
	
	@Test
	public void testCouple2() {
		new BlackSheep(region0);
		new Ram(region0);
		shepherd1.move(street1);
		assertTrue(region0.getListOfOvines().size()==2);
		shepherd1.couple2(region0);
		assertTrue(region0.getListOfOvines().size()==3);
		assertTrue(region0.getListOfOvines(OvineType.LAMB).size()==1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCouple2NoNeighbour() {
		new BlackSheep(region1);
		new Ram(region0);
		shepherd1.move(street3);
		shepherd1.couple2(region1);
		assertTrue(region1.getListOfOvines().size()==2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCouple2NoSheeps() {
		shepherd1.move(street1);
		shepherd1.couple2(region0);
		assertTrue(region0.getListOfOvines().size()==2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCouple2Null() {
		shepherd1.move(street1);
		shepherd1.couple2(null);
	}
	
	@Test
	public void testSetPlayerNull() {
		shepherd1.setPlayer(null);
		assertEquals(shepherd1.getPlayer(),player1);
	}
	
}