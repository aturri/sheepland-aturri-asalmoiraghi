/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 18/mag/2014
 *
 */

package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TerrainType;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Andrea
 */
public class DeckTest {
	private Deck deck;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		deck = new Deck();
		deck.setNumPlayers(2);
		deck.createPlayer("player");
		deck.setCurrentPlayer(0);
		deck.setCurrentShepherd(0);
		deck.setShepherdPosition(deck.getStreetById(39)); //between FIELD(7) and MOUNTAIN(1)
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getNumFences()}.
	 */
	@Test
	public void testGetNumFences() {
		assertTrue(deck.getNumFences()==20); //20 initial fences
	}

	/**
	 * Test method for {@link sheepland.model.Deck#setNumFences(int)}.
	 */
	@Test
	public void testSetNumFences() {
		deck.setNumFences(34);
		assertTrue(deck.getNumFences()==34);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#sellTerrainCard(sheepland.model.TerrainCard)}.
	 */
	@Test
	public void testSellTerrainCard() {
		List<TerrainCard> affordableCards = deck.getListOfAffordableCards();
		TerrainCard card = affordableCards.get(0);
		deck.sellTerrainCard(card); //buy MOUNTAIN card
		assertTrue(deck.getCurrentPlayer().getListBoughtCards().contains(card));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellTerrainCard2() {
		deck.sellTerrainCard(null);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#putCardForSale(sheepland.model.TerrainCard)}.
	 */
	@Test
	public void testPutCardForSale() {
		TerrainCard card = deck.getCurrentPlayer().getListBoughtCards().get(0); //the initial card of the player
		deck.putCardForSale(card,5);
		assertTrue(deck.getCardsForSale().contains(card));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPutCardForSaleNull() {
		deck.putCardForSale(null,5);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#sellCardForSale(sheepland.model.TerrainCard)}.
	 */
	@Test
	public void testSellCardForSale() {
		deck.createPlayer("player2");
		TerrainCard card = deck.getCurrentPlayer().getListBoughtCards().get(0); //the initial card of the player1
		deck.putCardForSale(card, 5); //cost 5;
		deck.setCurrentPlayer(1); //player2
		assertEquals(deck.getCardsForSale().get(0),card);
		deck.sellCardForSale(card);
		assertTrue(deck.getCurrentPlayer().getListBoughtCards().contains(card));
		assertEquals(card.getOwner(),deck.getCurrentPlayer());
		assertTrue(deck.getCurrentPlayer().getMoney()==25);
		deck.setCurrentPlayer(0);
		assertTrue(deck.getCurrentPlayer().getMoney()==35);
		assertFalse(deck.getCurrentPlayer().getListBoughtCards().contains(card));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellCardForSaleNull() {
		deck.sellCardForSale(null);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getCardsForSale()}.
	 */
	@Test
	public void testGetCardsForSale() {
		TerrainCard card = deck.getCurrentPlayer().getListBoughtCards().get(0); //the initial card of the player1
		deck.putCardForSale(card, 5);
		assertEquals(card,deck.getCardsForSale().get(0));
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getCurrentPlayer()}.
	 */
	@Test
	public void testGetCurrentPlayer() {
		assertTrue(deck.getCurrentPlayer().getName()=="player");
	}

	/**
	 * Test method for {@link sheepland.model.Deck#setCurrentPlayer(int)}.
	 */
	@Test
	public void testSetCurrentPlayer() {
		Player player1 = deck.getCurrentPlayer();
		deck.createPlayer("player2");
		deck.setCurrentPlayer(1);
		assertFalse(deck.getCurrentPlayer().equals(player1));
		deck.setCurrentPlayer(0);
		assertEquals(deck.getCurrentPlayer(),player1);
		deck.setCurrentPlayer(10);
		assertEquals(deck.getCurrentPlayer(),player1);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#createPlayer(java.lang.String)}.
	 */
	@Test
	public void testCreatePlayer() {
		deck.createPlayer("player2");
		Player player1 = deck.getCurrentPlayer();
		deck.setCurrentPlayer(1);
		assertFalse(deck.getCurrentPlayer().equals(player1));
		assertTrue(deck.getCurrentPlayer().getListBoughtCards().size()==1);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getCurrentShepherd()}.
	 */
	@Test
	public void testGetCurrentShepherd() {
		deck.setCurrentShepherd(1);
		assertEquals(deck.getCurrentShepherd(),deck.getCurrentPlayer().getShepherd(1));
	}

	/**
	 * Test method for {@link sheepland.model.Deck#setCurrentShepherd(int)}.
	 */
	@Test
	public void testSetCurrentShepherdInt() {
		deck.setCurrentShepherd(1);
		assertEquals(deck.getCurrentShepherd(),deck.getCurrentPlayer().getShepherd(1));
	}

	/**
	 * Test method for {@link sheepland.model.Deck#setCurrentShepherd(sheepland.model.Shepherd)}.
	 */
	@Test
	public void testSetCurrentShepherdShepherd() {
		deck.setCurrentShepherd(1);
		assertEquals(deck.getCurrentShepherd(),deck.getCurrentPlayer().getShepherd(1));
		deck.setCurrentShepherd(deck.getCurrentPlayer().getShepherd(0));
		assertEquals(deck.getCurrentShepherd(),deck.getCurrentPlayer().getShepherd(0));
	}

	/**
	 * Test method for {@link sheepland.model.Deck#unsetCurrentShepherd()}.
	 */
	@Test
	public void testUnsetCurrentShepherd() {
		assertFalse(deck.getCurrentShepherd()==null);
		deck.unsetCurrentShepherd();
		assertTrue(deck.getCurrentShepherd()==null);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#setShepherdPosition(sheepland.Street)}.
	 */
	@Test
	public void testSetShepherdPosition() {
		assertEquals(deck.getCurrentShepherd().getStreet(),deck.getStreetById(39));
		deck.setShepherdPosition(deck.getStreetById(23));
		assertEquals(deck.getCurrentShepherd().getStreet(),deck.getStreetById(23));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetShepherdPositionNull() {
		deck.setShepherdPosition(null);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getShepherdById(java.lang.Integer)}.
	 */
	@Test
	public void testGetShepherdById() {
		Shepherd shepherd1 = deck.getCurrentShepherd();
		deck.setCurrentShepherd(1);
		assertFalse(shepherd1.equals(deck.getCurrentShepherd()));
		assertTrue(deck.getShepherdById(0).equals(shepherd1));
		assertTrue(deck.getShepherdById(1).equals(deck.getCurrentShepherd()));
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getStreetById(java.lang.Integer)}.
	 */
	@Test
	public void testGetStreetById() {
		assertTrue(deck.getStreetById(39).getId()==39);
		assertEquals(deck.getStreetById(39),deck.getCurrentShepherd().getStreet());
	}

	/**
	 * Test method for {@link sheepland.model.Deck#checkOvinesNearShepherd()}.
	 */
	@Test
	public void testCheckOvinesNearShepherd() {
		assertTrue(deck.checkOvinesNearShepherd());
	}

	/**
	 * Test method for {@link sheepland.model.Deck#isCardAvailable()}.
	 */
	@Test
	public void testIsCardAvailable() {
		assertTrue(deck.isCardAvailable());
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		deck.sellTerrainCard(deck.getListOfAffordableCards().get(0));
		assertFalse(deck.isCardAvailable());
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getListOfAffordableCards()}.
	 */
	@Test
	public void testGetListOfAffordableCards() {
		List<TerrainCard> affordableCards = deck.getListOfAffordableCards();
		assertTrue(affordableCards.size()==2);
		assertEquals(affordableCards.get(0).getTerrainType(),TerrainType.MOUNTAIN);
		assertEquals(affordableCards.get(1).getTerrainType(),TerrainType.FIELD);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getCardById(int)}.
	 */
	@Test
	public void testGetCardById() {
		TerrainCard card = deck.getCurrentPlayer().getListBoughtCards().get(0); //the initial card of the player
		deck.putCardForSale(card, 5);
		deck.getCardsForSale();
		assertEquals(card,deck.getCardById(0));
	}

	/**
	 * Test method for {@link sheepland.model.Deck#getListOfNeighbourOvines()}.
	 */
	@Test
	public void testGetListOfNeighbourOvines() {
		List<Ovine> listOvines = deck.getListOfNeighbourOvines();
		assertTrue(listOvines.size()==2);
	}

	/**
	 * Test method for {@link sheepland.model.Deck#killOvine(sheepland.model.Ovine)}.
	 */
	@Test
	public void testKillOvine() {
		List<Ovine> listOvines = deck.getListOfNeighbourOvines();
		assertTrue(listOvines.size()==2);
		deck.killOvine(listOvines.get(0));
		listOvines = deck.getListOfNeighbourOvines();
		assertTrue(listOvines.size()==1);
		deck.killOvine(listOvines.get(0));
		listOvines = deck.getListOfNeighbourOvines();
		assertTrue(listOvines.size()==0);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testKillOvineNull() {
		deck.killOvine(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveOvineNull() {
		deck.moveOvine(null);
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#setNumPlayers(java.lang.Integer)}.
	 */
	@Test
	public void testSetNumPlayers() {
		assertFalse(deck.getCurrentShepherd()==deck.getCurrentPlayer().getShepherd(1));
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#moveWolf()}.
	 */
	@Test
	public void testMoveWolf() {
		assertEquals(deck.getWolfString(),"Lupo@Regione "+Integer.toString(deck.getRegions().get(0).getId())+" ("+deck.getRegions().get(0).getName()+")");
		deck.moveWolf();
		assertFalse(deck.getWolfString()=="Lupo@Regione "+Integer.toString(deck.getRegions().get(0).getId())+" ("+deck.getRegions().get(0).getName()+")");
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#moveBlackSheep()}.
	 */
	@Test
	public void testMoveBlackSheep() {
		assertEquals(deck.getBlackSheepString(),"Pecora nera@Regione "+Integer.toString(deck.getRegions().get(0).getId())+" ("+deck.getRegions().get(0).getName()+")");
		deck.moveBlackSheep();
		assertFalse(deck.getBlackSheepString()=="Pecora nera@Regione "+Integer.toString(deck.getRegions().get(0).getId())+" ("+deck.getRegions().get(0).getName()+")");
	}

	/**
	 * Test method for {@link sheepland.model.Deck#couple2(sheepland.model.Region)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouple1OneSheep() {
		deck.couple1(deck.getRegions().get(15));
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#couple2(sheepland.model.Region)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouple1Null() {
		deck.couple1(null);
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#couple2(sheepland.model.Region)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouple2Null() {
		deck.couple2(null);
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#check2Sheeps()}.
	 */
	@Test
	public void testCheck2Sheeps() {
		assertFalse(deck.check2Sheeps());
	}

	/**
	 * Test method for {@link sheepland.model.Deck#checkSheepAndRam()}.
	 */
	@Test
	public void testCheckSheepAndRam() {
		assertFalse(deck.checkSheepAndRam());
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#getNeighbourRegions()}.
	 */
	@Test
	public void testGetNeighbourRegions() {
		List<Region> nearRegion = deck.getNeighbourRegions();
		assertTrue(nearRegion.size()==2);
		assertTrue(nearRegion.contains(deck.getRegions().get(1)));
		assertTrue(nearRegion.contains(deck.getRegions().get(7)));
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#getCurrentPlayerCards()}.
	 */
	@Test
	public void testGetCurrentPlayerCards() {
		assertTrue(deck.getCurrentPlayerCards().size()==1);
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#getBlackSheepString()}.
	 */
	@Test
	public void testGetBlackSheepString() {
		assertEquals(deck.getBlackSheepString(),"Pecora nera@Regione "+Integer.toString(deck.getRegions().get(0).getId())+" ("+deck.getRegions().get(0).getName()+")");
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#getWolfString()}.
	 */
	@Test
	public void testGetWolfString() {
		assertEquals(deck.getWolfString(),"Lupo@Regione "+Integer.toString(deck.getRegions().get(0).getId())+" ("+deck.getRegions().get(0).getName()+")");
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#getRegions()}.
	 */
	@Test
	public void testGetRegions() {
		assertEquals(deck.getRegions().size(),19);
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#canMoveShepherd()}.
	 */
	@Test
	public void testCanMoveShepherd() {
		//case 1: free streets
		assertTrue(deck.canMoveShepherd());
		//case 2: streets not empty, but there is money
		deck.setCurrentPlayer(0);
		deck.setCurrentShepherd(0);
		deck.setShepherdPosition(deck.getStreetById(13));
		deck.setCurrentPlayer(1);
		deck.setCurrentShepherd(0);
		deck.setShepherdPosition(deck.getStreetById(15));
		deck.setCurrentPlayer(0);
		deck.setCurrentShepherd(1);
		deck.setShepherdPosition(deck.getStreetById(14));
		assertTrue(deck.canMoveShepherd());
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#getPlayers()}.
	 */
	@Test
	public void testGetPlayers() {
		assertEquals(deck.getPlayers().size(),1);
		deck.createPlayer("player2");
		deck.createPlayer("player3");
		assertEquals(deck.getPlayers().size(),3);
		assertTrue(deck.getPlayers().contains(deck.getCurrentPlayer()));
	}
	
	/**
	 * Test method for {@link sheepland.model.Deck#growLambs()}.
	 */
	@Test
	public void testGrowLambs() {
		List<Region> listRegions = deck.getRegions();
		Region testRegion = null;
		for(Region r: listRegions){
			if(r.containsLamb()){
				testRegion = r;
				break;
			}
		}
		if(testRegion == null){
			assertTrue(true);
		}else{
			assertTrue(testRegion.containsLamb());
			deck.growLambs();
			assertTrue(testRegion.containsLamb());
			deck.growLambs();
			assertFalse(testRegion.containsLamb());
			assertTrue(testRegion.containsRam()||testRegion.containsSheep());
		}
	}
	
	@Test
	public void testGetCurrentPlayerShepherds() {
		assertTrue(deck.getCurrentPlayerShepherds().size()==2);
	}
	
//	@Test
//	public void testDisblePlayer() {
//		Player player = deck.getCurrentPlayer();
//		assertTrue(deck.getEnablePlayers().size()==2);
//		assertTrue(deck.isPlayerEnabled(player));
//		assertTrue(deck.arePlayers());
//		deck.disablePlayer(player);
//		assertFalse(deck.isPlayerEnabled(player));
//		assertTrue(deck.getEnablePlayers().size()==2);
//		assertTrue(deck.arePlayers());
//		deck.setCurrentPlayer(1);
//		Player player1 = deck.getCurrentPlayer();
//		deck.disablePlayer(player1);
//		assertFalse(deck.arePlayers());
//	}

	@Test
	public void testWolf() {
		Wolf wolf = deck.getWolf();
		assertTrue(wolf!=null);
	}
	
	@Test
	public void testBlackSheep() {
		BlackSheep bSheep = deck.getBlackSheep();
		assertTrue(bSheep!=null);
	}
}
