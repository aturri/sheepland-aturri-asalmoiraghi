package it.polimi.sheepland.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 * This class represents ovine buttons.
 * 
 * @author Andrea
 *
 */
public class JOvine extends JMovableButton implements JAnimal {
	private static final long serialVersionUID = 3170418479706444159L;
	
	private static final int WIDTH = 60;
	private static final int HEIGHT = 36;
	private static final int PADDING = 0;
	
	private static final String PATH = "/media/";
	private static final String EXT = ".png";
	private static final String SOUND = "/media/sounds/belating.wav";
	private static final int TIME_MILLISEC = 1500;
	
	private static final int LABEL_PADDING = 0;
	private static final int LABEL_PADDING_LEFT = -2;
	private static final int LABEL_PADDING_TOP = 28;
	
	private int id;
    private int x;
    private int y;
    private JRegion region;
    private JAnimalType type;
    private JLabel count;
	
    /**
     * This method is constructor for JOvine.
     * 
     * <p>It sets up the timer, it adds region and animal type, then it specifies JButton settings.</p>
     * 
     * @param id int
     * @param region JRegion
     * @param strType
     */
	public JOvine(int id, JRegion region, String strType) {
		super();
		this.id = id;
		this.region = region;
		this.type = JAnimalType.valueOf(strType);
		region.addAnimal(this);
		x = region.getAnimalX(type);
		y = region.getAnimalY(type);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		setLocation(x, y);
		setSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		String path = PATH+type.getName()+EXT;
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setIcon(new ImageIcon(JOvine.class.getResource(path)));
		setDisabledIcon(new ImageIcon(JOvine.class.getResource(path)));
		setEnabled(false);
		count = new JLabel();
		count.setBorder(new EmptyBorder(LABEL_PADDING_LEFT,LABEL_PADDING_TOP,LABEL_PADDING,LABEL_PADDING));
		add(count);
		setVisible(true);
	}
	
	/**
	 * This method returns the id of the ovine.
	 * @return id int
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * This method disables click.
	 */
	public void disableClick() {
		removeAllActionListeners();
		setEnabled(false);
		setVisible(true);
	}

	/**
	 * This method enables click.
	 */
	public void enableClick() {
		setEnabled(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setVisible(true);		
	}

	/**
	 * This method moves the ovine to the specified JRegion.
	 * 
	 * <p>The method gets coordinates of new region and moves the ovine to them.</p>
	 * 
	 * @param region
	 */
	public void move(JRegion region) {
		this.region.removeAnimal(this);
		this.region.updateCounter(type);
		this.region = region;
		region.addAnimal(this);
		region.updateCounter(type);
		int newX = region.getAnimalX(type);
		int newY = region.getAnimalY(type);
		if(newX!=this.x || newY!=this.y) {
			playSound(SOUND);
		}
		moveTo(new Point(newX,newY),TIME_MILLISEC);
		x = newX;
		y = newY;
	}

	/**
	 * This method returns current region of the ovine.
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
		return type;
	}
	
	/**
	 * This method sets the label in animal.
	 * 
	 * @param string
	 */
	public void setLabel(String string) {
		count.setText(string);
	}
}