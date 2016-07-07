package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientProcessor implements Runnable{

	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	private int nb;
	private int[] gpios = {3,4,5,6};

	public ClientProcessor(Socket pSock,int number){
		sock = pSock;
		nb=number;
		try{
			Process proc_mode = Runtime.getRuntime().exec("gpio mode "+gpios[nb]+" out"); //Set the gpio to out mode
		} catch(IOException e){
			e.printStackTrace();		
		}
	}

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
				case "OK":
					System.out.print("OK");
					Thread t = new Thread(new Runnable(){
							public void run(){
								try{
									//Turn on the LED of this client (from nb)
									Process p = Runtime.getRuntime().exec("gpio write "+gpios[nb]+" 1");
									p.waitFor();
									Thread.sleep(60000); // 1 min
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
				case "2MIN":
					//TODO  Faire clignoter la LED
					System.out.print("2MIN");
					break;
				case "NO":
					//TODO
					System.out.print("NO");
					break;
				case "CLOSE":
					System.out.print("CLOSE");
					closeConnexion = true;
					break;
				default: 
					break;
				}
				System.out.println("] received from "+getName()+".");

				if(closeConnexion){
					System.err.println("Closing connexion with "+this.getName()+".");
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

	public String getName(){
		return sock.getInetAddress().getHostName();
	}
	
	public void timeToEat(){
		writer.write("[Time to eat !]");
		writer.flush();
	}
	
	//Read client answers
	private String read() throws IOException{      
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}

}
