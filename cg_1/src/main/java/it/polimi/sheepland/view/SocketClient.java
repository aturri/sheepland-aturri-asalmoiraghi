/**
 * @author Andrea Salmoiraghi
 * 
 * Ingegneria del Software 2013-2014
 * 
 * 10/giu/2014
 *
 */

package it.polimi.sheepland.view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andrea
 */
public class SocketClient extends Client {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	/**
	 * This is the constructor of the class.
	 * Set the socket and initialize it.
	 * @param client
	 */
	public SocketClient(Socket client) {
		this.socket = client;
		initializeSocket();
	}

	/**
	 * This method set the socket with the new stream and notify the changement
	 * @param the new socket client
	 */
	public void setSocket(SocketClient client) {
		closeSocket();
		setNewSocket(client);
		sendMessage("Ti sei riconnesso");
		notifyChangement();
	}
	
	/**
	 * This method set the new socket, input, output get from the new socket client
	 * @param the new socket client
	 */
	private void setNewSocket(SocketClient socketClient) {
		this.socket = socketClient.getSocket();
		input = socketClient.getInput();
		output = socketClient.getOutput();
	}

	@Override
	protected void sendMessage(String string) {
		try {
			output.writeObject(string);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Impossibile mandare il messaggio al client",e);
		}
	}

	/**
	 * This method return the socket of the client
	 * @return
	 */
	private Socket getSocket() {
		return socket;
	}

	/**
	 * This method initialize the socket
	 * @param socket
	 */ 
	private void initializeSocket() {
		try {
			createSocketObjectStream(socket);
		}catch (IOException e) {
			LOGGER.log(Level.WARNING, "Unable to initialize socket",e);
		}
	}
	
	/**
	 * This method create the input and output objects stream of the socket
	 * @param socket
	 * @throws IOException
	 */
	private void createSocketObjectStream(Socket socket) throws IOException {
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
	}
	
	/**
	 * This method close the socket
	 */
	private void closeSocket(){
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Impossibile chiudere la connesione socket",e);
		}
	}
	
	/**
	 * This method return the output of the client
	 * @return ObjectOutputStream output
	 */
	public ObjectOutputStream getOutput(){
		return output;
	}
	
	/**
	 * This method return the input of the client
	 * @return ObjectInputStream input
	 */
	public ObjectInputStream getInput(){
		return input;
	}
	
	/**
	 * This method check if the connection with the client is up.
	 * @return true if the connection is up
	 */
	@Override
	public boolean checkConnected(){
		try {
			output.writeObject("CHECK_CONNECTED");
			input.readObject();
			return true;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Impossibile contattare il client",e);
			return false;
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "Errore inatteso: ClassNotFoundException while send the token",e);
			return true;
		}
	}
	
	/**
	 * This method send the token to the client
	 */
	@Override
	public void sendToken(){
		try {
			output.writeObject("TOKEN:"+token.toString());
			checkToken((String) input.readObject());
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Impossibile inviare il token al client",e);
			disconnect();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "Errore inatteso: ClassNotFoundException mentre inviavo il token",e);
		}
	}
	
	/**
	 * This method disconnect the client
	 */
	@Override
	public void disconnect(){
		closeSocket();
	}

	/**
	 * This method get the token string directly from the client
	 * @return token string
	 */
	@Override
	public String getTokenFromClient() {
		String receivedToken = null;
		try {
			output.writeObject("GET_TOKEN");
			receivedToken = (String) input.readObject();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Impossibile ricevere il token dal client",e);
			disconnect();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "Errore inatteso: ClassNotFoundException while getting the token from client",e);
		}
		return receivedToken;
	}
}
