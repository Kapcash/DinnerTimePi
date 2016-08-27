package server;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.BindException;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import static common.Constants.*;

import javax.swing.JFrame;

/**
 * @author Kapcash
 * This class is the server application. It connect all the clients and treat the sockets answers between them.
 */
public class TimeServer {
	/**
	 * Instance of class : can have only one server at a time
	 */
	static private TimeServer instance;
	//Default values
	static private String[] cmdButton = {"/bin/bash","/home/pi/Documents/DinnerTimePi/button.sh"}; //Command for listening button
	//Variables
	private ServerSocket server = null;
	private boolean isRunning = true;
	static private List<ClientProcessor> list;

	static private String host;
	static private int port;
	/**
	 * Number of second between two button push
	 */
	static final private int secondsBetweenPush = 30;
	/**
	 * How many time the server has to try to connect if doesn't success the first time
	 */
	private static final int nbOfTry = 50;

	static public TimeServer getInstance(){
		if(instance == null){
			instance = new TimeServer(host,port);
		}
		return instance;
	}

	/**
	 * @param pHost The string IP representation. Should be the current IP of the local machine on the network.
	 * @param pPort The port used by the server.
	 */
	protected TimeServer(String pHost, int pPort){
		int i=0;
		while(++i != nbOfTry && !initServer(getHost(), getPort())){
			try{
				Thread.sleep(500);
			}catch(InterruptedException intEx){
				intEx.printStackTrace();
			}
		}
		if(server == null){
			System.out.println("Server failed to init. Exiting the program.");
			System.exit(1);
		}
	}
	
	/**
	 * Initialize the server with the given parameters
	 * @param pHost The string IP representation. Should be the current IP of the local machine on the network.
	 * @param pPort The port used by the server.
	 * @return Return true if the server succeded to connect, false else
	 */
	private boolean initServer(String pHost, int pPort){
		boolean ret = true;
		list = new LinkedList<ClientProcessor>();
		host = pHost;
		port = pPort;
		try {
			server = new ServerSocket(port, 10, InetAddress.getByName(host));
		} catch (BindException bind){
			bind.printStackTrace();
			ret = false;
		} catch (UnknownHostException hoste) {
			hoste.printStackTrace();
			ret = false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			ret = false;
		}
		return ret;
	}
	
	/**
	 * Launch the server. Receive client connexions.
	 */
	public void open(){
		System.out.println("Serveur initialised at : "+host+" address and : "+port+" port.");
		//Server loop
		Thread clientThread = new Thread(new Runnable(){
			public void run(){
				while(isRunning == true){
					try {
						Socket client = server.accept();

						if(list.size() >= 4){ //Can't have more than 4 clients at same time
							System.out.println("There is already 4 clients connected, not possible adding more.");
							continue;
						}
						// Creating new client process
						ClientProcessor c = new ClientProcessor(client,list.size());
						System.out.println("Client connexion received :"+c.getName());
						list.add(c); //Add this client to the list
						Thread t = new Thread(c); //Launching client
						t.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
					server = null;
				}
			}
		});
		clientThread.start();

		//Listening for button
		Thread buttonThread = new Thread(new Runnable(){
			public void run(){
				do{
					try{
						Process p_but = Runtime.getRuntime().exec(cmdButton);
						p_but.waitFor();
						if((p_but.exitValue() == 1)){
							System.out.println(getServerQuestion());
							for(ClientProcessor c : list){
								c.timeToEat();
							}
							System.out.println("Waiting for 30 sec before push again.");
							Thread.sleep(secondsBetweenPush*1000);
						}
					}catch(IOException ioe){
						ioe.printStackTrace();
					}catch(InterruptedException interrupted){
						interrupted.printStackTrace();
					}
				}while(isRunning);
			}
		});
		buttonThread.start();
	}

	/**
	 * Close the connexion between the server and the given client
	 * @param c The client to close
	 */
	static public void closeClient(ClientProcessor c){
		System.out.println("Client "+c.getName()+" disconnected !");
		list.remove(c);
	}
	
	/**
	 * Close entirely the server. Reset all the gpios LED.
	 */
	public void close(){
		System.out.println("Closing server");
		try{
			Process p = Runtime.getRuntime().exec("gpio write "+2+" 0");
			p.waitFor();
			p = Runtime.getRuntime().exec("gpio write "+3+" 0");
			p.waitFor();
			p = Runtime.getRuntime().exec("gpio write "+4+" 0");
			p.waitFor();
			p = Runtime.getRuntime().exec("gpio write "+5+" 0");
			p.waitFor();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(InterruptedException interrupted){
			interrupted.printStackTrace();
		}
	}   
}

