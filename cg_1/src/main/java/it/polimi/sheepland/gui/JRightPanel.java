package it.polimi.sheepland.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * This class represents the right panel. It extends JPanel.
 * 
 * @author Andrea
 *
 */
public class JRightPanel extends JPanel {
	private static final long serialVersionUID = -8761792307962767683L;
	
	private static final int WIDTH = 328;
	private static final int HEIGHT = 700;
	private static final int BORDER = 0;
	private static final Color BG = new Color(33,161,244);

	/**
	 * This is constructor for JRight panel, it sets up settings.
	 */
	public JRightPanel() {
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(BG);
		this.setLayout(new BorderLayout(BORDER, BORDER));
	}
}
