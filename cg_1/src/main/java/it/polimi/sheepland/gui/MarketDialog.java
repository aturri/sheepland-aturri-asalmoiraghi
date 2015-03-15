package it.polimi.sheepland.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * This class represents dialog box for market phase.
 * 
 * @author Andrea
 *
 */
public class MarketDialog {
	private static final String TITLE = "Market";
	private static final String ASK_PUT = "Vuoi mettere in vendita questa tessera?";
	private static final String ASK_PRICE = "A quale prezzo?";
	private static final String ASK_BUY = "Vuoi comprare una tessera?";
	
	private static final int PADDING = 10;
	
	private static final int WIDTH_C = 100;
	private static final int HEIGHT_C = 40;
	
	private static final String PATH = "/media/";
	private static final String EXT = "_s.png";
	
    private static final int YES = 1;
    private static final int NO = 0;
    
    private JDialog dialog;
    private JPanel content;
    private JLabel label;
    
    private int choice = NO;
    private String cost = "1";
    
    /**
     * This method is constructor for market dialog box.
     */
    public MarketDialog() {
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setTitle(TITLE);  
        
        label = new JLabel();
        label.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    /**
     * This method sets up the dialog box to ask user if he wants to sell a card on market.
     * 
     * @param message
     * @return response input
     */
    public int askPut(String message) {
    	label.setIcon(new ImageIcon(JBoughtCard.class.getResource(PATH+message+EXT)));
        label.setText(ASK_PUT);

        JButton yesButton = new JButton("Si");
        yesButton.addActionListener(new ActionListener() {
        	/**
        	 * This is the method called whenever user clicks on button. It returns the value of choice.
        	 */
            public void actionPerformed(ActionEvent e) {
                choice = YES;
                JButton button = (JButton)e.getSource();
                SwingUtilities.getWindowAncestor(button).dispose();
            }
        });

        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener() {
        	/**
        	 * This is the method called whenever user clicks on button. It returns the value of choice.
        	 */
            public void actionPerformed(ActionEvent e) {
                choice = NO;
                JButton button = (JButton)e.getSource();
                SwingUtilities.getWindowAncestor(button).dispose();
            }
        });

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(yesButton);
        buttons.add(noButton);

        content = new JPanel(new BorderLayout());
        content.add(label, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        finalizeDialog();

        return choice;
    }

    /**
     * This method finalizes the dialog box.
     */
	private void finalizeDialog() {
        dialog.getContentPane().add(content);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
	}

	/**
	 * This method sets up dialog box to ask the price.
	 * 
	 * @param message
	 * @return string of price
	 */
	public String askPrice() {
        label.setText(ASK_PRICE);

        final JTextField textField = new JTextField();
        textField.setColumns(10);
        
        JButton submitButton = new JButton("Ok");
        submitButton.addActionListener(new ActionListener() {
        	/**
        	 * This is the method called whenever user clicks on button. It returns the value of choice.
        	 */
            public void actionPerformed(ActionEvent e) {
                cost = textField.getText();
                JButton button = (JButton)e.getSource();
                SwingUtilities.getWindowAncestor(button).dispose();
            }
        });

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(textField);
        buttons.add(submitButton);

        content = new JPanel(new BorderLayout());
        content.add(label, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        finalizeDialog(); 

        return cost;
	}

	/**
	 * This method asks to buy a card or not.
	 * 
	 * @param listCardNames
	 * @param listCardCost
	 * @return
	 */
	public int askBuy(List<String> listCardNames, List<Integer> listCardCost) {
        label.setText(ASK_BUY);

        JPanel buttons = new JPanel(new FlowLayout());
        
        for(int i=0;i<listCardNames.size();i++) {
        	final int value = i;
            JButton buttonCard = new JButton(listCardCost.get(i)+"$");
            buttonCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonCard.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
            buttonCard.setSize(new Dimension(WIDTH_C, HEIGHT_C));
            buttonCard.setPreferredSize(new Dimension(WIDTH_C, HEIGHT_C));
            buttonCard.setMinimumSize(new Dimension(WIDTH_C, HEIGHT_C));
            buttonCard.setMaximumSize(new Dimension(WIDTH_C, HEIGHT_C));
    		String path = PATH+listCardNames.get(i)+EXT;
    		buttonCard.setIcon(new ImageIcon(JOvine.class.getResource(path)));
            buttonCard.addActionListener(new ActionListener() {
	        	/**
	        	 * This is the method called whenever user clicks on button. It returns the value of choice.
	        	 */
                public void actionPerformed(ActionEvent e) {
                    choice = value;
                    JButton button = (JButton)e.getSource();
                    SwingUtilities.getWindowAncestor(button).dispose();
                }
            }); 
            buttons.add(buttonCard);
        }

        JButton noButton = new JButton("No");
        final int value = listCardNames.size();
        noButton.addActionListener(new ActionListener() {
        	/**
        	 * This is the method called whenever user clicks on button. It returns the value of choice.
        	 */
            public void actionPerformed(ActionEvent e) {
                choice = value;
                JButton button = (JButton)e.getSource();
                SwingUtilities.getWindowAncestor(button).dispose();
            }
        });
        buttons.add(noButton);
      
        content = new JPanel(new BorderLayout());
        content.add(label, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        finalizeDialog();

        return choice;
	}
}
