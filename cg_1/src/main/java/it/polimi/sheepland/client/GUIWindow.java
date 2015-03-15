package it.polimi.sheepland.client;

import it.polimi.sheepland.gui.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This class represents the frame of the game. It extends the JFrame class.
 * @author Andrea
 *
 */
public class GUIWindow extends JFrame {
	private static final long serialVersionUID = -3072091833275894902L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static final String TITLE = "Sheepland";
	private static final ImageIcon ICON = new ImageIcon(GUIWindow.class.getResource("/media/sheep.png"));
	private static final String FONT_FAMILY = "Herculanum";
	private static final String ISLAND = "src/main/java/media/island.jpg";
	private static final String ISLAND_TXT = "/media/Sheepland.txt";
	
	private static final String FIELD = "FIELD";
	private static final String MOUNTAIN = "MOUNTAIN";
	private static final String DESERT = "DESERT";
	private static final String LAKE = "LAKE";
	private static final String RIVER = "RIVER";
	private static final String WOOD = "WOOD";
	
	private static final int WIDTH_FRAME = 815;
	private static final int HEIGHT_FRAME = 700;

	private static final int PADDING_MM_W = 39;
	private static final int PADDING_MM_H = 0;
	
	private static final int PADDING_BC_W = 60;
	private static final int PADDING_BC_H = 0;
	private static final int WIDTH_BC = 328;
	private static final int HEIGHT_BC = 100;
	
	private static final int PLAYER_ICON_SIZE = 26;
	private static final int WIDTH_WILLY = 100;
	private static final int HEIGHT_WILLY = 122;
	private static final String WILLY_ICON = "/media/wolf_1.png";
	
	private static final int WIDTH_MSG = 328;
	private static final int HEIGHT_MSG = 100;
	
	private static final int REGION_Z_INDEX = 0;
	private static final int STREET_Z_INDEX = 1;
	private static final int OVINE_Z_INDEX = 2;
	private static final int WOLF_Z_INDEX = 1;
	private static final int SHEPHERD_Z_INDEX = 3;
	private static final int PIN_Z_INDEX = 4;
	
	private static final String SHOT_SOUND = "/media/sounds/shot.wav";
	private static final String BUY_SOUND = "/media/sounds/buy.wav";
	
	private static final int TIME_MILLISEC_GROW = 800;
	private static final int INITIAL_WIDTH_SCORE_BAR = 10;
	private static final int MIN_WIDTH_SCORE_BAR = 150;
	private static final int HEIGHT_SCORE_BAR = 40;
	private static final double PIXEL_MULT = 1.7;
	private static final int WIDTH_SCORE_BAR = 320;
	
	private static final String MOVE_SHEPHERD = "MOVE_SHEPHERD";
	private static final String MOVE_OVINE = "MOVE_OVINE";
	private static final String KILL_OVINE = "KILL_OVINE";
	private static final String COUPLE1 = "COUPLE1";
	private static final String COUPLE2 = "COUPLE2";
	private static final String BUY_CARD = "BUY_CARD";
	
	int w = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int h = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private JLayeredPane islandPanel;
	private JPanel rightPanel;	
	private JPanel menuTop;
	private JPanel menuMoves;
	private JPanel bottomCards;
	
	private JLabel message;
	private JLabel playerName;
	private JLabel playerIcon;
	private JLabel playerMoney;
	
	private JWolf wolf;
	private List<JOvine> listJOvines = new ArrayList<JOvine>();
	private List<JRegion> listJRegions = new ArrayList<JRegion>();
	private List<JStreet> listJStreets = new ArrayList<JStreet>();
	private List<JShepherd> listJShepherds = new ArrayList<JShepherd>();
	private List<JCard> listJCards = new ArrayList<JCard>();
	private List<JButton> listRegionButtons = new ArrayList<JButton>();
	
	private Map<String,JButton> mapTurnButtons = new HashMap<String,JButton>();
	private Map<String,JBoughtCard> mapBoughtCards = new HashMap<String,JBoughtCard>();
	private List<String> listCardsMarketName = new ArrayList<String>();
	private List<Integer> listCardsMarketCost = new ArrayList<Integer>();
	
	private EventWaiter waiter;
	private String lastTurn;
	
	private JPanel finalScorePane = null;
	
	/**
	 * This method is constructor for GUI. It initializes it.
	 */
	public GUIWindow() {
		LOGGER.setLevel(Level.INFO);
		startGUI();
	}
	
	/**
	 * This method sets up the event waiter to wait input from users.
	 * @param waiter
	 */
	public void setWaiter(EventWaiter waiter) {
		this.waiter = waiter;
	}
	
	/**
	 * This method launches the application the GUI. It sets up the panels and all other elements.
	 */
	private void startGUI() {	
		//start frame
		setupFrame();
	
		//set up island
		islandPanel = new JIsland(ISLAND);
		this.getContentPane().add(islandPanel);
		
		//set up right panel
		rightPanel = new JRightPanel();
		this.getContentPane().add(rightPanel);
		
		//set up top menu
		menuTop = new JRightMenu();
		rightPanel.add(menuTop, BorderLayout.NORTH);

		//set up menuMoves
		menuMoves = new JRightMenu();
		menuMoves.setBorder(new EmptyBorder(PADDING_MM_H, PADDING_MM_W, PADDING_MM_H, PADDING_MM_W));
		rightPanel.add(menuMoves, BorderLayout.CENTER);
		
		//set up user cards
		bottomCards = new JRightMenu();
		bottomCards.setBorder(new EmptyBorder(PADDING_BC_H, PADDING_BC_W, PADDING_BC_H, PADDING_BC_W));
		bottomCards.setPreferredSize(new Dimension(WIDTH_BC, HEIGHT_BC));
		rightPanel.add(bottomCards, BorderLayout.SOUTH);
		
		setupMenuTop();
		setupTurnButtons();
		setupBoughtCards();
		
		scanTxt(ISLAND_TXT);
		
		//set up regions and streets
		for(JRegion r: listJRegions) {
			islandPanel.add(r);
			islandPanel.setLayer(r, REGION_Z_INDEX);
		}
		for(JStreet s: listJStreets) {
			islandPanel.add(s);
			islandPanel.setLayer(s, STREET_Z_INDEX);
		}
		
		//now the frame can be visible
		this.setVisible(true);
	}

	/**
	 * This method scans a txt file to create the island.
	 * 
	 * <p>The txt file must be structurated in this way:<br/><br/>
	 * 
	 * STREET=[x],[y]<br/>
	 * STREET=280,281<br/>
	 * STREET=285,331<br/>
	 * STREET=250,357<br/>
	 * ...<br/>
	 * REGION=[sheepOffsetX],[sheepOffsetY]/[bSheepOffsetX],[bSheepOffsetY]/[ramOffsetX],[ramOffsetY]/[lambOffsetX],[lambOffsetY]/[wolfOffsetX],[wolfOffsetY]/[wodth],[height],[x],[y]<br/>
	 * REGION=4,14/23,-2/25,28/10,50/14,43/75,90,230,180<br/>
	 * REGION=-5,22/23,-2/28,24/7,45/26,60/75,95,300,230<br/>
	 * REGION=-2,44/23,20/32,40/10,70/10,96/80,125,290,315<br/>
	 * ...<br/>
	 * </p>
	 * 
	 * @param path
	 */
	private void scanTxt(String path) {
		Scanner scanner = new Scanner(GUIWindow.class.getResourceAsStream(path));
		try {
			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split("=");
				String name = tokens[0].trim();
				String data = tokens[1].trim();
				if("STREET".equals(name)) {
					createStreet(data);
				} else if("REGION".equals(name)) {
					createRegion(data);
				}
			}
		} finally {
			if(scanner!=null) {
				scanner.close();
			}
		}
	}

	/**
	 * This method creates a region.
	 * 
	 * It sets up width, height, x, y and offset for every animal.
	 * 
	 * @param data
	 */
	private void createRegion(String data) {
		Map<JAnimalType,Point> offset = new HashMap<JAnimalType,Point>();
		String[] values = data.split("/");
		offset = createOffset(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim());

		String[] regionValues = values[5].split(",");
		int width = Integer.parseInt(regionValues[0].trim());
		int height = Integer.parseInt(regionValues[1].trim());
		int x = Integer.parseInt(regionValues[2].trim());
		int y = Integer.parseInt(regionValues[3].trim());
		JRegion region = new JRegion(width, height, x, y, offset);
		listJRegions.add(region);
	}

	/**
	 * This method sets up the offset for every animal.
	 * 
	 * @param sheep
	 * @param blacksheep
	 * @param ram
	 * @param lamb
	 * @param wolf
	 * @return map
	 */
	private Map<JAnimalType,Point> createOffset(String sheep, String blacksheep, String ram, String lamb, String wolf) {
		Map<JAnimalType,Point> offset = new HashMap<JAnimalType,Point>();
		int x;
		int y;
		
		//for sheeps
		String[] valuesSheep = sheep.split(",");
		x = Integer.parseInt(valuesSheep[0].trim());
		y = Integer.parseInt(valuesSheep[1].trim());
		offset.put(JAnimalType.valueOf("SHEEP"), new Point(x,y));

		//for black sheeps
		String[] valuesBlackSheep = blacksheep.split(",");
		x = Integer.parseInt(valuesBlackSheep[0].trim());
		y = Integer.parseInt(valuesBlackSheep[1].trim());
		offset.put(JAnimalType.valueOf("BLACKSHEEP"), new Point(x,y));
		
		//for rams
		String[] valuesRam = ram.split(",");
		x = Integer.parseInt(valuesRam[0].trim());
		y = Integer.parseInt(valuesRam[1].trim());
		offset.put(JAnimalType.valueOf("RAM"), new Point(x,y));
		
		//for lambs
		String[] valuesLamb = lamb.split(",");
		x = Integer.parseInt(valuesLamb[0].trim());
		y = Integer.parseInt(valuesLamb[1].trim());
		offset.put(JAnimalType.valueOf("LAMB"), new Point(x,y));
		
		//for wolfs
		String[] valuesWolf = wolf.split(",");
		x = Integer.parseInt(valuesWolf[0].trim());
		y = Integer.parseInt(valuesWolf[1].trim());
		offset.put(JAnimalType.valueOf("WOLF"), new Point(x,y));
		
		return offset;
	}

	/**
	 * This method creates a street.
	 * 
	 * It sets up x and y.
	 * 
	 * @param data
	 */
	private void createStreet(String data) {
		String[] values = data.split(",");
		int x = Integer.parseInt(values[0].trim());
		int y = Integer.parseInt(values[1].trim());
		JStreet street = new JStreet(x, y);
		listJStreets.add(street);
	}

	/**
	 * This method sets up the application frame. It sets up title and some settings.
	 */
	private void setupFrame() {
		this.setTitle(TITLE);
		this.setIconImage(ICON.getImage());
		this.setResizable(false);
		this.setBounds((w-WIDTH_FRAME)/2, (h-HEIGHT_FRAME)/2, WIDTH_FRAME, HEIGHT_FRAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
	}

	/**
	 * This method sets up the top menu with its labels.
	 */
	private void setupMenuTop() {
		playerName = new JLabel();
		playerName.setForeground(Color.WHITE);
		playerName.setFont(new Font(FONT_FAMILY, Font.PLAIN, 18));
		menuTop.add(playerName);
		
		playerIcon = new JLabel();
		menuTop.add(playerIcon);
		playerIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		playerIcon.setSize(new Dimension(PLAYER_ICON_SIZE, PLAYER_ICON_SIZE));
		playerIcon.setPreferredSize(new Dimension(PLAYER_ICON_SIZE, PLAYER_ICON_SIZE));
		playerIcon.setMinimumSize(new Dimension(PLAYER_ICON_SIZE, PLAYER_ICON_SIZE));
		playerIcon.setMaximumSize(new Dimension(PLAYER_ICON_SIZE, PLAYER_ICON_SIZE));
		
		JLabel willyCoyote = new JLabel();
		menuTop.add(willyCoyote);
		willyCoyote.setBorder(new EmptyBorder(0, 0, 0, 0));
		willyCoyote.setSize(new Dimension(WIDTH_WILLY, HEIGHT_WILLY));
		willyCoyote.setPreferredSize(new Dimension(WIDTH_WILLY, HEIGHT_WILLY));
		willyCoyote.setMinimumSize(new Dimension(WIDTH_WILLY, HEIGHT_WILLY));
		willyCoyote.setMaximumSize(new Dimension(WIDTH_WILLY, HEIGHT_WILLY));
		willyCoyote.setIcon(new ImageIcon(GUIClientView.class.getResource(WILLY_ICON)));
		
		playerMoney = new JLabel();
		playerMoney.setForeground(Color.WHITE);
		playerMoney.setFont(new Font(FONT_FAMILY, Font.PLAIN, 32));
		menuTop.add(playerMoney);	
	}

	/**
	 * This method initializes the bought cards list.
	 */
	private void setupBoughtCards() {		
		JBoughtCard cardField = new JBoughtCard(0,FIELD);
		bottomCards.add(cardField);
		mapBoughtCards.put(FIELD, cardField);
		
		JBoughtCard cardDesert = new JBoughtCard(0,DESERT);
		bottomCards.add(cardDesert);
		mapBoughtCards.put(DESERT, cardDesert);
		
		JBoughtCard cardRiver = new JBoughtCard(0,RIVER);
		bottomCards.add(cardRiver);
		mapBoughtCards.put(RIVER, cardRiver);
		
		JBoughtCard cardWood = new JBoughtCard(0,WOOD);
		bottomCards.add(cardWood);
		mapBoughtCards.put(WOOD, cardWood);
		
		JBoughtCard cardLake = new JBoughtCard(0,LAKE);
		bottomCards.add(cardLake);
		mapBoughtCards.put(LAKE, cardLake);
		
		JBoughtCard cardMountain = new JBoughtCard(0,MOUNTAIN);
		bottomCards.add(cardMountain);		
		mapBoughtCards.put(MOUNTAIN, cardMountain);
	}

	/**
	 * This method initializes the turn type buttons.
	 */
	private void setupTurnButtons() {
		message	= new JLabel();
		message.setPreferredSize(new Dimension(WIDTH_MSG,HEIGHT_MSG));
		message.setFont(new Font(FONT_FAMILY, Font.PLAIN, 18));
		message.setHorizontalAlignment(SwingConstants.CENTER);
		menuMoves.add(message);
		
		JButton buttonMoveShepherd = new JTurnButton("Muovi pastore","/media/turns/move_shepherd.png","/media/turns/move_shepherd_disabled.png");
		buttonMoveShepherd.setEnabled(false);
		menuMoves.add(buttonMoveShepherd);
		mapTurnButtons.put(MOVE_SHEPHERD, buttonMoveShepherd);
		
		JButton buttonBuyCard = new JTurnButton("Acquista tessera","/media/turns/buy_card.png","/media/turns/buy_card_disabled.png");
		buttonBuyCard.setEnabled(false);
		menuMoves.add(buttonBuyCard);
		mapTurnButtons.put(BUY_CARD, buttonBuyCard);
		
		JButton buttonMoveOvine = new JTurnButton("Muovi ovino","/media/turns/move_ovine.png","/media/turns/move_ovine_disabled.png");
		buttonMoveOvine.setEnabled(false);
		menuMoves.add(buttonMoveOvine);
		mapTurnButtons.put(MOVE_OVINE, buttonMoveOvine);
		
		JButton buttonKillOvine = new JTurnButton("Abbatti ovino","/media/turns/kill_ovine.png","/media/turns/kill_ovine_disabled.png");
		buttonKillOvine.setEnabled(false);
		menuMoves.add(buttonKillOvine);
		mapTurnButtons.put(KILL_OVINE, buttonKillOvine);
		
		JButton buttonCouple1 = new JTurnButton("Accoppiamento pecora-pecora (dado)","/media/turns/coupling_sheep_sheep.png","/media/turns/coupling_sheep_sheep_disabled.png");
		buttonCouple1.setEnabled(false);
		menuMoves.add(buttonCouple1);
		mapTurnButtons.put(COUPLE1, buttonCouple1);
		
		JButton buttonCouple2 = new JTurnButton("Accoppiamento pecora-montone","/media/turns/coupling_sheep_ram.png","/media/turns/coupling_sheep_ram_disabled.png");
		buttonCouple2.setEnabled(false);
		menuMoves.add(buttonCouple2);
		mapTurnButtons.put(COUPLE2, buttonCouple2);
	}
	
	/**
	 * This method shows a message in the label above the turn type buttons.
	 * @param string
	 */
	public void showMessage(String string) {
		message.setForeground(Color.WHITE);
		message.setText(string);
	}

	/**
	 * This method disables all clickable elements.
	 */
	public void disableClicks() {
		listCardsMarketName.clear();
		listCardsMarketCost.clear();
		disableStreets();
		disableShepherds();
		disableCards();
		disableOvines();
		disableTurns();
		disableRegions();
		islandPanel.repaint();
	}

	/**
	 * This method destroys list of clickable regions.
	 */
	private void disableRegions() {
		if(!listRegionButtons.isEmpty()) {
			for(JButton jBr: listRegionButtons) {
				islandPanel.remove(jBr);
			}
			listRegionButtons.clear();
		}
 	}

	/**
	 * This method disables all cards ready to be bought.
	 */
	private void disableCards() {
		for(JCard c: listJCards) {
			islandPanel.remove(c);
			c.setVisible(false);
		}
		listJCards.clear();
		menuMoves.repaint();
	}

	/**
	 * This method disables all clickable ovines.
	 */
	private void disableOvines() {
		for(JOvine o: listJOvines) {
			o.disableClick();
		}
	}

	/**
	 * This method disables all turn type buttons.
	 */
	private void disableTurns() {
		List<JButton> listButtons = new ArrayList<JButton>();
		listButtons.addAll(mapTurnButtons.values());
		for(JButton jB: listButtons) {
			for(ActionListener al: jB.getActionListeners()) {
		       jB.removeActionListener(al);
		    }
			jB.setEnabled(false);
		}
	}

	/**
	 * This method disables all clickable shepherds.
	 */
	private void disableShepherds() {
		for(JShepherd s: listJShepherds) {
			s.disableClick();
		}
	}

	/**
	 * This method disables all clickable streets.
	 */
	private void disableStreets() {
		for(int i=0;i<listJStreets.size();i++) {
			JStreet s = listJStreets.get(i);
			if(!s.isFenced() && !s.isShepherd()) {
				s.disableClick();
			}
		}
	}

	/**
	 * This method activates all the streets that can be activated.
	 */
	public void activateStreets() {
		for(int i=0;i<listJStreets.size();i++) {
			JStreet s = listJStreets.get(i);
			if(!s.isFenced() && !s.isShepherd()) {
				s.enableClick();
				addListener(s,i);
			}
		}
	}

	/**
	 * This method adds a listener for click at the button. It contains the string for turn type in order to recognize the turn type and add some effects.
	 * @param button
	 * @param i
	 */
	public void addListener(JButton button, final String turnType, final int i) {
		button.addActionListener(new ActionListener() {
        	/**
        	 * This method is called whenever the user clicks on the component.
        	 */
			public void actionPerformed(ActionEvent e) {
				waiter.notifyClick(i);
				lastTurn = turnType;
			}		
		});
	}
	
	/**
	 * This method adds a listener for click at the button.
	 * @param button
	 * @param i
	 */
	public void addListener(JButton button, final int i) {
		button.addActionListener(new ActionListener() {
        	/**
        	 * This method is called whenever the user clicks on the component.
        	 */
			public void actionPerformed(ActionEvent e) {
				waiter.notifyClick(i);
			}		
		});
	}
	
	/**
	 * This method adds a listener for click at the button. It plays a sound.
	 * @param button
	 * @param i
	 */
	public void addListener(JButton button, final int i, final String audioFilePath) {
		button.addActionListener(new ActionListener() {
        	/**
        	 * This method is called whenever the user clicks on the component.
        	 */
			public void actionPerformed(ActionEvent e) {
				waiter.notifyClick(i);
				playSound(audioFilePath);
			}		
		});
	}
	
	/**
	 * This method adds a listener for click at the layered pane. It plays a sound.
	 * @param layered pane
	 * @param i
	 */
	public void addListener(JLayeredPane button, final int i, final String audioFilePath) {
		button.addMouseListener(new MouseListener() {
        	/** This method is called whenever the user clicks on the component.*/
			public void mouseClicked(MouseEvent e) {
				playSound(audioFilePath);
				waiter.notifyClick(i);
			}
        	/** This method is called whenever the user has the mouse down on the component.*/
			public void mousePressed(MouseEvent e) {
				return;
			}
        	/** This method is called whenever the user releases the click on the component. */
			public void mouseReleased(MouseEvent e) {
				return;
			}
        	/** This method is called whenever the user has the mouse on the component. */
			public void mouseEntered(MouseEvent e) {
				return;
			}
        	/** This method is called whenever the user has the mouse on the component and comes out. */
			public void mouseExited(MouseEvent e) {
				return;
			}			
		});
	}
	
	/**
	 * This method plays a sound.
	 * @param path of the wav file
	 */
	public void playSound(String path) {
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(GUIWindow.class.getResource(path));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Canot play sound",e);
		}
	}

	/**
	 * This method updates player name.
	 * @param name
	 */
	public void setPlayerName(String name) {
		playerName.setText(name);
	}
	
	/**
	 * This method updates player's icon
	 * @param num player
	 */
	public void setPlayerIcon(int num) {
		playerIcon.setIcon(new ImageIcon(GUIClientView.class.getResource("/media/she"+Integer.toString(num)+".png")));
	}
	
	/**
	 * This method updates player's money 
	 * @param money
	 */
	public void setPlayerMoney(int money) {
		playerMoney.setText(Integer.toString(money)+" $");
	}

	/**
	 * This method returns the Jregion corresponding to the given id.
	 * It returns null if there isn't the corresponding region.
	 * @param id
	 * @return JRegion
	 */
	public JRegion getRegionById(int id) {
		if(id<=listJRegions.size()) {
			return listJRegions.get(id);
		}
		return null;
	}

	/**
	 * This method returns the Jovine corresponding to the given id.
	 * It returns null if there isn't the corresponding ovine.
	 * @param id
	 * @return JOvine
	 */
	public JOvine getOvineById(int id) {
		for(JOvine o: listJOvines) {
			if(o.getId()==id) {
				return o;
			}
		}
		return null;
	}

	/**
	 * This method opens an alert with the error message.
	 * @param message
	 */
	public void showError(String message) {
		JOptionPane.showMessageDialog(this, message, TITLE, JOptionPane.INFORMATION_MESSAGE, ICON);
	}

	/**
	 * This method tries to move the shepherd to the given street.
	 * If there isn't the Jshepherd it creates it.
	 * @param numPlayer
	 * @param numShepherd
	 * @param idStreet (destination)
	 */
	public void moveShepherdTo(int numPlayer, int numShepherd, int idStreet) {
		JShepherd jShepherd = getShepherd(numPlayer,numShepherd);
		if(jShepherd!=null) {
			JStreet newStreet = getStreetById(idStreet);
			JStreet oldStreet = jShepherd.getStreet();
			newStreet.setShepherd(jShepherd);
			jShepherd.move(newStreet);
			oldStreet.setFenced();
		} else {
			createShepherd(numPlayer, numShepherd, idStreet);
		}
	}

	/**
	 * This method creates the shepherd in the given street.
	 * @param numPlayer
	 * @param numShepherd
	 * @param idStreet
	 */
	private void createShepherd(int numPlayer, int numShepherd, int idStreet) {
		JStreet newStreet = getStreetById(idStreet);
		JShepherd jShepherd = new JShepherd(newStreet, numPlayer, numShepherd);
		listJShepherds.add(jShepherd);
		islandPanel.add(jShepherd);
		islandPanel.setLayer(jShepherd, SHEPHERD_Z_INDEX);	
	}

	/**
	 * This method returns the Jstreet corresponding to the given id.
	 * @param idStreet
	 * @return JStreet
	 */
	private JStreet getStreetById(int idStreet) {
		return listJStreets.get(idStreet);
	}

	/**
	 * This method returns the Jshepherd corresponding to player number and shepherd number. 
	 * @param numPlayer
	 * @param numShepherd
	 * @return JShepherd
	 */
	private JShepherd getShepherd(int numPlayer, int numShepherd) {
		for(JShepherd jS: listJShepherds) {
			if(jS.getNumPlayer()==numPlayer && jS.getNumShepherd()==numShepherd) {
				return jS;
			}
		}
		return null;
	}

	/**
	 * This method tries to move the ovine to the given region.
	 * If there isn't the ovine, it creates it on the region.
	 * @param idOvine
	 * @param idRegion
	 * @param type of ovine
	 */
	public void moveOvineTo(int idOvine, int idRegion, String type) {
		JOvine jOvine = getOvineById(idOvine);
		if(jOvine!=null) {
			JRegion newRegion = getRegionById(idRegion);
			jOvine.move(newRegion);					
		} else {
			createOvine(idOvine, idRegion, type);
		}
	}

	/**
	 * This method creates the ovine (of the given type) in the given region.
	 * @param idOvine
	 * @param idRegion
	 * @param type of ovine
	 */
	private void createOvine(int idOvine, int idRegion, String type) {
		JOvine jOvine = new JOvine(idOvine, getRegionById(idRegion), type);
		listJOvines.add(jOvine);
		islandPanel.add(jOvine);
		islandPanel.setLayer(jOvine, OVINE_Z_INDEX);	
	}

	/**
	 * This method tries to move the wolf to a given region.
	 * If there isn't the wolf, it creates it.
	 * @param idRegion
	 */
	public void moveWolfTo(int idRegion) {
		if(wolf!=null) {
			JRegion newRegion = getRegionById(idRegion);
			wolf.move(newRegion);					
		} else {
			createWolf(idRegion);
		}
		islandPanel.repaint();
	}

	/**
	 * This method creates the wolf in the given region.
	 * @param idRegion
	 */
	private void createWolf(int idRegion) {
		wolf = new JWolf(listJRegions.get(idRegion));
		islandPanel.add(wolf);
		islandPanel.setLayer(wolf, WOLF_Z_INDEX);
	}

	/**
	 * This method returns the list of J regions.
	 * @return listJRegions
	 */
	public List<JRegion> getListJRegions() {
		return listJRegions;
	}

	/**
	 * This method returns the list of J ovines.
	 * @return listJovines
	 */
	public List<JOvine> getListJOvines() {
		return listJOvines;
	}

	/**
	 * This method removes the ovine.
	 * @param id of the ovine
	 */
	public void removeOvine(int id) {
		JOvine jOvine = this.getOvineById(id);
		if(jOvine!=null) {
			JRegion jRegion = jOvine.getRegion();
			jRegion.removeAnimal(jOvine);
			jRegion.updateCounter(jOvine.getType());
			listJOvines.remove(jOvine);
			islandPanel.remove(jOvine);
			jOvine.setVisible(false);
		}
		islandPanel.repaint();
	}

	/**
	 * This method resets the counter for bought cards.
	 */
	public void clearCards() {
		List<JBoughtCard> listBoughtCards = new ArrayList<JBoughtCard>();
		listBoughtCards.addAll(mapBoughtCards.values());
		for(JBoughtCard jBc: listBoughtCards) {
			jBc.resetNumber();
		}
	}
	
	/**
	 * This method adds a bought card of the given type.
	 * @param terrainType
	 */
	public void updateCard(String terrainType) {
		JBoughtCard jBc = mapBoughtCards.get(terrainType);
		jBc.addOneCard();
	}

	/**
	 * This method activated the shepherd corresponding to player number and shepherd number.
	 * It sets up the listener with the shepherd number.
	 * @param playerNum
	 * @param shepherdNum
	 */
	public void activateShepherd(int playerNum, int shepherdNum) {
		JShepherd jShepherd = this.getShepherd(playerNum, shepherdNum);
		jShepherd.enableClick();
		addListener(jShepherd,shepherdNum);
	}

	/**
	 * This method activates the turn type corresponding to the name.
	 * It sets up the listener with index and turn type to keep trace of last turn (useful for some effects).
	 * @param name
	 * @param index to return
	 */
	public void activateTurnType(String name, int index) {
		JButton turnButton = mapTurnButtons.get(name);
		turnButton.setEnabled(true);
		addListener(turnButton, name, index);	
	}

	/**
	 * This method activates the ovine corresponding to the id.
	 * It sets up the listener with the index.
	 * @param ovineId
	 * @param index to return
	 */
	public void activateOvine(int ovineId, int index) {
		JOvine jOvine = this.getOvineById(ovineId);
		jOvine.enableClick();
		if(KILL_OVINE.equals(lastTurn)) {
			addListener(jOvine,index,SHOT_SOUND);
		} else {
			addListener(jOvine,index);
		}
	}

	/**
	 * This method creates a new card to be bought.
	 * It sets up the listener with the index to return.
	 * @param terrainType
	 * @param cost
	 * @param index to return
	 */
	public void showNewCard(String terrainType, int cost, int index) {
		JCard card = new JCard(terrainType, cost);
		addListener(card,index,BUY_SOUND);
		listJCards.add(card);
		menuMoves.add(card);
	}
	
	/**
	 * This method activates the market/buy dialog box.
	 * @param name
	 * @param cost
	 * @param i
	 */
	public void showNewDialogCard(String name, int cost) {
		listCardsMarketName.add(name);
		listCardsMarketCost.add(cost);
	}

	/**
	 * This method activates the region corresponding to the id.
	 * It sets up the listener with the index to return.
	 * @param regionId
	 * @param index to return
	 */
	public void activateRegion(int regionId, int index) {
		JRegion region = getRegionById(regionId);
		JPin regionButton = new JPin (region);
		addListener(regionButton,index);
		listRegionButtons.add(regionButton);
		islandPanel.add(regionButton);
		islandPanel.setLayer(regionButton, PIN_Z_INDEX);
		islandPanel.repaint();
	}

	/**
	 * This method changes the icon of the street (obtained from id) to the "free"
	 * @param id of the street
	 */
	public void setFreeStreet(int id) {
		JStreet jStreet = getStreetById(id);
		jStreet.setCost("0");
	}

	/**
	 * This method activates the dialog window to ask if user wants to sell a card.
	 * @param message
	 */
	public void activateDialogMarketAsk(String message) {
		MarketDialog dialog = new MarketDialog();
		int input = dialog.askPut(message);
		waiter.notifyClick(input);
	}

	/**
	 * This method activates the dialog window to ask the price of the card he wants to sell.
	 * @param message
	 */
	public void activateDialogMarketAskPrice() {
		MarketDialog dialog = new MarketDialog();
		String inputStr = null;
		while(!isInteger(inputStr)) {
			inputStr = dialog.askPrice();
			if(isInteger(inputStr) && (Integer.parseInt(inputStr)<0 || Integer.parseInt(inputStr)>60)) {
				inputStr = null;
			}
		}
		int input = Integer.parseInt(inputStr);
		waiter.notifyClick(input);
	}
	
	/**
	 * This method checks if a string can be converted to an integer.
	 * @param string
	 * @return true if string can be converted
	 */
	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
			LOGGER.log(Level.FINEST,e.getMessage(),e);
	        return false; 
	    }
	    return true;
	}

	/**
	 * This method activates the dialog box for market/buy
	 */
	public void activateDialogMarketBuy() {
		MarketDialog dialog = new MarketDialog();
		int input = dialog.askBuy(listCardsMarketName, listCardsMarketCost);
		waiter.notifyClick(input);
	}

	/**
	 * This method shows final score for a player.
	 * @param string
	 */
	public void showScore(List<?> listItems) {
		if(finalScorePane==null) {
			createFinalScorePanel();
		}
		
		for(Object o: listItems) {
			String string = (String) o;
			String player = string.substring(0,string.indexOf(':'));
			String points = string.substring(string.indexOf(':')+1);
			Color color = getPlayerColor(player);

			int score = (int) (Integer.parseInt(points)*PIXEL_MULT) + MIN_WIDTH_SCORE_BAR;

			JLayeredPane playerPane = new JLayeredPane();
			playerPane.setBorder(new EmptyBorder(30, 0, 0, 0));
			playerPane.setPreferredSize(new Dimension(WIDTH_SCORE_BAR, HEIGHT_SCORE_BAR));
			finalScorePane.add(playerPane);
			playerPane.setLayout(null);

			JGrowableLabel playerLabel = new JGrowableLabel(player+" - "+points);
			playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
			playerLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
			playerLabel.setForeground(Color.WHITE);
			playerLabel.setOpaque(true);
			playerLabel.setBackground(color);
			playerLabel.setBounds(0, 0, INITIAL_WIDTH_SCORE_BAR, HEIGHT_SCORE_BAR);
			playerLabel.growTo(score, TIME_MILLISEC_GROW);
			playerPane.add(playerLabel);
		}

	}

	/**
	 * This method parses the player's name to get his color.
	 * @param player
	 * @return color
	 */
	private Color getPlayerColor(String player) {
		Color color = null;
		if("Giocatore 1".equals(player)) {
			color = new Color(220, 20, 60);
		} else if("Giocatore 2".equals(player)) {
			color = new Color(0, 0, 139);
		} else if("Giocatore 3".equals(player)) {
			color = new Color(34, 139, 34);
		} else if("Giocatore 4".equals(player)) {
			color = new Color(240, 230, 140);
		} else {
			color = new Color(70,70,70);
		}
		return color;
	}

	/**
	 * This method creates the final score panel.
	 */
	private void createFinalScorePanel() {
		showMessage("Fine!");
		finalScorePane = new JRightMenu();
		rightPanel.remove(menuMoves);
		rightPanel.add(finalScorePane, BorderLayout.CENTER);
	}

	/**
	 * This method sets up the adjacent streets for the street corresponding to the id.
	 * 
	 * @param id
	 * @param listAdjStreets
	 */
	public void setAdjStreets(int id, List<Integer> listAdjStreets) {
		JStreet street = getStreetById(id);
		for(Integer i: listAdjStreets) {
			street.addAdjStreet(getStreetById(i));
		}
	}
}