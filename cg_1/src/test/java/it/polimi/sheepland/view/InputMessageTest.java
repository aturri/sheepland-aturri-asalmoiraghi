/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 26/mag/2014
 *
 */

package it.polimi.sheepland.view;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Andrea
 */
public class InputMessageTest {
	private InputMessage inputMex;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		@SuppressWarnings("unused")
		InputMessage inputMex = null;
	}


	/**
	 * Test method for {@link sheepland.view.InputMessage#InputMessage(sheepland.view.InputMessageType, java.lang.Object)}.
	 */
	@Test
	public void testInputMessage() {
		assertTrue(inputMex==null);
		inputMex = new InputMessage(InputMessageType.NUM_PLAYERS, Integer.getInteger("4"));
		assertTrue(inputMex!=null);
		assertTrue(inputMex instanceof InputMessage);
	}

	/**
	 * Test method for {@link sheepland.view.InputMessage#getCode()}.
	 */
	@Test
	public void testGetCode() {
		inputMex = new InputMessage(InputMessageType.NUM_PLAYERS, Integer.getInteger("4"));
		assertEquals(inputMex.getCode(),InputMessageType.NUM_PLAYERS);
	}

	/**
	 * Test method for {@link sheepland.view.InputMessage#getInput()}.
	 */
	@Test
	public void testGetInput() {
		inputMex = new InputMessage(InputMessageType.NUM_PLAYERS, Integer.getInteger("4"));
		assertEquals(inputMex.getInput(),Integer.getInteger("4"));
	}

}
