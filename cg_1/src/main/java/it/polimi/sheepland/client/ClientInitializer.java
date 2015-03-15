/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 31/mag/2014
 *
 */

package it.polimi.sheepland.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class initialize the client. Create the communicator and the view.
 * 
 * @author Andrea
 */
public class ClientInitializer {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static final String ASK_UI = "Scegli la modalità di gioco:";
	private static final String ASK_SERVER = "Scegli la modalità di connessione al server:";
	
	private static final String DAT_FILE = "GameSaved.dat";
	
	private ClientView clientView;
	private ClientCommunicator communicator;
	private String token;
	
	/**
	 * This map contains the class of all available view
	 */
	private static final Map<Class<?>,String> USER_INTERFACES;
	static {
		Map<Class<?>,String> aMap = new LinkedHashMap<Class<?>,String>();
		aMap.put(CommandLineClientView.class, "Linea di comando");
		aMap.put(GUIClientView.class, "Interfaccia grafica");
		USER_INTERFACES = Collections.unmodifiableMap(aMap);
	}
	/**
	 * This map contains the class of all available communicators
	 */
	private static final Map<Class<?>,String> NETWORK_COMMUNICATOR;
	static {
		Map<Class<?>,String> aMap = new LinkedHashMap<Class<?>,String>();
		aMap.put(ClientSocketCommunicator.class, "Socket");
		aMap.put(ClientRMICommunicator.class, "Remote Method Invocation");
		NETWORK_COMMUNICATOR = Collections.unmodifiableMap(aMap);
	}
	
	/**
	 * This method initialize the client
	 */
	public void initialize() throws RemoteException{
		checkTokenSaved();
		setInterface();
		setCommunicator();
		clientView.setClientCommunicator(communicator);
		communicator.setClientView(clientView);
		startConnection();
	}

	/**
	 * This method load a token if a token is stored in local
	 */
	private void checkTokenSaved() {
		Scanner sc;
		try {
			sc = new Scanner(new File(DAT_FILE));
			LOGGER.log(Level.INFO, "Partita in sospeso trovata.");
			token=sc.next();
//			recreateNewFile(sc);**
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.INFO, "Nessuna partita in sospeso trovata. Ricerca di una nuova partita.",e);
		}
	}

	/**
	 * This method recreate the file GameSaved, removing the first token find.
	 */
	public void recreateNewFile(Scanner sc) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(DAT_FILE)));
			while(sc.hasNext()){
				writer.println(sc.next());
			}
			writer.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Errore inatteso durante ricreazione "+DAT_FILE,e);
		}
	}

	/**
	 * This method start the connection with the server
	 */
	private void startConnection(){
		//start the connection with the server
		this.communicator.startCommunications();
	}

	/**
	 * This method create the view chosen
	 */
	private void setInterface() {
		Selection selector = new Selection();
		int selection = selector.ask(ASK_UI, USER_INTERFACES.values());
		Class<?> selectedInterface = new ArrayList<Class<?>>(USER_INTERFACES.keySet()).get(selection);
		this.clientView = (ClientView) createObject(selectedInterface);
	}

	/**
	 * This method create the communicator chosen
	 * If a token is stored local, it set the last communicator automatically
	 */
	private void setCommunicator() {
		Selection selector = new Selection();
		int selection = selector.ask(ASK_SERVER, NETWORK_COMMUNICATOR.values());
		Class<?> selectedCommunicator = new ArrayList<Class<?>>(NETWORK_COMMUNICATOR.keySet()).get(selection);
		this.communicator = (ClientCommunicator) createObject(selectedCommunicator);
		this.communicator.setToken(token);
	}

	/**
	 * This method create a new instance of a object.
	 * 
	 * @param <T>
	 * @param class of the object
	 * @param new instance of the object
	 */
	private <T> T createObject(Class<T> selectedClass) {
		try {
			return selectedClass.newInstance();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to initialize "+selectedClass.getSimpleName(),e);
		}
		return null;
	}
}
