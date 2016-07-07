package server;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

import javax.swing.JFrame;

public class TimeServer {
	//Instance of class : can have only one server at a time
	static private TimeServer instance;
	//Default values
	static private int port = 6543;
	static private String host = "192.168.1.35"; //Adapt for your network, give a fix IP by DHCP
	static private String cmd[] = {"/bin/bash","/home/pi/Documents/DinnerTimePi/src/button.sh"}; //Command for listening button
	//Variables
	private ServerSocket server = null;
	private boolean isRunning = true;
	static private List<ClientProcessor> list;
	static final private int secondsBetweenPush = 30;

	static public TimeServer getInstance(){
		if(instance == null){
			instance = new TimeServer(host,port);
		}
		return instance;
	}

	protected TimeServer(int pPort){
		list = new LinkedList<ClientProcessor>();
		port = pPort;
		try {
			server = new ServerSocket(port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected TimeServer(String pHost, int pPort){
		list = new LinkedList<ClientProcessor>();
		host = pHost;
		port = pPort;
		try {
			server = new ServerSocket(port, 10, InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Launch the server
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

		Thread buttonThread = new Thread(new Runnable(){
			public void run(){
				do{
					//Listening for button
					try{
						Process p_but = Runtime.getRuntime().exec(cmd);
						p_but.waitFor();
						if((p_but.exitValue() == 1)){
							System.out.println("[Time to eat !]");
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

	//Client disconned
	static public void closeClient(ClientProcessor c){
		System.out.println("Client "+c.getName()+" disconnected !");
		list.remove(c);
	}
	
	//Close the server
	public void close(){
		isRunning = false;
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
