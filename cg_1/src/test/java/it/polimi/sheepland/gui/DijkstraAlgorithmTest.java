package it.polimi.sheepland.gui;

import static org.junit.Assert.*;
import it.polimi.sheepland.gui.DijkstraAlgorithm;
import it.polimi.sheepland.gui.JStreet;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DijkstraAlgorithmTest {
	JStreet street0 = new JStreet(0, 0);
	JStreet street1 = new JStreet(0, 0);
	JStreet street2 = new JStreet(0, 0);
	JStreet street3 = new JStreet(0, 0);
	JStreet street4 = new JStreet(0, 0);
	JStreet street5 = new JStreet(0, 0);
	JStreet street6 = new JStreet(0, 0);
	JStreet street7 = new JStreet(0, 0);
	JStreet street8 = new JStreet(0, 0);
	JStreet street9 = new JStreet(0, 0);
	JStreet street10 = new JStreet(0, 0);
	JStreet street11 = new JStreet(0, 0);
	JStreet street12 = new JStreet(0, 0);
	
	@Before
	public void setUp() throws Exception {		
		street0.addAdjStreet(street1);
		street0.addAdjStreet(street2);
		street0.addAdjStreet(street3);
		street0.addAdjStreet(street4);
		
		street1.addAdjStreet(street5);
		street1.addAdjStreet(street6);
		street1.addAdjStreet(street2);
		street1.addAdjStreet(street0);
		street1.addAdjStreet(street3);

		street2.addAdjStreet(street0);
		street2.addAdjStreet(street1);
		street2.addAdjStreet(street4);
		street2.addAdjStreet(street8);
		street2.addAdjStreet(street7);
		
		street3.addAdjStreet(street12);
		street3.addAdjStreet(street11);
		street3.addAdjStreet(street1);
		street3.addAdjStreet(street0);
		street3.addAdjStreet(street4);
		
		street4.addAdjStreet(street9);
		street4.addAdjStreet(street10);
		street4.addAdjStreet(street2);
		street4.addAdjStreet(street0);
		street4.addAdjStreet(street3);
		
		street5.addAdjStreet(street1);
		street6.addAdjStreet(street1);
		street7.addAdjStreet(street2);
		street8.addAdjStreet(street2);
		street9.addAdjStreet(street4);
		street10.addAdjStreet(street4);
		street11.addAdjStreet(street3);
		street12.addAdjStreet(street3);
	}

	@Test
	public void test1() {
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
		dijkstra.execute(street0);
		List<JStreet> path = dijkstra.getPath(street7);
		assertTrue(path.get(0).equals(street0));
		assertTrue(path.get(path.size()-1).equals(street7));
	}
	
	@Test
	public void test2() {
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
		dijkstra.execute(street8);
		List<JStreet> path = dijkstra.getPath(street12);
		assertTrue(path.get(0).equals(street8));
		assertTrue(path.get(path.size()-1).equals(street12));
	}

	@Test
	public void test3() {
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
		dijkstra.execute(street4);
		List<JStreet> path = dijkstra.getPath(street4);
		assertTrue(path.size()==2);
		assertTrue(path.get(0).equals(street4));
		assertTrue(path.get(path.size()-1).equals(street4));
	}
}
