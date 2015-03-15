package it.polimi.sheepland.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;

/**
 * This class represents the JPanel with background.
 * 
 * @author Andrea
 *
 */
public class JPanelBG extends JLayeredPane {
	private static final long serialVersionUID = 8809258901076624498L;
	
	private static final int X = 0;
	private static final int Y= 0;
	
	private Image backgroundImage;
	
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * This is constructor for island panel.
	 * 
	 * @param fileName: path of the island board
	 */
	public JPanelBG(String fileName) {
		LOGGER.setLevel(Level.INFO);
		try {
			backgroundImage = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,"Image not found",e);
		}
	}

	/**
	 * This method draws the image as a background.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    //Draw the background image
	    g.drawImage(backgroundImage, X, Y, this);
	 }
}