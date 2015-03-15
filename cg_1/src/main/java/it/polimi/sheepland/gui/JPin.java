package it.polimi.sheepland.gui;

import java.awt.Cursor;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * This class represents the pin to select regions. It extends JButton.
 * 
 * @author Andrea
 *
 */
public class JPin extends JButton {
	private static final long serialVersionUID = 3860969059091252310L;
	
	private static final int WIDTH = 51;
	private static final int HEIGHT = 80;
	private static final int PADDING = 0;
	
	private static final String IMAGE = "/media/pin.png";
	
	/**
	 * This is constructor for pin. It sets up settings.
	 * 
	 * @param region
	 */
	public JPin(JRegion region) {
		setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		Point position = region.getLocation();
		int x = ((int) position.getX()) + ((region.getWidth()-WIDTH)/2);
		int y = ((int) position.getY()) + (region.getHeight()/2) - HEIGHT;
		setBounds(x, y, WIDTH, HEIGHT);
		setIcon(new ImageIcon(JPin.class.getResource(IMAGE)));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setVisible(true);
	}

}
