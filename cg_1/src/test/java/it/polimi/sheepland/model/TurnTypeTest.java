package it.polimi.sheepland.model;

import static org.junit.Assert.*;
import it.polimi.sheepland.model.TurnType;

import org.junit.Test;

public class TurnTypeTest {

	@Test
	public void test() {
		assertTrue(TurnType.valueOf("MOVE_SHEPHERD").getName()=="Muovi pastore");
	}

}
