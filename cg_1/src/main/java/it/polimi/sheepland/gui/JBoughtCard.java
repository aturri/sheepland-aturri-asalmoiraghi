package it.polimi.sheepland.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This class represents the icon of card type bought from user.
 * 
 * @author Andrea
 *
 */
public class JBoughtCard extends JLabel {
	private static final long serialVersionUID = -1245130213507049208L;
	
	private static final int WIDTH = 55;
	private static final int HEIGHT = 40;
	
	private static final String PATH = "/media/";
	private static final String EXT = "_s.png";
	
	private int number;

	/**
	 * This is constructor for the card.
	 * 
	 * @param number
	 * @param terrainType
	 */
	public JBoughtCard(int number, String terrainType) {
		super(Integer.toString(number));
		this.number = number;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setForeground(Color.WHITE);
		this.setBackground(Color.RED);
		this.setOpaque(true);
		this.setIcon(new ImageIcon(JBoughtCard.class.getResource(PATH+terrainType+EXT)));
	}
	
	/**
	 * This method resets number of cards.
	 */
	public void resetNumber() {
		number = 0;
		writeNumber();
	}
	
	/**
	 * This method adds one card.
	 */
	public void addOneCard() {
		number++;
		writeNumber();
	}
	
	/**
	 * This method writes the number of cards.
	 */
	private void writeNumber() {
		this.setText(Integer.toString(number));		
	}
}
