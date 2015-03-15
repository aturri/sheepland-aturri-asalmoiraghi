package it.polimi.sheepland.view;

import it.polimi.sheepland.controller.Controller;
import it.polimi.sheepland.model.Deck;

import org.junit.Before;
import org.junit.Test;

public class RandomViewTest {
	private Controller controller;
	
	@Before
	public void setUp() throws Exception {
		Deck deck = new Deck();
		RandomView view = new RandomView(deck);
		controller = new Controller(view, deck);
	}

	@Test
	public void test() {
		controller.startController();
	}


}
