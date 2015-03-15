package it.polimi.sheepland.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * This class represents GUI elements for streets.
 * 
 * @author Andrea
 *
 */
public class JStreet extends JButton {
	private static final long serialVersionUID = -9031921950813878032L;
	
	private static int counter;
	
	private static final int WIDTH = 26;
	private static final int HEIGHT = 26;
	private static final int PADDING = 0;
	
	private static final String FENCE = "/media/fence.png";
	private static final String FINAL_FENCE = "/media/finalFence.png";
	private static final int MAX_FENCES = 20;
	private static final String FILE = "/media/street_";
	private static final String EXT = ".png";
	
	private Boolean fenced = false;
	private JShepherd shepherd;
	
	private List<JStreet> listAdjStreets = new ArrayList<JStreet>();
	
	/**
	 * This is constructor for JStreet. It requires the position.
	 * 
	 * @param x
	 * @param y
	 */
	public JStreet(int x, int y) {
		super();
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		setBounds(x, y, WIDTH, HEIGHT);
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setEnabled(false);
		setVisible(true);
	}
	
	/**
	 * This method sets the street unclickable and shows fence on it.
	 */
	public void setFenced() {
		fenced = true;
		unsetShepherd();
		if(counter<=MAX_FENCES) {
			setIcon(new ImageIcon(JStreet.class.getResource(FENCE)));
			setDisabledIcon(new ImageIcon(JStreet.class.getResource(FENCE)));
		} else {
			setIcon(new ImageIcon(JStreet.class.getResource(FINAL_FENCE)));
			setDisabledIcon(new ImageIcon(JStreet.class.getResource(FINAL_FENCE)));			
		}
		setEnabled(false);
		setVisible(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	/**
	 * This method says id the street is fenced.
	 * @return true if fenced
	 */
	public Boolean isFenced() {
		return fenced;
	}
	
	/**
	 * This method disables click on the street.
	 */
	public void disableClick() {
		removeAllActionListeners();
		setEnabled(false);
		setVisible(true);
	}

	/**
	 * This method removes all action listeners
	 */
	private void removeAllActionListeners() {
		for(ActionListener al: getActionListeners()) {
	        removeActionListener(al);
	    }
	}
	
	/**
	 * This method enables the click on the street. It sets the icon for cost 1.
	 */
	public void enableClick() {
		setCost("1");
		setEnabled(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setVisible(true);		
	}

	/**
	 * This method sets the icon corresponding to the cost.
	 * 
	 * @param cost (String)
	 */
	public void setCost(String cost) {
		setIcon(new ImageIcon(JStreet.class.getResource(FILE+cost+EXT)));
	}

	/**
	 * This method sets the shepherd on this street.
	 * 
	 * @param shepherd
	 */
	public void setShepherd(JShepherd shepherd) {
		hideStreet();
		this.shepherd = shepherd;
	}
	
	/**
	 * This method unsets the shepherd from this street.
	 */
	private void unsetShepherd() {
		this.shepherd = null;
	}
	
	/**
	 * This method says if there is a shepherd on the street.
	 * 
	 * @return true if there is a shepherd
	 */
	public boolean isShepherd() {
		return shepherd!=null;
	}

	/**
	 * This method hides the street.
	 */
	private void hideStreet() {
		removeAllActionListeners();
		setEnabled(false);
		setVisible(false);
	}
	
	/**
	 * This method increments the static counter.
	 */
	public void incrementCounter() {
		counter++;
	}

	/**
	 * This method adds an element in the adjacent streest list.
	 * 
	 * @param id of neighbour street
	 */
	public void addAdjStreet(JStreet adjStreet) {
		listAdjStreets.add(adjStreet);
	}
	
	/**
	 * This method returns the list of adjacent streets for the current street.
	 * 
	 * @return listAdJStreets
	 */
	public List<JStreet> getListAdjStreets() {
		return listAdjStreets;
	}
}