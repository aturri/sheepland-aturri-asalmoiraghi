package it.polimi.sheepland.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

/**
 * This class represents shepherds buttons.
 * 
 * @author Andrea
 *
 */
public class JShepherd extends JMovableButton {
	private static final long serialVersionUID = -952483007604671096L;
	
	private static final int WIDTH = 26;
	private static final int HEIGHT = 26;
	private static final int PADDING = 0;
	
	private static final String PATH = "/media/she";
	private static final String EXT = ".png";
	
	private static final String SOUND = "/media/sounds/steps.wav";
	private static final int TIME_MILLISEC = 2000;
	
    private int x;
    private int y;
    private JStreet street;
    private int playerNum;
    private int shepherdNum;
    
    /**
     * This method is constructor for JShepherd.
     * 
     * @param street where to place the shepherd
     * @param playerNum: the number of the player which owns the shepherd (1-4)
     * @param shepherdNum: the number of the shepherd (0-1)
     */
	public JShepherd(JStreet street, int playerNum, int shepherdNum) {
		super();
		this.street = street;
		street.setShepherd(this);
		this.playerNum = playerNum;
		this.shepherdNum = shepherdNum;
		x = street.getX();
		y = street.getY();
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		setLocation(x, y);
		String path = PATH+Integer.toString(playerNum)+EXT;
		setIcon(new ImageIcon(JOvine.class.getResource(path)));
		setDisabledIcon(new ImageIcon(JOvine.class.getResource(path)));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setEnabled(false);
		setVisible(true);
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
	 * This method moves the shepherd to the specified JStreet.
	 * 
	 * <p>The method gets coordinates of new street and moves the shepherd to them.</p>
	 * 
	 * @param street
	 */
	public void move(JStreet street) {
		int newX = street.getX();
		int newY = street.getY();
		if(newX!=this.x || newY!=this.y) {
			//get directions and update street
			List<JStreet> directions = getDirections(this.street, street);
			this.street = street;
			//play sound and increment counter
			playSound(SOUND);
			street.incrementCounter();
			//move shpeherd
			List<Point> listPoints = new ArrayList<Point>();
			for(JStreet moveStreet: directions) {
				int tmpX = moveStreet.getX();
				int tmpY = moveStreet.getY();
				listPoints.add(new Point(tmpX, tmpY));
			}
			moveTo(listPoints,TIME_MILLISEC);
			x = newX;
			y = newY;
		}
	}
	
	/**
	 * This method gets the path from Dijkstra Algorithm.
	 * 
	 * @param source
	 * @param destination
	 * @return list of streets
	 */
	private List<JStreet> getDirections(JStreet source, JStreet destination) {
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
		dijkstra.execute(source);
		List<JStreet> directions = dijkstra.getPath(destination);
		if(directions==null || directions.isEmpty()) {
			directions = new ArrayList<JStreet>();
			directions.add(source);
			directions.add(destination);
		}
		return directions;
	}

	/**
	 * This method returns current street of the shepherd.
	 * 
	 * @return street
	 */
	public JStreet getStreet() {
		return street;
	}
	
	/**
	 * This method returns the palyer number.
	 * 
	 * @return player number
	 */
	public int getNumPlayer() {
		return playerNum;
	}

	/**
	 * This method returns the shepherd number.
	 * 
	 * @return shepherd number
	 */
	public int getNumShepherd() {
		return shepherdNum;
	}
}