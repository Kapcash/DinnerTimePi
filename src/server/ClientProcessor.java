package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import static common.Constants.*;

/**
 * @author Kapcash
 * This class is used to run the behavior of the Raspberry Pi according to the client answers (Turning on LEDs most of the time).
 */
public class ClientProcessor implements Runnable{

	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;

	/**
	 * The pin number of the current client (from 0 to 3)
	 */
	private int nb;
	/**
	 * Pin numbers used for client LEDS
	 */
	static private int[] gpios = {3,4,5,6}; 
	/**
	 * Number of seconds the LED has to be ON.
	 */
	static private String timeOn = "60";
	/**
	 * Path to the command for flashing a LED
	 */
	private String cmdFlash[] = {"/bin/bash","/home/pi/Documents/DinnerTimePi/flashbutton.sh",""+gpios[nb],timeOn};

	/**
	 * @param pSock The socket connected to the client
	 * @param number The client number, between 1 and 4
	 */
	public ClientProcessor(Socket pSock,int number){
		sock = pSock;
		nb=number;
		try{
			Process proc_mode = Runtime.getRuntime().exec("gpio mode "+gpios[nb]+" out"); //Set the gpio to out mode
		} catch(IOException e){
			e.printStackTrace();		
		}
	}

	/**
	 * Launch the client process which execute the client answer on the LEDs.
	 */
	public void run(){
		boolean closeConnexion = false;
		while(!sock.isClosed()){
			try {
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());

				String response = read();
				InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();
				System.out.print("[");
				switch(response.toUpperCase()){
				case getCommands()[0]:
					System.out.print(getCommands()[1]);
					Thread t = new Thread(new Runnable(){
							public void run(){
								try{
									//Turn on the LED of this client (from nb)
									Process p = Runtime.getRuntime().exec("gpio write "+gpios[nb]+" 1");
									p.waitFor();
									Thread.sleep(timeOn*1000); // 1 min
									// Turn off LED
									p = Runtime.getRuntime().exec("gpio write "+gpios[nb]+" 0");
									p.waitFor();
								} catch(InterruptedException e){
									e.printStackTrace();
								}catch(IOException e){
									e.printStackTrace();
								}
							}
						});
					t.start();
					break;
				case getCommands()[1]:
					Process proc_mode = Runtime.getRuntime().exec(cmdFlash);
					System.out.print(getCommands()[1]);
					break;
				case getCommands()[2]:
					//TODO
					System.out.print(getCommands()[2]);
					break;
				case getCommands()[3]:
					System.out.print(getCommands()[3]);
					closeConnexion = true;
					break;
				default: 
					break;
				}
				System.out.println("] received from "+getName()+".");

				// Close the connexion between the server and the client
				if(closeConnexion){
					System.err.println("Closing connexion with "+getName()+".");
					TimeServer.closeClient(this);
					writer = null;
					reader = null;
					sock.close();
					break;
				}
			}catch(SocketException e){
				TimeServer.closeClient(this);
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}         
		}
	}

	/**
	 * @return Return the name of the client
	 */
	public String getName(){
		return sock.getInetAddress().getHostName();
	}
	
	/**
	 * Send the server question to this client
	 */
	public void timeToEat(){
		writer.write(getServerQuestion());
		writer.flush();
	}
	
	/**
	 * Read client answers
	 * @return Return the response of the server
	 */
	private String read() throws IOException{      
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}

}
