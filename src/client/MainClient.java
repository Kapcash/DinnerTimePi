package client;
 
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClient {

	public static void main(String[] args) {

		Thread client = new Thread(ClientConnexion.getInstance());
		client.start();
	}
}
