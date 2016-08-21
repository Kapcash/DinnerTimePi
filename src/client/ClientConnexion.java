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

import javax.swing.JButton;
import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static common.Constants.*;


/**
 * @author Kapcash
 * This class is the main client process, running on the client machine.
 * It displays notifications, play sound etc. It is the connexion with the server.
 */
public class ClientConnexion extends MouseAdapter implements Runnable, ActionListener{

	// Objects
	private Socket connexion = null;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;

	// Constants
	
	/**
	 * The path to the notification sound file. Only .wav file can be played !
	 */
	private static final String notifSoundPath = "data/sounds/NotifTime.wav";
	/**
	 * The array containing the possible client answers.
	 */
	private String[] listCommands = getCommands();

	// GUI
	private JButton ok,min,no;
	private JLabel close;
	private JWindow window;
	private int taskBarHeight;
	
	/**
	 * Number of local clients connected
	 */
	private static int count = 0;
	private String name = "Client-";
	/**
	 * Dimensions of the screen.
	 */
	private double width, height;
	/**
	 * True if the notification is shown on the screen.
	 */
	private boolean displayed;

	public ClientConnexion(String host, int port){
		name += ++count;
		displayed = false;

		// Getting screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		taskBarHeight = (int) height - winSize.height;
	
		initNotificationGUI();

		/*
		try {
			connexion = new Socket(host, port);
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
					this.displayNotification();
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

	private void initNotificationGUI(){
		/* Init notification window */
		window = new JWindow();
		
		ok = new JButton(listCommands[0]);
		min = new JButton(listCommands[1]);
		no = new JButton(listCommands[2]);
		close = new JLabel("Close");
		ok.addActionListener(this);
		min.addActionListener(this);
		no.addActionListener(this);
		close.addMouseListener(this);

		JPanel buttonsPanel = new JPanel();
		
		buttonsPanel.setLayout(new GridLayout(1,3));
		buttonsPanel.add(ok);
		buttonsPanel.add(min);
		buttonsPanel.add(no);

		JPanel closePanel = new JPanel();
		closePanel.add(close);

		window.setLayout(new BorderLayout());
		window.add(buttonsPanel,BorderLayout.CENTER);
		window.add(closePanel,BorderLayout.SOUTH);
		window.setSize(new Dimension(250,50));
		window.pack();
		window.setLocation((int)width, (int)(height-50-taskBarHeight-25));
	}

	/**
	 * Display the  GUI notification, asking the user for an answer
	 */
	public void displayNotification() {
		if(!displayed){
			displayed = true;
			System.out.println("Notification displayed");
			playSound(new File(notifSoundPath));
			window.setVisible(true);
			//Animation
			for(int i=0;i<250;i++){
				Point loc = window.getLocation();
				window.setLocation(--loc.x, loc.y);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				window.repaint();
			}
		}else{
			System.out.println("Notification NOT displayed : already exisiting");
		}
	}

	private void hideNotification(){
		window.setVisible(false);
		displayed = false;
		window.setLocation((int)width, (int)(height-50-taskBarHeight-25));
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == ok){
			send("OK");
		}
		else if(src == min){
			send("2MIN");
		}
		else if(src == no){
			send("NO");
		}
		hideNotification();
	}

	@Override
	public void mouseClicked(MouseEvent e){
		Object src = e.getSource();

		hideNotification();
	}
}
