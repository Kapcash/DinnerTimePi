package common;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.File;

public class Constants{
	
	private static final String[] listCommands = {"OK", "2 MIN", "NO", "CLOSE"};
	private static final String serverQuestion = "Time to eat !";

	private static final String host = "192.168.1.35"; //Adapt for your network, give a fix IP by DHCP
	private static final int port = 35150;
	/**
	 * The path to the notification sound file. Only .wav file can be played !
	 */
	private static final String notifSoundPath = "data/sounds/NotifTime.wav";

	public static String[] getCommands(){ return listCommands; }
	public static String getServerQuestion(){ return serverQuestion; }
	public static String getNotifSoundPath(){ return notifSoundPath; }
	public static int getPort(){ return port; }
	public static String getHost(){ return host; }

	public static ImageIcon getScaledImageIcon(ImageIcon srcImg, int w, int h){
    	Image image = srcImg.getImage();
		Image newimg = image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH); 
		return new ImageIcon(newimg);
	}

	/**
	 * @param soundFile .wav file to play
	 */
	public static void playSound(String pathToSoundFile){
		try{
			File soundFile = new File(pathToSoundFile);
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
}