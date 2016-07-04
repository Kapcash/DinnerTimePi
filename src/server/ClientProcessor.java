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

	public ClientProcessor(Socket pSock){
		sock = pSock;
	}

	public void run(){
		System.err.println("Lancement du traitement de la connexion cliente");

		boolean closeConnexion = false;
		while(!sock.isClosed()){

			try {
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());

				String response = read();
				InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();

				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
				debug += " Sur le port : " + remote.getPort() + ".\n";
				debug += "-> Commande reçue : " + response + "\n";
				System.err.println("\n" + debug);

				switch(response.toUpperCase()){
				case "OK":
					//TODO
					System.out.print("OK");
					break;
				case "2MIN":
					//TODO
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
				System.out.println(" received.");

				if(closeConnexion){
					System.err.println("COMMANDE CLOSE DETECTEE ! ");
					writer = null;
					reader = null;
					sock.close();
					break;
				}
			}catch(SocketException e){
				System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}         
		}
	}
	
	public void timeToEat(){
		writer.write("TIME TO EAT");
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