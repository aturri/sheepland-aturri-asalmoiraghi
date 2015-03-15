package it.polimi.sheepland.client;

import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Shepherd;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TerrainType;
import it.polimi.sheepland.model.TurnType;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * This class is the command line view for client.
 * @author Andrea
 *
 */
public class CommandLineClientView extends ClientView {
	private static final String ASK_STREET = "ASK:STREET";
	private static final String ASK_SHEPHERD = "ASK:SHEPHERD";
	private static final String ASK_TURN = "ASK:TURN";
	private static final String ASK_OVINE = "ASK:OVINE";
	private static final String ASK_CARD = "ASK:CARD";
	private static final String ASK_REGION = "ASK:REGION";
	private static final String ASK_PUT = "ASK:PUT:";
	private static final String ASK_PRICE = "ASK:PRICE";
	private static final String ASK_BUY = "ASK:BUY:";
	private static final String SEPARATOR = "----------------------------------";
	private static final String WAIT_MSG = "Tentativo di riconnessione...\n\n==> ATTENDERE <==";
	private static final String RECONNECTED_MSG = "Riconnessione riuscita!\n\n==> PROCEDERE <==";
	private static final String DISCONNECTED_MSG = "Riconnessione fallita!\n\n==> PARTITA TERMINATA <==";
	
	private Scanner scanner = new Scanner(System.in);
	private PrintStream output = System.out;
	
	/**
	 * This method shows a message
	 * @param string
	 */
	@Override
	public void showMessage(String string) {
		output.println(string);
	}

	/**
	 * This method asks user input
	 * @param message
	 * @param min
	 * @param max
	 * @return index
	 */
	@Override
	public int askInput(String message, int min, int max) {
		String showMessage = parseInputRequest(message);
		int num = -1;
		do {
			output.println(showMessage);
			if(scanner.hasNextInt()) {
				num = scanner.nextInt();
			} else {
				scanner.next();
			}
		} while(num<min||num>max);	
		return num;
	}
	
	/**
	 * This method parses the input request and translates to the final message shown to the user
	 * @param message
	 * @return showMessage
	 */	
	private String parseInputRequest(String message) {
		String showMessage = null;
		if(ASK_STREET.equals(message)) {
			showMessage = "["+client.getPlayer().getName()+"#"+client.getShepherd().getName()+"]: inserisci l'ID della strada su cui vuoi muovere il pastore:";
		} else  if(ASK_SHEPHERD.equals(message)) {
			showMessage = "["+client.getPlayer().getName()+"]: inserisci l'ID del tuo pastore:";
		} else if(ASK_TURN.equals(message)) {
			showMessage = "["+client.getPlayer().getName()+"#"+client.getShepherd().getName()+"]: inserisci il numero corrispondente alla mossa:";
		} else if(ASK_OVINE.equals(message)) {
			showMessage = "["+client.getPlayer().getName()+"#"+client.getShepherd().getName()+"]: inserisci l'ID dell'ovino:";
		} else if(ASK_CARD.equals(message)) {
			showMessage = "["+client.getPlayer().getName()+"#"+client.getShepherd().getName()+"]: inserisci l'ID della card:";
		} else if(ASK_REGION.equals(message)) {
			showMessage = "["+client.getPlayer().getName()+"#"+client.getShepherd().getName()+"]: inserisci l'ID della regione:";
		} else if(message.startsWith(ASK_PUT)) {
			String str = message.substring(ASK_PUT.length());
			String name = TerrainType.valueOf(str).getName();
			showMessage = "0: No\n1: Si\n["+client.getPlayer().getName()+"]: Vuoi mettere in vendita la tessera "+name+"?";
		} else if(ASK_PRICE.equals(message)) {
			showMessage = "["+client.getPlayer().getName()+"]: A quale prezzo?";
		} else if(message.startsWith(ASK_BUY)) {
			String str = message.substring(ASK_BUY.length());
			showMessage = str+": Non acquistare\n["+client.getShepherd().getName()+"]: Vuoi acquistare una tessera?";
		} else {
			showMessage = message;
		}
		return showMessage;
	}

	/**
	 * This method shows the wolf
	 */
	@Override
	public void showWolf() {
		output.println("Il lupo si e' spostato! "+client.getWolfString());
	}

	/**
	 * This method shows the blacksheep
	 */
	@Override
	public void showBlackSheep() {
		output.println("La pecora nera si e' spostata! "+client.getBlackSheepString());
	}

	/**
	 * This method shows the shepherd
	 */
	@Override
	public void showShepherd() {
		String string = client.getPlayer().getName()+" / "+client.getShepherd().getName();
		if(client.getShepherd().getStreet()!=null) {
			string = string+" @ Strada "+Integer.toString(client.getShepherd().getStreet().getId());
		}
		output.println(string);
	}

	/**
	 * This method shows all animals
	 */
	@Override
	public void showAnimals() {
		output.println(SEPARATOR+"\nLista Animali:\n");
		List<Region> listRegions = client.getRegions();
		output.println(client.getWolfString());			
		for(Region r: listRegions) {
			List<Ovine> listOvines = r.getListOfOvines();
			for(Ovine ovine: listOvines) {
				output.println(ovine.toString());
			}
		}
		output.println(SEPARATOR);	
	}
	
	/**
	 * This method shows the player
	 */
	@Override
	public void showPlayer() {
		output.println(SEPARATOR+"\n"+client.getPlayer().getName()+":\n");
		output.println(Integer.toString(client.getPlayer().getMoney())+" $");
		List<TerrainCard> listCards = client.getPlayer().getListBoughtCards();
		for(TerrainCard t: listCards) {
			output.println("Tessera "+t.getTerrainType().getName());
		}
		output.println(SEPARATOR);
	}
	
	/**
	 * This method displays a message for "Connection established"
	 */
	@Override
	public void showConnected() {
		output.println("Connesso al server\nPartita pronta\n\n");
	}

	/**
	 * This method shows waiting message
	 * @param playerName
	 */
	@Override
	public void showWait(String playerName) {
		output.println("Attendo la mossa di "+playerName);
	}

	/**
	 * This method shows an error message
	 * @param message
	 */
	@Override
	public void showError(String message) {
		output.println(message);

	}
	
	/**
	 * This method shows available shepherds
	 * @param listItems
	 */
	@Override
	public void showListShepherds(List<?> listItems) {
		for(int i=0;i<listItems.size();i++) {
			Shepherd s = (Shepherd) listItems.get(i);
			output.println(Integer.toString(i)+": "+s.getName());
		}
	}

	/**
	 * This method shows available turns
	 * @param listItems
	 */
	@Override
	public void showListTurns(List<?> listItems) {
		for(int i=0;i<listItems.size();i++) {
			TurnType t = (TurnType) listItems.get(i);
			output.println(Integer.toString(i)+": "+t.getName());
		}
	}

	/**
	 * This method shows available ovines
	 * @param listItems
	 */
	@Override
	public void showListOvines(List<?> listItems) {
		for(int i=0;i<listItems.size();i++) {
			Ovine o = (Ovine) listItems.get(i);
			output.println(Integer.toString(i)+": "+o.toString());
		}
	}

	/**
	 * This method returns available cards
	 * @param listItems
	 */
	@Override
	public void showListCards(List<?> listItems) {
		for(int i=0;i<listItems.size();i++) {
			TerrainCard c = (TerrainCard) listItems.get(i);
			output.println(Integer.toString(i)+": "+c.getTerrainType().getName()+" "+Integer.toString(c.getCost())+"$");
		}
	}
	
	/**
	 * This method returns available cards for market
	 * @param listItems
	 */
	@Override
	public void showListCardsMarket(List<?> listItems) {
		showListCards(listItems);
	}

	/**
	 * This method returns available regions
	 * @param listItems
	 */
	@Override
	public void showListRegions(List<?> listItems) {
		for(int i=0;i<listItems.size();i++) {
			Region r = (Region) listItems.get(i);
			output.println(Integer.toString(i)+": Regione "+Integer.toString(r.getId())+" ("+r.getName()+")");
		}
	}
	
	/**
	 * This method shows deck - not used
	 */
	@Override
	public void showDeck() {
		return;
	}

	/**
	 * This method shows final result.
	 */
	@Override
	public void showFinalScore(List<?> listItems) {
		for(Object o: listItems) {
			String string = (String) o;
			showMessage(string);
		}
	}

	/**
	 * This method shows a message to wait for reconnection
	 */
	@Override
	public void showWaitReconnection() {
		showMessage(WAIT_MSG);
	}

	/**
	 * This method shows that connection has been re-established
	 */
	@Override
	public void showReconnected() {
		showMessage(RECONNECTED_MSG);
	}

	/**
	 * This method shows that connection has definitively lost
	 */
	@Override
	public void showDisconnected() {
		showMessage(DISCONNECTED_MSG);
	}
}
