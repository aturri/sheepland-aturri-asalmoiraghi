package it.polimi.sheepland.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

/**
 * This class represents the loading window shown while waiting for other clients to connect.
 * @author Andrea
 *
 */
public class Loading {
	private static final String TITLE = "Attendere";
	private static final int WIDTH = 200;
	private static final int HEIGHT = 200;
	private static final int PADDING_FRAME = 10;

	private static final int IMAGE_PADDING_LEFT = 39;
	private static final int IMAGE_PADDING = 0;
	private static final int IMAGE_WIDTH = 123;
	private static final int IMAGE_HEIGHT = 150;
	
	private static final String FONT_FAMILY = "Herculanum";
	private static final int FONT_SIZE = 18;
	
	private int w = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int h = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private JFrame loading;
	private JLabel labelWait;
	
	/**
	 * This method is constructor for loading window.
	 */
	public Loading(String text, String imagePath) {
		initalizeLoading();
		addLabelIcon(imagePath);
		addTextLabel(text);
		loading.getContentPane().add(labelWait, BorderLayout.CENTER);
		loading.pack();
		loading.setVisible(true);
	}
	
	/**
	 * This method sets up the text label with message.
	 */
	private void addTextLabel(String text) {
		labelWait = new JLabel(text);
		labelWait.setBorder(new EmptyBorder(PADDING_FRAME, PADDING_FRAME, PADDING_FRAME, PADDING_FRAME));
		labelWait.setFont(new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE));
	}

	/**
	 * This method sets up the label with image.
	 */
	private void addLabelIcon(String imagePath) {
		JLabel labelIcon = new JLabel();
		labelIcon.setBorder(new EmptyBorder(IMAGE_PADDING, IMAGE_PADDING_LEFT, IMAGE_PADDING, IMAGE_PADDING));
		labelIcon.setSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		labelIcon.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		labelIcon.setMinimumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		labelIcon.setIcon(new ImageIcon(GUIClientView.class.getResource(imagePath)));
		loading.getContentPane().add(labelIcon, BorderLayout.NORTH);
	}

	/**
	 * This method sets up the loading frame. It sets the title, close operaton, no resize, dimension, position, layout.
	 */
	private void initalizeLoading() {
		loading = new JFrame(TITLE);
		loading.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		loading.setResizable(false);
		loading.setBounds((w-WIDTH)/2, (h-HEIGHT)/2, WIDTH, HEIGHT);
		loading.getContentPane().setLayout(new BorderLayout());
	}

	/**
	 * This method sets the loading window visible or not.
	 * @param bool
	 */
	public void setVisible(boolean b) {
		loading.setVisible(b);		
	}

	/**
	 * This method sets the text of the label
	 * @param string
	 */
	public void setText(String string) {
		labelWait.setText(string);
	}

	/**
	 * This method shows an error
	 * @param message
	 */
	public void showError(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
