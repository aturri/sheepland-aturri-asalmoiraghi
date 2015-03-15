package it.polimi.sheepland.gui;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * This class extends JPanel with background and represents the Island.
 * 
 * @author Andrea
 *
 */
public class JIsland extends JPanelBG {
	private static final long serialVersionUID = -7566771341901559643L;
	
	private static final int WIDTH = 488;
	private static final int HEIGHT = 700;
	private static final int X = 0;
	private static final int Y = 0;

	/**
	 * This is constructor for JIsland. It sets up the settigs.
	 * 
	 * @param fileName
	 */
	public JIsland(String fileName) {
		super(fileName);
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBounds(new Rectangle(X, Y, WIDTH, HEIGHT));
		this.setLayout(null);
	}
}
