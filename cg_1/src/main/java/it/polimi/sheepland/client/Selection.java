package it.polimi.sheepland.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * This class represents selection dialog box.
 * @author Andrea
 *
 */
public class Selection {
	private static final String TITLE = "Start Sheepland";
	
	private static final int PADDING = 10;
    
    private JDialog dialog;
    private JPanel content;
    private JPanel buttons;
    private JLabel label;
    
    private int choice = 0;
    
    /**
     * This method sets up the dialog box to ask user something.
     * 
     * <p>The message is contained in the string parameter, then it adds a button for every possible choice.</p>
     * 
     * @param text
     * @param collection
     * @return choice
     */
	public int ask(String text, Collection<String> collection) {
		initializeDialog();
		label.setText(text);
        content.add(label, BorderLayout.CENTER);

        int i = 0;
		for(String option: collection){
			final int value = i;
	        JButton newButton = new JButton(option);
	        newButton.addActionListener(new ActionListener() {
	        	/**
	        	 * This is the method called whenever user clicks on button. It returns the value of choice.
	        	 */
	            public void actionPerformed(ActionEvent e) {
	                choice = value;
	                JButton button = (JButton)e.getSource();
	                SwingUtilities.getWindowAncestor(button).dispose();
	            }
	        });
	        buttons.add(newButton);
			i++;
		}

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
	 * This method initializes the dialog box.
	 */
	private void initializeDialog() {
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.setTitle(TITLE);  
        
        label = new JLabel();
        label.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        buttons = new JPanel(new FlowLayout());
        
        content = new JPanel(new BorderLayout());
	}
}
