package it.polimi.sheepland.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * This class represents all movable buttons.
 * It extends JButton.
 * 
 * @author Andrea
 *
 */
public class JGrowableLabel extends JLabel {
	private static final long serialVersionUID = 334171427423706980L;
	
	private static final long DURATION_MUL = (long) 1E6;
	private static final int TIMER_DELAY = 10;
	
	private int endWidth;
	private int startWidth;
	private int height;
	private long startingTime;
	private long animationDuration;
	private boolean animating;
	
	/**
	 * This method is constructor for JGrowableLabel.
	 * 
	 * @param text
	 */
	public JGrowableLabel(String text) {
		super(text);
	}
	
	/**
	 * This method moves the component from its location to the point of destination.
	 * 
	 * @param destination
	 * @param timeMillisec
	 */
	public void growTo(int destination, int timeMillisec) {
		startingTime = System.nanoTime();
		animationDuration = (long) (timeMillisec*DURATION_MUL);
		startWidth = getWidth();
		this.height = getHeight();
		this.endWidth = destination;
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
	 * This method returns y for overshoot function.
	 * @param x
	 * @return y
	 */
	public static double overshoot(double x) {
		if (x < 0.7) {
			return 2*Math.pow(x,2);
		} else if (x >= 0.7 || x < 1.0) {
			return -14.0 * Math.pow(x - 0.855, 2) + 1.3;
		} else {
			return 1;
		}
	}
	
	/**
	 * This method performs animation.
	 */
	private void performAnimation() {
		ActionListener animationTask = new ActionListener() {
			/**
			 * This method is called whenever is requested an animation for the component.
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
				
				double modifiedProgress = overshoot(progress);
				
				//Compute the new position using a simple proportion 
				double newWidth = startWidth + (endWidth - startWidth)*modifiedProgress;
				//check whether the animation must end
				if(progress == 1) {
					((Timer)evt.getSource()).stop();
					animating = false;
				}
				//Set the current location
				JGrowableLabel.this.setSize((int) newWidth, height);
			}
		};
		//Set up a timer that fire each 10ms, calling 
		//the method declare in the animationTask
		Timer timer = new Timer(TIMER_DELAY,animationTask);
		timer.start();
	}
}
