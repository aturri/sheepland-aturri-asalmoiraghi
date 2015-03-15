package it.polimi.sheepland.view;

/**
 * This class represents the input messages recieved
 * @author Andrea
 *
 */
public class InputMessage {
	private InputMessageType code;
	private Object input;

	/**
	 * Constructor for input message
	 * @param code
	 * @param input
	 */
	public InputMessage(InputMessageType code, Object input) {
		this.code = code;
		this.input = input;
	}

	/**
	 * @return the code
	 */
	public InputMessageType getCode() {
		return code;
	}

	/**
	 * @return the input
	 */
	public Object getInput() {
		return input;
	}
}
