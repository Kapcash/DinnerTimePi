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

import javax.swing.JFrame;

public class TimeServer {

	private int port = 1234;
	private String host = "192.168.1.20";
	private ServerSocket server = null;
	private boolean isRunning = true;
	List<ClientProcessor> list;

	public TimeServer(){
		list = new LinkedList<ClientProcessor>();
		try {
			server = new ServerSocket(port, 10, InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TimeServer(String pHost, int pPort){
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

	public void open(){
		Thread t = new Thread(new Runnable(){
			public void run(){
				while(isRunning == true){
					try {
						Socket client = server.accept();

						System.out.println("Connexion cliente reçue.");
						ClientProcessor c = new ClientProcessor(client);
						list.add(c);
						Thread t = new Thread(c);
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

		t.start();
		
		/* -----------------------
		 * Frame only used for keyboard listening (-> push button)*/
		JFrame frame = new JFrame();
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("Key pressed");
				for(ClientProcessor c : list){
					c.timeToEat();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		frame.setSize(0, 0);
		frame.pack();
		frame.setVisible(true);
		/* ----------------------- */
		
	}

	public void close(){
		isRunning = false;
	}   
}