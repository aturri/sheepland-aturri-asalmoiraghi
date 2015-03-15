package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.NormalRegion;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Sheep;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TerrainType;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	private TerrainType terrainType = Enum.valueOf(TerrainType.class, "WOOD");
	private TerrainType terrainType1 = Enum.valueOf(TerrainType.class, "RIVER");
	private Region region0 = null;
	private Region region1 = null;
	private Region region2 = null;
	private Street street1 = null;
	private Street street2 = null;
	private Street street3 = null;
	private Player player1 = null;
	private Player player2 = null;
	private Player player3 = null;
	private Shepherd shepherd1 = null;
	private Shepherd shepherd2 = null;
	private Shepherd shepherd3 = null;
	private Shepherd shepherd4 = null;
	
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
		street3.addNextStreet(street2);
		
		ArrayList<Shepherd> list1 = new ArrayList<Shepherd>();
		shepherd1 = new Shepherd("Pastore 1");
		list1.add(shepherd1);
		player1 = new Player("Giocatore 1",30,list1,new TerrainCard(0,TerrainType.MOUNTAIN));
		ArrayList<Shepherd> list2 = new ArrayList<Shepherd>();
		shepherd2 = new Shepherd("Pastore 2");
		shepherd3 = new Shepherd("Pastore 3");
		list2.add(shepherd2);
		list2.add(shepherd3);
		player2 = new Player("Giocatore 2",30,list2,new TerrainCard(0,TerrainType.DESERT));
		ArrayList<Shepherd> list3 = new ArrayList<Shepherd>();
		shepherd4 = new Shepherd("Pastore 4");
		list3.add(shepherd4);
		player3 = new Player("Giocatore 3",0,list3,new TerrainCard(0,TerrainType.RIVER));
		
		shepherd1.setPlayer(player1);
		shepherd2.setPlayer(player2);
		shepherd3.setPlayer(player2);
		shepherd4.setPlayer(player3);
	}

	@Test
	public void test() {
		assertTrue(player1.getName()=="Giocatore 1");
		assertEquals(player1.getShepherd(0),shepherd1);
		assertTrue(player2.getName()=="Giocatore 2");
		assertEquals(player2.getShepherd(0),shepherd2);
		assertEquals(player2.getShepherd(1),shepherd3);
		assertTrue(player2.getCurrentShepherd()==null);
		player2.setCurrentShepherd(1);
		assertEquals(player2.getCurrentShepherd(),shepherd3);
		player2.setCurrentShepherd(shepherd2);
		assertEquals(player2.getCurrentShepherd(),shepherd2);
		player2.setCurrentShepherd(null);	
		assertTrue(player2.getCurrentShepherd()==null);
		assertTrue(player1.getMoney()==30);
	}
	
	@Test
	public void testPay() {
		assertTrue(player1.payToPlayer(30,player2));
		assertTrue(player1.getMoney()==0);
		assertTrue(player2.getMoney()==60);
		assertFalse(player1.payToPlayer(30,player2));
		assertTrue(player2.getMoney()==60);
		assertTrue(player1.payToPlayer(-30,player2));
		assertTrue(player1.getMoney()==30);
		assertTrue(player2.getMoney()==30);
		assertFalse(player1.payToPlayer(0,null));
	}
	
	@Test
	public void testCard1() {
		TerrainCard terrainCard = new TerrainCard(0,TerrainType.valueOf("WOOD"));
		assertFalse(player1.getListBoughtCards().contains(terrainCard));
		player1.buyTerrain(terrainCard);
		assertTrue(player1.getListBoughtCards().contains(terrainCard));
		player1.soldCard(terrainCard);
		assertFalse(player1.getListBoughtCards().contains(terrainCard));
	}
	@Test
	public void testCard2() {
		TerrainCard terrainCard = null;
		player1.soldCard(terrainCard);
		assertFalse(player1.getListBoughtCards().contains(terrainCard));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCardNoMoney() {
		TerrainCard terrainCard = new TerrainCard(40,TerrainType.valueOf("WOOD"));
		assertFalse(player1.getListBoughtCards().contains(terrainCard));
		player1.buyTerrain(terrainCard);
		assertFalse(player1.getListBoughtCards().contains(terrainCard));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCardNull() {
		player1.buyTerrain(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveShepherd() {
		player1.moveShepherd(null);
		assertTrue(player1.getMoney()==30);
	}
	
	@Test
	public void testMoveShepherd1() {
		player1.setCurrentShepherd(shepherd1);
		player1.moveShepherd(street1);
		assertTrue(player1.getMoney()==30);
		player1.moveShepherd(street3);
		assertTrue(player1.getMoney()==29);
		player1.moveShepherd(street2);
		assertTrue(player1.getMoney()==29);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveShepherd2() {
		player1.setCurrentShepherd(shepherd1);
		player1.moveShepherd(street1);
		player1.moveShepherd(street1);
		assertTrue(player1.getMoney()==30);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveShepherd3() {
		player1.setCurrentShepherd(shepherd1);
		street1.setFenced();
		player1.moveShepherd(street1);
		assertTrue(player1.getMoney()==30);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveShepherd4() {
		player3.setCurrentShepherd(shepherd4);
		player3.moveShepherd(street1);
		assertTrue(player3.getMoney()==0);
		player3.moveShepherd(street3);
		assertTrue(player3.getMoney()==0);
		assertEquals(shepherd4.getStreet(),street1);
	}
	
	@Test
	public void testKillOvine() {
		player1.setCurrentShepherd(shepherd1);
		player1.moveShepherd(street1);
		Sheep sheep = new Sheep(region0);
		player1.killOvine(sheep);
		assertTrue(player1.getMoney()==30);
		assertTrue(region0.getListOfOvines().isEmpty());
	}
	
	@Test
	public void testKillOvine1() {
		Sheep sheep = new Sheep(region0);
		player1.setCurrentShepherd(shepherd1);
		player2.setCurrentShepherd(shepherd2);
		player3.setCurrentShepherd(shepherd4);
		player1.moveShepherd(street1);		
		player2.moveShepherd(street2);
		player3.moveShepherd(street3);
		player2.killOvine(sheep);
		assertTrue(region0.getListOfOvines().isEmpty());
	}
	
	@Test
	public void testGetShepherds() {
		assertTrue(player1.getShepherds().contains(shepherd1));
		assertFalse(player1.getShepherds().contains(shepherd2));
		assertFalse(player1.getShepherds().contains(shepherd3));
		assertTrue(player1.getShepherds().size()==1);
		assertFalse(player2.getShepherds().contains(shepherd1));
		assertTrue(player2.getShepherds().contains(shepherd2));
		assertTrue(player2.getShepherds().contains(shepherd3));
		assertTrue(player2.getShepherds().size()==2);
	}
	
	@Test
	public void test1ShepherdSet() {
		player1.setCurrentShepherd(null);
		assertEquals(player1.getCurrentShepherd(),shepherd1);
	}
	
	@Test
	public void testSetGetScore() {
		player1.setScore(Integer.getInteger("100"));
		assertEquals(player1.getScore(),Integer.getInteger("100"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetShepherdOutOfIndex() {
		player1.getShepherd(3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetShepherdOutOfIndexNegative() {
		player1.getShepherd(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetShepherdOutOfIndex() {
		player1.setCurrentShepherd(3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetShepherdOutOfIndexNegative() {
		player1.setCurrentShepherd(-1);
	}
}
