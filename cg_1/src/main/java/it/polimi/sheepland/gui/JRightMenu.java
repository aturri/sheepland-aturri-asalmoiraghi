package it.polimi.sheepland.gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;

/**
 * This class represents all menus on the right panel. It extends JPanel.
 * 
 * @author Andrea
 *
 */
public class JRightMenu extends JPanel {
	private static final long serialVersionUID = -6943709880082747476L;

	/**
	 * This is constructor, it sets up the settings.
	 */
	public JRightMenu() {
		this.setOpaque(false);
		this.setLayout(new FlowLayout());
	}
}
