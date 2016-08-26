package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.sound.sampled.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;

import org.jdesktop.swingx.border.DropShadowBorder;

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
	 * The path to the notification sound file. Only .wav file can be played !
	 */
	private static final String notifSoundPath = "data/sounds/NotifTime.wav";
	/**
	 * The array containing the possible client answers.
	 */
	private String[] listCommands = getCommands();

	/**
	 * Number of local clients connected
	 */
	private static int count = 0;
	private String name = "Client-";

	public ClientConnexion(String host, int port){
		name += ++count;
		//displayed = false;

		view = new MainView(this);

		/*
		try {
			connexion = new Socket(host, port);
			if(connexion != null){
				isConnected = true;
			}
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException !");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("The Server is not running.");
			//TODO : Display the error frame
			//e.printStackTrace();
		}*/
	}

	/**
	 * Launch the client connexion. Wait for the server response.
	 */
	public void run(){
		for(int i =0; i < 10; i++){
			try {

				writer = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedInputStream(connexion.getInputStream());

				//Waiting for answer
				String response = read();
				System.out.println("["+name+"] : "+response+" received");

				if(response.equals("[Time to eat !]")){ 
					//TODO : Change checking (if message from server change -> doens't work anymore)
					view.displayGUI();
				}
			} catch (IOException e) {
				System.out.println("The Server is not running anymore.");
				System.exit(1);
			}
		}

		writer.write("CLOSE");
		writer.flush();
		writer.close();
	}

	public boolean isConnected(){
		return isConnected;
	}

	/**
	 * @param soundFile .wav file to play
	 */
	private void playSound(File soundFile){
		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		}catch(IOException io){
			io.printStackTrace();
		}catch(LineUnavailableException line){
			line.printStackTrace();
		}catch(UnsupportedAudioFileException unsup){
			unsup.printStackTrace();
		}
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
