package client;
 
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main_Client {

	public static void main(String[] args) {

		String host = "192.168.1.35";
		int port = 35150;

/*		Thread client1 = new Thread(new ClientConnexion(host, port));
		client1.start();
*/
		new ClientConnexion(host,port).displayNotification(); //For notification test
	}
}
