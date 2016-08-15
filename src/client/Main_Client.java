package client;
 
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main_Client {

	public static void main(String[] args) {

		String host = "192.168.1.35";
		int port = 35150;

		Thread client1 = new Thread(new ClientConnexion(host, port));
		client1.start();

		/*Thread client2 = new Thread(new ClientConnexion(host, port));
		client2.start();*/
		/*Thread client3 = new Thread(new ClientConnexion(host, port));
		client3.start();
		Thread client4 = new Thread(new ClientConnexion(host, port));
		client4.start();
		Thread client5 = new Thread(new ClientConnexion(host, port));
		client5.start();*/
	}
}
