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
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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
	private JLabel close,connect;
	private JWindow window;
	private int taskBarHeight;
	private TrayIcon trayIcon;

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

		 if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported on this device");
            return;
        }
        BufferedImage img = null;
        Image dinnerIcon = null;
		try {
    		dinnerIcon = ImageIO.read(new File("data/img/clock-1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        trayIcon = new TrayIcon(dinnerIcon, "DinnerTimePi");
        trayIcon.setImageAutoSize(true);
        SystemTray tray = SystemTray.getSystemTray();


        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }

		/* Init notification window */
		window = new JWindow();
		window.setMinimumSize(new Dimension(300,180));
		window.setPreferredSize(new Dimension(300,180));
		JPanel north = new JPanel();
		north.setBackground(new Color(245,245,245));
		north.setMaximumSize(new Dimension(300,30));
		JPanel south = new JPanel();
		south.setBackground(new Color(245,245,245));
		south.setMaximumSize(new Dimension(300,30));
		JPanel logs = new JPanel();
		JScrollPane scroll = new JScrollPane(logs);

		window.setLayout(new BorderLayout());

		ok = new JButton(listCommands[0]);
		min = new JButton(listCommands[1]);
		no = new JButton(listCommands[2]);
		close = new JLabel("Close");
		connect = new JLabel("Connected");

		ok.addActionListener(this);
		min.addActionListener(this);
		no.addActionListener(this);
		close.addMouseListener(this);
		trayIcon.addActionListener(this);

		north.add(connect);
		south.add(close);
		logs.add(ok);
		logs.add(min);
		logs.add(no);

		window.add(north, BorderLayout.NORTH);
		window.add(south,BorderLayout.SOUTH);
		window.add(scroll,BorderLayout.CENTER);
		window.pack();
		//window.setLocation((int)width, (int)(height-50-taskBarHeight-25));
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
		}else{
			System.out.println("[ERR] Notification already displayed");
		}
	}

	private void hideNotification(){
		if(displayed){
			System.out.println("Notification hidden");
			window.setVisible(false);
			displayed = false;
			window.setLocation((int)width, (int)(height-50-taskBarHeight-25));
		}else{
			System.out.println("[ERR] Notification already hidden");
		}
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
			hideNotification();
		}
		else if(src == min){
			send("2MIN");
			hideNotification();
		}
		else if(src == no){
			send("NO");
			hideNotification();
		}
		else if(src == trayIcon){
			displayNotification();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e){
		Object src = e.getSource();

		hideNotification();
	}
}
