package it.polimi.sheepland.gui;

import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * This class represnts buttons for turn type. It extends JButton.
 * 
 * @author Andrea
 */
public class JTurnButton extends JButton {
	private static final long serialVersionUID = 837338147418861519L;

	private static final int PADDING = 0;
	private static final int WIDTH = 75;
	private static final int HEIGHT = 75;
	
	private String string;
	
	/**
	 * This method is constructor for JTurnButton.
	 * @param string
	 */
	public JTurnButton(String string, String pathEnabledIcon, String pathDisabledIcon) {
		this.string = string;
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		setSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setIcon(new ImageIcon(JTurnButton.class.getResource(pathEnabledIcon)));
		setDisabledIcon(new ImageIcon(JTurnButton.class.getResource(pathDisabledIcon)));
		setVisible(true);
	}
	
	/**
	 * This method returns button's string.
	 * @return
	 */
	public String getString() {
		return string;
	}
}
