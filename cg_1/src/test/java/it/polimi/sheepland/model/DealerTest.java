package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.Dealer;
import it.polimi.sheepland.model.Player;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TerrainType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DealerTest {
	private Dealer dealer = null;
	private Player player = null;
	private TerrainType terrainType = null;
	private TerrainCard card1 = null;
	private TerrainCard card2 = null;
	private TerrainCard card3 = null;
	private TerrainCard card4 = null;
	private Map<TerrainType,List<TerrainCard>> cards = null;
	private List<TerrainCard> initCards = null;
	
	@Before
	public void setUp() throws Exception {
		cards = new HashMap<TerrainType,List<TerrainCard>>();
		initCards = new ArrayList<TerrainCard>();
		for(TerrainType t: TerrainType.values()){
			initCards.add(new TerrainCard(0,t));
			List<TerrainCard> listCards = new ArrayList<TerrainCard>();
			for (int i=1; i<5; i++){
				listCards.add(new TerrainCard(i,t));
			}
			cards.put(t, listCards);
		}
		dealer = new Dealer(cards,initCards);
		player = new Player(null, 30, null, new TerrainCard(0,TerrainType.MOUNTAIN));
		
		terrainType = TerrainType.valueOf("WOOD");
		card1 = cards.get(terrainType).get(0);
		card2 = cards.get(terrainType).get(1);
		card3 = cards.get(terrainType).get(2);
		card4 = cards.get(terrainType).get(3);
	}

	@Test
	public void testSell() {
		dealer.sellCard(card1,player);
		assertTrue(player.getMoney()==29); //cost=1
		assertFalse(cards.containsValue(card1));
		dealer.sellCard(card2,player);
		assertFalse(cards.containsValue(card2));
		assertTrue(player.getMoney()==27); //cost=2
		dealer.sellCard(card3,player);
		assertFalse(cards.containsValue(card3));
		assertTrue(player.getMoney()==24); //cost=3
		assertTrue(dealer.getAffordableCard(terrainType).getCost()==4);
		assertEquals(dealer.getAffordableCard(null),null);
		dealer.sellCard(card4,player);
		assertFalse(cards.containsValue(card4));
		assertTrue(player.getMoney()==20); //cost=4
		assertFalse(dealer.isCardAvailable(terrainType));
		assertFalse(dealer.isCardAvailable(null));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellEx() {
		dealer.sellCard(null, player);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellEx1() {
		dealer.sellCard(card1, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellEx2() {
		dealer.sellCardForSale(null, player);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellEx3() {
		dealer.sellCardForSale(card1, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellEx4() {
		dealer.putCardForSale(null, 0);
	}
	
	@Test
	public void testMarket() {
		dealer.sellCard(card1,player); //cost 1
		dealer.sellCard(card2,player); //cost 2
		dealer.sellCard(card3,player); //cost 3
		dealer.putCardForSale(card2,4); //cost 4
		assertTrue(dealer.getCardsForSale().size()==1);
		assertTrue(dealer.getCardsForSale().contains(card2));
		Player player2 = new Player(null,30,null,new TerrainCard(0,TerrainType.DESERT));
		dealer.sellCardForSale(card2, player2);
		assertTrue(dealer.getCardsForSale().size()==0);
		assertTrue(player2.getMoney()==26);
		assertTrue(player.getMoney()==28);
	}
	
	@Test
	public void testGetRandomInitialCard() {
		TerrainCard rndCard = dealer.getRandomInitialCard();
		assertFalse(initCards.contains(rndCard));
		TerrainCard rndCard2 = dealer.getRandomInitialCard();
		assertFalse(rndCard.equals(rndCard2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMarketEx() {
		dealer.sellCardForSale(null, player);
		assertTrue(dealer.getCardsForSale().size()==0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMarketEx1() {
		dealer.sellCardForSale(card1, player);
		assertTrue(dealer.getCardsForSale().size()==0);
	}
	
	@Test
	public void testClearListMarket() {
		dealer.putCardForSale(card1,1);
		dealer.putCardForSale(card2,1);
		dealer.putCardForSale(card3,1);
		assertTrue(dealer.getCardsForSale().size()==3);
		dealer.clearListMarket();
		assertTrue(dealer.getCardsForSale().size()==0);
	}
}
