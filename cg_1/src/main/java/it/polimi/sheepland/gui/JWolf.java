package it.polimi.sheepland.gui;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

/**
 * This class represents wolf element on GUI.
 * 
 * @author Andrea
 *
 */
public class JWolf extends JMovableButton implements JAnimal {
	private static final long serialVersionUID = -2817516876823940514L;
	
	private static final int WIDTH = 60;
	private static final int HEIGHT = 38;
	private static final int PADDING = 0;
	
	private static final String IMAGE = "/media/wolf.png";
	private static final String SOUND = "/media/sounds/growl.wav";
	private static final int TIME_MILLISEC = 1500;
	
    private int x;
    private int y;
    private JRegion region;
	
    /**
     * This method is constructor for wolf.
     * 
     * <p>It sets up the timer, it adds region, then it specifies JLabel settings.</p>
     * 
     * @param region
     */
	public JWolf(JRegion region) {
		super();
		this.region = region;
		region.addAnimal(this);
		x = region.getAnimalX(JAnimalType.valueOf("WOLF"));
		y = region.getAnimalY(JAnimalType.valueOf("WOLF"));
		setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		setLocation(x, y);
		setSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setIcon(new ImageIcon(JWolf.class.getResource(IMAGE)));
		setDisabledIcon(new ImageIcon(JWolf.class.getResource(IMAGE)));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setEnabled(false);
		setVisible(true);
	}

	/**
	 * This method moves the wolf to the specified JRegion.
	 * 
	 * <p>The method gets coordinates of new region and moves the wolf to them.</p>
	 * 
	 * @param region
	 */
	public void move(JRegion region) {
		this.region.removeAnimal(this);
		this.region = region;
		region.addAnimal(this);
		int newX = region.getAnimalX(JAnimalType.valueOf("WOLF"));
		int newY = region.getAnimalY(JAnimalType.valueOf("WOLF"));
		if(newX!=this.x || newY!=this.y) {
			playSound(SOUND);
		}
		moveTo(new Point(newX,newY),TIME_MILLISEC);
		x = newX;
		y = newY;
	}

	/**
	 * This method returns current region of the wolf.
	 * 
	 * @return region
	 */
	public JRegion getRegion() {
		return region;
	}
	
	/**
	 * This method returns the type of animal.
	 * 
	 * @return type
	 */
	public JAnimalType getType() {
		return JAnimalType.WOLF;
	}
}