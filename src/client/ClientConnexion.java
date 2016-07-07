package client;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.sound.sampled.*;

import javax.swing.JButton;
import javax.swing.JWindow;

public class ClientConnexion implements Runnable, ActionListener{

	private Socket connexion = null;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;

	private static final String notifSoundPath = "data/sounds/NotifTime.wav";

	private String[] listCommands = {"OK", "2MIN", "NO", "CLOSE"};
	private static int count = 0;
	private String name = "Client-";
	private JButton ok,min,no;
	private JWindow window;
	private double width, height;
	private int taskBarHeight;
	private boolean displayed;

	public ClientConnexion(String host, int port){
		name += ++count;
		displayed = false;

		/* Init notification window */
		window = new JWindow();
		ok = new JButton("OK");
		min = new JButton("2Min");
		no = new JButton("NO");
		ok.addActionListener(this);
		min.addActionListener(this);
		no.addActionListener(this);
		window.setLayout(new GridLayout(1,3));
		window.add(ok);
		window.add(min);
		window.add(no);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		taskBarHeight = (int) height - winSize.height;
		
		window.pack();
		window.setSize(new Dimension(250,50));
		window.setLocation((int)width, (int)(height-50-taskBarHeight-25));
		/* ------------------------ */
		
		try {
			connexion = new Socket(host, port);
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException !");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException !");
			//TODO : Display the error frame
			e.printStackTrace();
		}
	}

	public void run(){
		for(int i =0; i < 10; i++){
			try {

				writer = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedInputStream(connexion.getInputStream());

				//On attend la réponse
				String response = read();
				System.out.println("["+name+"] : "+response+" received");
				if(response.equals("TIME TO EAT !")){
					//TODO : play sound
					this.displayNotification();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		writer.write("CLOSE");
		writer.flush();
		writer.close();
	}

	private void displayNotification() {
		//TODO : check if notification not already displayed
		if(!displayed){
			displayed = true;
			System.out.println("Notification displayed");
			playSound();
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
		}
	}

	private void playSound(){
		try{
			File soundFile = new File(notifSoundPath);
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

	private String read() throws IOException{      
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);      
		return response;
	}
	
	private void send(String command){
		writer.write(command);
		writer.flush();
		System.out.println("Commande "+command+" envoyée au serveur");
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
		window.setVisible(false);
		displayed = false;
		window.setLocation((int)width, (int)(height-50-taskBarHeight-25));
	}
}