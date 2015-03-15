package it.polimi.sheepland.view;

import static org.junit.Assert.*;
import it.polimi.sheepland.controller.Controller;
import it.polimi.sheepland.model.Deck;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ControllerTesterViewTest {
	private Deck deck;
	private Controller controller;
	private ControllerTesterView view;
	private List<Integer> listInputs = new ArrayList<Integer>();
	
	@Before
	public void setUp() throws Exception {
		deck = new Deck();
	}

	/**
	 * Test #1:
	 * <ul>
	 * <li>player 1 30 points</li>
	 * <li>player 2 27 points</li>
	 * <li>blacksheep counter 8</li>
	 * <li>wolf counter 4</li>
	 * <li>changed player 8 times</li>
	 * <li>0 errors</li>
	 * </ul>
	 */
	@Test
	public void test() {
		//num players
		listInputs.add(2);
		//initial positioning
		listInputs.add(0);
		listInputs.add(1);
		listInputs.add(2);
		listInputs.add(3);
		//first player #1
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(4);
		listInputs.add(0);
		listInputs.add(5);
		listInputs.add(0);
		listInputs.add(6);
		//second player #2
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(7);
		listInputs.add(0);
		listInputs.add(8);
		listInputs.add(0);
		listInputs.add(9);
		//market
		listInputs.add(0);
		listInputs.add(0);
		//first player #3
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(11);
		listInputs.add(0);
		listInputs.add(12);
		listInputs.add(0);
		listInputs.add(13);
		//second player #4
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(14);
		listInputs.add(0);
		listInputs.add(15);
		listInputs.add(0);
		listInputs.add(16);
		//market
		listInputs.add(0);
		listInputs.add(0);
		//first player #5
		listInputs.add(0);
		listInputs.add(2);//buy card
		listInputs.add(1);
		listInputs.add(0);
		listInputs.add(17);
		listInputs.add(0);
		listInputs.add(10);
		//second player #6
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(41);
		listInputs.add(0);
		listInputs.add(27);
		listInputs.add(0);
		listInputs.add(20);
		//market
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		//first player #7
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(31);
		listInputs.add(0);
		listInputs.add(33);
		listInputs.add(0);
		listInputs.add(34);
		//second player #8
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(23);
		listInputs.add(0);
		listInputs.add(24);
		listInputs.add(0);
		listInputs.add(25);
		//market
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		
		view = new ControllerTesterView(deck,listInputs);
		controller = new Controller(view, deck);
		controller.startController();
		
		assertTrue(view.getCountErrors()==0);
		assertTrue(view.getMoveBlackSheep()==8);
		assertTrue(view.getMoveWolf()==4);
		//there can be blacksheep
		assertTrue(view.getSingleScores("Giocatore 1")>=30);
		assertTrue(view.getSingleScores("Giocatore 2")>=27);
	}
	
	/**
	 * Test #2:
	 * <ul>
	 * <li>player 1 >=16 points</li>
	 * <li>player 2 >=18 points</li>
	 * <li>player 3 >=18 points</li>
	 * <li>wolf counter 3</li>
	 * <li>4 errors</li>
	 * </ul>
	 */
	@Test
	public void test2() {
		//num players
		listInputs.add(3);
		//initial positioning
		listInputs.add(8);
		listInputs.add(8);
		listInputs.add(7);
		listInputs.add(6);
		//first player #1
		listInputs.add(3);
		listInputs.add(0);
		listInputs.add(2);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(10);
		//second player #2
		listInputs.add(0);
		listInputs.add(11);
		listInputs.add(0);
		listInputs.add(12);
		listInputs.add(0);
		listInputs.add(12);
		listInputs.add(0);
		listInputs.add(13);
		//third player #3
		listInputs.add(1);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(1);
		listInputs.add(1);
		listInputs.add(1);
		//market
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		//first player #4
		listInputs.add(0);
		listInputs.add(41);
		listInputs.add(0);
		listInputs.add(41);
		listInputs.add(0);
		listInputs.add(12);
		listInputs.add(0);
		listInputs.add(40);
		listInputs.add(0);
		listInputs.add(39);
		//second player #5
		listInputs.add(0);
		listInputs.add(38);
		listInputs.add(0);
		listInputs.add(37);
		listInputs.add(0);
		listInputs.add(36);
		//third player #6
		listInputs.add(0);
		listInputs.add(35);
		listInputs.add(0);
		listInputs.add(34);
		listInputs.add(0);
		listInputs.add(33);
		//market
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		//first player #7
		listInputs.add(0);
		listInputs.add(30);
		listInputs.add(0);
		listInputs.add(29);
		listInputs.add(0);
		listInputs.add(28);
		//second player #8
		listInputs.add(0);
		listInputs.add(27);
		listInputs.add(0);
		listInputs.add(26);
		listInputs.add(0);
		listInputs.add(25);
		//second player #9
		listInputs.add(0);
		listInputs.add(24);
		listInputs.add(0);
		listInputs.add(23);
		listInputs.add(0);
		listInputs.add(22);
		//market
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		
		view = new ControllerTesterView(deck,listInputs);
		controller = new Controller(view, deck);
		controller.startController();
		
		assertTrue(view.getCountErrors()==4);
		assertTrue(view.getMoveWolf()==3);
		//there can be blacksheep
		assertTrue(view.getSingleScores("Giocatore 1")>=15);
		assertTrue(view.getSingleScores("Giocatore 2")>=17);
		assertTrue(view.getSingleScores("Giocatore 3")>=17);
	}
	
	/**
	 * Test #3:
	 * <ul>
	 * <li>player 1 >=31 points</li>
	 * <li>player 2 >=12 points</li>
	 * <li>player 3 >=8 points</li>
	 * <li>player 4 >=12 points</li>
	 * <li>wolf counter 2</li>
	 * <li>0 errors</li>
	 * </ul>
	 */
	@Test
	public void test3() {
		//num players
		listInputs.add(4);
		//initial positioning
		listInputs.add(0);
		listInputs.add(1);
		listInputs.add(2);
		listInputs.add(3);
		//first player #1
		listInputs.add(2);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(4);
		listInputs.add(0);
		listInputs.add(5);
		//second player #2
		listInputs.add(0);
		listInputs.add(6);
		listInputs.add(2);
		listInputs.add(1);
		listInputs.add(0);
		listInputs.add(7);
		//third player #3
		listInputs.add(0);
		listInputs.add(8);
		listInputs.add(0);
		listInputs.add(9);
		listInputs.add(0);
		listInputs.add(10);
		//fourth player #4
		listInputs.add(0);
		listInputs.add(11);
		listInputs.add(0);
		listInputs.add(12);
		listInputs.add(0);
		listInputs.add(13);
		//market
		listInputs.add(1);
		listInputs.add(10);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		//buy
		listInputs.add(0);
		//first player #5
		listInputs.add(0);
		listInputs.add(14);
		listInputs.add(0);
		listInputs.add(15);
		listInputs.add(0);
		listInputs.add(16);
		//second player #6
		listInputs.add(0);
		listInputs.add(17);
		listInputs.add(0);
		listInputs.add(18);
		listInputs.add(0);
		listInputs.add(19);
		//third player #7
		listInputs.add(0);
		listInputs.add(20);
		listInputs.add(0);
		listInputs.add(21);
		listInputs.add(0);
		listInputs.add(22);
		//third player #8
		listInputs.add(0);
		listInputs.add(23);
		listInputs.add(0);
		listInputs.add(24);
		listInputs.add(0);
		listInputs.add(25);
		//market
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		listInputs.add(0);
		
		view = new ControllerTesterView(deck,listInputs);
		controller = new Controller(view, deck);
		controller.startController();		
		
		assertTrue(view.getCountErrors()==0);
		assertTrue(view.getMoveWolf()==2);
		//there can be blacksheep
		assertTrue(view.getSingleScores("Giocatore 1")>=21);
		assertTrue(view.getSingleScores("Giocatore 2")>=12);
		assertTrue(view.getSingleScores("Giocatore 3")>=8);
		assertTrue(view.getSingleScores("Giocatore 4")>=12);
	}
}
