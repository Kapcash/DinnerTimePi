package client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.sound.sampled.*;

import static common.Constants.*;

/**
 * @author Kapcash
 * This class is the main client process, running on the client machine.
 * It displays notifications, play sound etc. It is the connexion with the server.
 */
public class ClientConnexion implements Runnable{

	// Objects
	private Socket connexion = null;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	private boolean isConnected = false;
	private MainView view;

	// Constants
	
	/**
	 * The array containing the possible client answers.
	 */
	private String[] listCommands = getCommands(); //From Constants

	/**
	 * Number of local clients connected
	 */
	private static int count = 0;
	private String name = "Client-";

	public ClientConnexion(){
		name += ++count;
		//displayed = false;

		view = new MainView(this);
	}

	public void connect(){
		if(!isConnected){
			try {
				connexion = new Socket(getHost(), getPort()); //From Constants
				if(connexion != null){
					isConnected = true;
				}
			} catch (UnknownHostException e) {
				System.out.println("UnknownHostException !");
				view.addLog("Unknown host : "+getHost(),"data/img/error.png"); 
			} catch (IOException e) {
				System.out.println("The Server is not running.");
				view.addLog("Server not running","data/img/error.png");
			}
			view.setConnectionState(isConnected);
		}
	}

	/**
	 * Launch the client connexion. Wait for the server response.
	 */
	public void run(){
		this.connect();
		while(isConnected){
			try {

				writer = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedInputStream(connexion.getInputStream());

				//Waiting for answer
				String response = read();
				System.out.println("["+name+"] : "+response+" received");

				if(response.equals(getServerQuestion())){
					view.addLog("Time to eat !","data/img/in.png");
					playSound(getNotifSoundPath());
				}
			} catch (IOException ioEx) {
				view.addLog("Server not running anymore", "data/img/error.png");
			} catch (NullPointerException nullEx){
				view.addLog("Server not launched", "data/img/error.png");
			}
		}
	}

	public boolean isConnected(){
		return isConnected;
	}

	/**
	 * Read the server response
	 * @return Return the String answer of the server.
	 */
	private String read() throws IOException{      
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);

		if(stream == -1){
			throw new IOException("Server ended !");  
		}
		response = new String(b, 0, stream);

		return response;
	}
	
	/**
	 * Send a specific answer to the server.
	 * @param command The command to send to the server, included in the 'listCommand' array.
	 */
	private void send(String command){
		writer.write(command);
		writer.flush();
		System.out.println("Command "+command+" sent to server");
	}

}
