package it.polimi.sheepland.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.EmptyBorder;

/**
 * This class represents the card that can be bought by the player.
 * 
 * @author Andrea
 *
 */
public class JCard extends JLayeredPane {
	private static final long serialVersionUID = 7310275823734584372L;
	
	private static final int WIDTH = 90;
	private static final int HEIGHT = 90;
	private static final int COIN_WIDTH = 50;
	private static final int COIN_HEIGHT = 50;
	private static final int COIN_X = 0;
	private static final int COIN_Y = 0;
	private static final int PADDING = 0; 
	private static final int BG_X = 0;
	private static final int BG_Y = 0;
	
	private static final String BG_PATH = "src/main/java/media/";
	private static final String COINS_PATH = "/media/coins_";
	private static final String EXT = ".png";
	
	private Image backgroundImage;
	
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * This method is constructor for Jcard.
	 * 
	 * <p>It sets up the dimensions, background image and the coins image.</p>
	 * 
	 * @param terrainType
	 * @param cost
	 */
	public JCard(String terrainType, int cost) {
		LOGGER.setLevel(Level.INFO);
		this.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		try {
			backgroundImage = ImageIO.read(new File(BG_PATH+terrainType+EXT));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,"Image not found",e);
		}
		if(cost>0 && cost<5) {
			JLabel coins = new JLabel();
			coins.setIcon(new ImageIcon(JCard.class.getResource(COINS_PATH+Integer.toString(cost)+EXT)));
			coins.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
			coins.setLocation(COIN_X, COIN_Y);
			coins.setSize(new Dimension(COIN_WIDTH, COIN_HEIGHT));
			coins.setPreferredSize(new Dimension(COIN_WIDTH, COIN_HEIGHT));
			coins.setMinimumSize(new Dimension(COIN_WIDTH, COIN_HEIGHT));
			coins.setMaximumSize(new Dimension(COIN_WIDTH, COIN_HEIGHT));
			coins.setVisible(true);
			this.add(coins);
		}
	}

	/**
	 * This method draws the image as a background.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(backgroundImage, BG_X, BG_Y, this);
	 }
}
