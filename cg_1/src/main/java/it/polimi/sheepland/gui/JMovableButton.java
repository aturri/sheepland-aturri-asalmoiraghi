package it.polimi.sheepland.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 * This class represents all movable buttons. It extends JButton.
 * 
 * @author Andrea
 *
 */
public class JMovableButton extends JButton {
	private static final long serialVersionUID = 6354318666012227908L;
	
	private static final long DURATION_MUL = (long) 1E6;
	private static final int TIMER_DELAY = 10;
	
	private List<Point> points = new ArrayList<Point>();
	private long startingTime;
	private long animationDuration;
	private boolean animating;
	
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * This method moves the component from its location to the point of destination.
	 * 
	 * @param destination
	 * @param timeMillisec
	 */
	public void moveTo(Point destination, int timeMillisec) {
		startingTime = System.nanoTime();
		animationDuration = (long) (timeMillisec*DURATION_MUL);
		points.clear();
		//starting point
		points.add(getBounds().getLocation());
		//destination point
		points.add(destination);
		animating = true;
		performAnimation();
	}
	
	/**
	 * This method moves the component from its location to the point of destination, through all points in the list.
	 * 
	 * @param listOfPoints
	 * @param timeMillisec
	 */
	public void moveTo(List<Point> points, int timeMillisec) {
		startingTime = System.nanoTime();
		animationDuration = (long) (timeMillisec*DURATION_MUL);
		this.points.clear();
		this.points = points;
		animating = true;
		performAnimation();
	}
	 
	/**
	 * This method says if component is being animated.
	 * 
	 * @return true if animating
	 */
	public boolean isAnimating() {
		return animating;
	}
	
	/**
	 * This method computes the current position of component.
	 * 
	 * @param progress
	 * @return point of position
	 */
	private Point getPosition(double progress) {
		int lastIndex = points.size()-1;
		Point startPosition;
		Point endPosition;
		double localProgress;
		if(progress>=1) {
			startPosition = points.get(lastIndex-1);
			endPosition = points.get(lastIndex);	
			localProgress = progress;
		} else {
			double num = progress*lastIndex;
			startPosition = points.get((int) Math.floor(num));
			endPosition = points.get((int) Math.ceil(num));
			localProgress = num - Math.floor(num);
		}
		double newX = startPosition.x + (endPosition.x - startPosition.x)*localProgress;
		double newY = startPosition.y + (endPosition.y - startPosition.y)*localProgress;
		return new Point((int)newX,(int)newY);
	}
	
	/**
	 * This method performs animation.
	 */
	private void performAnimation() {
		ActionListener animationTask = new ActionListener() {
        	/**
        	 * This method is called whenever is requested an animation on the component.
        	 */
			public void actionPerformed(ActionEvent evt) {
				//get the current time in nanoseconds
				long now = System.nanoTime();
				//progress is a number between 0 and 1 represents the progress of the animation
				//according to the passed time from the start of the animation
				double progress =  now > (startingTime + animationDuration) ?
						1 : (double)(now - startingTime) / (double)animationDuration ;
				if(now < startingTime) {
					progress = 0;
				}
				Point newPosition = getPosition(progress);
				//check whether the animation must end
				if(progress == 1) {
					((Timer)evt.getSource()).stop();
					animating = false;
				}
				//Set the current location
				JMovableButton.this.setLocation(newPosition);
			}
		};
		//Set up a timer that fire each 10ms, calling 
		//the method declare in the animationTask
		Timer timer = new Timer(TIMER_DELAY,animationTask);
		timer.start();
	}
	
	/**
	 * This method plays a sound.
	 * 
	 * @param absolute path
	 */
	public void playSound(String path) {
		LOGGER.setLevel(Level.INFO);
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(JOvine.class.getResource(path));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Cannot play sound",e);
		}
	}
	
	/**
	 * This method removes all action listeners.
	 */
	public void removeAllActionListeners() {
		for(ActionListener al: getActionListeners()) {
	        removeActionListener(al);
	    }
	}
}
