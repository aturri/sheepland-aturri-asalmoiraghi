package it.polimi.sheepland.client;

import it.polimi.sheepland.model.BlackSheep;
import it.polimi.sheepland.model.Lamb;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.OvineType;
import it.polimi.sheepland.model.Ram;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

/**
 * This class represents GUI Client Adapter. It communicates with the GUI and ClientCommunicator.
 * @author Andrea
 *
 */
public class GUIClientView extends ClientView {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static final int SETUP_INT = -1;
	private static final String ASK_STREET = "ASK:STREET";
	private static final String ASK_SHEPHERD = "ASK:SHEPHERD";
	private static final String ASK_TURN = "ASK:TURN";
	private static final String ASK_OVINE = "ASK:OVINE";
	private static final String ASK_CARD = "ASK:CARD";
	private static final String ASK_REGION = "ASK:REGION";
	private static final String ASK_PUT = "ASK:PUT:";
	private static final String ASK_PRICE = "ASK:PRICE";
	private static final String ASK_BUY = "ASK:BUY:";
	
	private static final String INVOK_EX = "Cannot use invokeAndWait";
	private static final String UI_EX = "Cannot wait for user input";
	
	private static final String WAIT_MESSAGE = "Attendo avversari...";
	private static final String WAIT_IMAGE = "/media/wolf_1.png";
	private static final String WAIT_MESSAGE_1 = "Riconnessione...";
	private static final String WAIT_IMAGE_1 = "/media/loading.gif";
	private static final String DISCONNECTED_MESSAGE = "Connessione persa";
	
	//loading window
	private Loading loading;
	//GUI attributes
	private Boolean ready = false;
	private Boolean graphStreets = false;
	private GUIWindow window;
	//event attributes
	private int lastInput;
	
	/**
	 * Create the application.
	 * The constructor builds a window with a message explaining that the client is waiting for opponents.
	 */
	public GUIClientView() {
		loading = new Loading(WAIT_MESSAGE, WAIT_IMAGE);
		LOGGER.setLevel(Level.WARNING);
	}
	
	/**
	 * This method shows the island when ready
	 */
	@Override
	public void showDeck() {
		if(!ready) {
			loading.setVisible(false);
			loading = null;
			SwingUtilities.invokeLater(new Runnable() {
				/**
				 * This method starts the GUI Window and sets the graph of street in this class.
				 */
				public void run() {
					window = new GUIWindow();
					if(!graphStreets) {
						setupGraphStreets();
						graphStreets = true;
					}
				}		
			});
		}
		ready = true;
	}
	
	/**
	 * This method sets up the connections between streets in GUI.
	 */
	private void setupGraphStreets() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method sets up adjacent streets in JStreets.
			 */
			public void run() {
				for(Street street: client.getAllStreets()) {
					List<Integer> listAdjStreets = new ArrayList<Integer>();
					for(Street adjStreet: street.getListOfStreets()) {
						listAdjStreets.add(adjStreet.getId());
					}
					window.setAdjStreets(street.getId(), listAdjStreets);					
				}
			}		
		});
	}

	/**
	 * This method updates the position of the shepherd
	 */
	@Override
	public void showShepherd() {
		if(client.getShepherd().getStreet()!=null) {
			Shepherd s = client.getShepherd();
			final int playerNum = parsePlayerName(s.getPlayer().getName());
			final int shepherdNum = parseShepherdName(s.getName());
			final int streetId = s.getStreet().getId();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					/**
					 * This method asks the window to move the shepherd.
					 */
					public void run() {
						window.moveShepherdTo(playerNum, shepherdNum, streetId);
					}			
				});
			} catch (InvocationTargetException e) {
				LOGGER.log(Level.WARNING, INVOK_EX,e);
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, UI_EX,e);
			}
		}
	}

	/**
	 * This method parses the Shepherd name to get index
	 * @param name
	 * @return index
	 */
	private int parseShepherdName(String name) {
		String num = name.substring("Pastore ".length());
		return Integer.parseInt(num)-1;
	}

	/**
	 * This method updates the position of the blacksheep
	 */
	@Override
	public void showBlackSheep() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to move the ovine.
			 */
			public void run() {
				int idOvine = client.getBlackSheep().getId();
				int idRegion = client.getBlackSheep().getRegion().getId();
				window.moveOvineTo(idOvine, idRegion, OvineType.BLACKSHEEP.name());
			}
		});
	}

	/**
	 * This method updates position of the wolf
	 */
	@Override
	public void showWolf() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to move the wolf.
			 */
			public void run() {
				window.moveWolfTo(client.getWolf().getRegion().getId());
			}
		});
		removeDeletedAnimals();
	}

	/**
	 * This method changes the label in loading window, explaining that it is now ready
	 */
	@Override
	public void showConnected() {
		loading.setText("Connesso");
	}

	/**
	 * This method asks the GUI to show the waiting message
	 */
	@Override
	public void showWait(final String playerName) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to show info message.
			 */
			public void run() {
				window.showMessage("Attendo la mossa del "+playerName);
			}
		});
	}

	/**
	 * This method asks GUI to show error message
	 */
	@Override
	public void showError(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to show an error message.
			 */
			public void run() {
				window.showError(message);
			}
		});
	}

	/**
	 * This method sends to GUI the selectable shepherd to activate them
	 * @param listItems
	 */
	@Override
	public void showListShepherds(final List<?> listItems) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method activates selectable shepherds in window.
			 */
			public void run() {
				for(int i=0;i<listItems.size();i++) {
					Shepherd s = (Shepherd) listItems.get(i);
					int playerNum = parsePlayerName(s.getPlayer().getName());
					window.activateShepherd(playerNum,i);
				}	
			}
		});
	}

	/**
	 * This method parses the player's name to get the numeber
	 * @param name
	 * @return number
	 */
	private int parsePlayerName(String name) {
		String num = name.substring("Giocatore ".length());
		return Integer.parseInt(num);
	}

	/**
	 * This method sends to GUI the selectable turn types to activate them
	 * @param listItems
	 */
	@Override
	public void showListTurns(final List<?> listItems) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to activate turn types.
			 */
			public void run() {
				for(int i=0;i<listItems.size();i++) {
					TurnType t = (TurnType) listItems.get(i);
					window.activateTurnType(t.name(),i);
				}
			}
		});
	}

	/**
	 * This method sends to GUI the selectable ovines to activate them
	 * @param listItems
	 */
	@Override
	public void showListOvines(final List<?> listItems) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to activate selectable ovines.
			 */
			public void run() {
				List<?> tmpList = listItems;
				for(int i=0;i<tmpList.size();i++) {
					Ovine o = (Ovine) tmpList.get(i);
					window.activateOvine(o.getId(),i);
				}	
			}
		});
	}

	/**
	 * This method sends to GUI the selectable cards to activate them
	 * @param listItems
	 */
	@Override
	public void showListCards(final List<?> listItems) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to show affordable cards.
			 */
			public void run() {
				for(int i=0;i<listItems.size();i++) {
					TerrainCard c = (TerrainCard) listItems.get(i);
					window.showNewCard(c.getTerrainType().name(),c.getCost(),i);
				}
			}
		});
	}

	/**
	 * This method asks the GUI the dialog box with cards to be bought for market.
	 * @param listItems
	 */
	@Override
	public void showListCardsMarket(final List<?> listItems) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to show affordable cards for market.
			 */
			public void run() {
				for(int i=0;i<listItems.size();i++) {
					TerrainCard c = (TerrainCard) listItems.get(i);
					window.showNewDialogCard(c.getTerrainType().name(),c.getCost());
				}
			}
		});
	}

	/**
	 * This method sends to GUI the selectable regions to activate them
	 * @param listItems
	 */
	@Override
	public void showListRegions(final List<?> listItems) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to activate selectable regions.
			 */
			public void run() {
				for(int i=0;i<listItems.size();i++) {
					Region r = (Region) listItems.get(i);
					window.activateRegion(r.getId(),i);
				}	
			}
		});
	}

	/**
	 * This method provides all the regions
	 * @return list of regions
	 */
	public List<Region> getRegions() {
		return client.getRegions();
	}

	/**
	 * This method returns the player's money
	 * @return player money
	 */
	private int getPlayerMoney() {
		return client.getPlayer().getMoney();
	}

	/**
	 * This method returns the palyer name
	 * @return player name
	 */
	private String getPlayerName() {
		return client.getPlayer().getName();
	}

	/**
	 * This method asks the GUI to show the message
	 * @param message
	 */
	@Override
	public void showMessage(final String string) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to show a message.
			 */
			public void run() {
				window.showMessage(string);
			}
		});
	}

	/**
	 * This method asks to click something.
	 * It requires blocking tasks to wait user click, then it checks if the input is valid and then it returns it.
	 * Before returning it disables all clickable elements in GUI.
	 */
	@Override
	public int askInput(String message, int min, int max) {
		String showMessage = parseInputRequest(message);
		showMessage(showMessage);
		Boolean isClicked = false;
		lastInput = SETUP_INT;
		while(!isClicked) {
			final EventWaiter waiter = new EventWaiter(this);
			sendWaiter(waiter);
			waiter.waitClick();
			if(lastInput>=min && lastInput<=max) {
				isClicked = true;
			}
		}
		disableAll();
		return lastInput;
	}

	/**
	 * This method asks the GUI to disable all clickable elements.
	 */
	private void disableAll() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to disable elements.
			 */
			public void run() {
				window.disableClicks();	
			}
		});
	}

	/**
	 * This method sends the event waiter to the GUI.
	 * @param waiter
	 */
	private void sendWaiter(final EventWaiter waiter) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				/**
				 * This method sets up waiter in the window.
				 */
				public void run() {
					window.setWaiter(waiter);
				}
			});
		} catch (InvocationTargetException e) {
			LOGGER.log(Level.WARNING, INVOK_EX,e);
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, UI_EX,e);
		}
	}

	/**
	 * This method parses the input request and translates to the final message shown to the user
	 * @param message
	 * @return showMessage
	 */
	private String parseInputRequest(String message) {
		String showMessage = null;
		if(ASK_STREET.equals(message)) {
			showMessage = "Posiziona il pastore su una strada";
			askActivateStreets();
		} else if(ASK_SHEPHERD.equals(message)) {
			showMessage = "Scegli il pastore";
		} else if(ASK_TURN.equals(message)) {
			showMessage = "Scegli la mossa";
		} else if(ASK_OVINE.equals(message)) {
			showMessage = "Scegli l'ovino";
		} else if(ASK_CARD.equals(message)) {
			showMessage = "Scegli la tessera";
		} else if(ASK_REGION.equals(message)) {
			showMessage = "Scegli la regione";
		} else if(message.startsWith(ASK_PUT)) {
			String str = message.substring(ASK_PUT.length());
			showMarketAsk(str);
			showMessage = "Fase di market - Scelta";
		} else if(ASK_PRICE.equals(message)) {
			showMarketAskPrice();
			showMessage = "Fase di market - Prezzo";
		} else if(message.startsWith(ASK_BUY)) {
			showMarketBuy();
			showMessage = "Fase di market - Vendita";
		} else {
			showMessage = message;
		}
		return showMessage;
	}

	/**
	 * This method creates the dialog box for market buy.
	 */
	private void showMarketBuy() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to activate the dialog box for market (buy).
			 */
			public void run() {
				window.activateDialogMarketBuy();
			}
		});
	}

	/**
	 * This method shows dialog box to ask the price of the card for market.
	 * @param message
	 */
	private void showMarketAskPrice() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to activate dialog for market (ask price).
			 */
			public void run() {
				window.activateDialogMarketAskPrice();
			}
		});
	}

	/**
	 * This method shows dialog box to ask if player wants to sell a card
	 * @param message
	 */
	private void showMarketAsk(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to activate the dialog for market (put).
			 */
			public void run() {
				window.activateDialogMarketAsk(message);
			}
		});
	}

	/**
	 * This method asks the GUI to activate the streets.
	 */
	private void askActivateStreets() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to activate the streets.
			 */
			public void run() {
				window.activateStreets();
				Shepherd shepherd = client.getShepherd();
				if(shepherd.getStreet()!=null) {
					List<Street> listNearStreets = shepherd.getStreet().getListOfStreets();
					for(Street s: listNearStreets) {
						window.setFreeStreet(s.getId());	
					}
				}
			}
		});
	}

	/**
	 * This method updates player's data.
	 */
	@Override
	public void showPlayer() {
		final int playerNum = parsePlayerName(getPlayerName());
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to update player's data
			 */
			public void run() {
				window.setPlayerName(getPlayerName());
				window.setPlayerIcon(playerNum);
				window.setPlayerMoney(getPlayerMoney());
			}
		});
		updateCards();
	}

	/**
	 * This method updates player's cards.
	 */
	private void updateCards() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method asks the window to update player's cards.
			 */
			public void run() {
				window.clearCards();
				List<TerrainCard> listCards = client.getPlayer().getListBoughtCards();
				for(TerrainCard t: listCards) {
					window.updateCard(t.getTerrainType().name());
				}
			}
		});
	}

	/**
	 * This method updates all animals in the GUI.
	 */
	@Override
	public void showAnimals() {
		updateAnimalPositions();
		removeDeletedAnimals();
	}

	/**
	 * This method removes all deleted animals from GUI.
	 */
	private void removeDeletedAnimals() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method removes in window deleted animals.
			 */
			public void run() {
				for(int i=0;i<window.getListJOvines().size();i++) {
					int index = window.getListJOvines().get(i).getId();
					if(client.getOvineById(index)==null) {
						window.removeOvine(index);
					}
				}
			}
		});
	}

	/**
	 * This method sends request to GUI to update animal position for every animal of the model.
	 */
	private void updateAnimalPositions() {
		SwingUtilities.invokeLater(new Runnable() {
			/** This method updates animals in window.
			 */
			public void run() {
				showWolf();	
				for(Region r: client.getRegions()) {
					for(Ovine ovine: r.getListOfOvines()) {
						String str = OvineType.SHEEP.name();
						if(ovine instanceof BlackSheep) {
							str = OvineType.BLACKSHEEP.name();
						} else if(ovine instanceof Ram) {
							str = OvineType.RAM.name();
						} else if(ovine instanceof Lamb) {
							str = OvineType.LAMB.name();
						}
						window.moveOvineTo(ovine.getId(), ovine.getRegion().getId(), str);
					}
				}
			}
		});
	}

	/**
	 * This method sets the last input recieved
	 * @param i
	 */
	public void setLastInput(int i) {
		this.lastInput = i;
	}

	/**
	 * This method shows final score.
	 */
	@Override
	public void showFinalScore(final List<?> listItems) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This method shows final scores in window.
			 */
			public void run() {
				window.showScore(listItems);
			}
		});
	}

	/**
	 * This method shows a waiting window to wait for reconnection
	 */
	@Override
	public void showWaitReconnection() {
		window.setVisible(false);
		loading = new Loading(WAIT_MESSAGE_1, WAIT_IMAGE_1);
	}

	/**
	 * This method shows again the game board after it is reconnected
	 */
	@Override
	public void showReconnected() {
		window.setVisible(true);
		loading.setVisible(false);
		loading = null;
	}

	/**
	 * This method shows that connection has definitively lost
	 */
	@Override
	public void showDisconnected() {
		loading.showError(DISCONNECTED_MESSAGE);
		loading.setVisible(false);
		loading = null;
	}
}
