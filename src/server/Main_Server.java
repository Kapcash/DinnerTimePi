package server;

import server.TimeServer;

public class Main_Server {

	public static void main(String[] args) {

		String host = "192.168.1.20";
		int port = 1234;

		TimeServer ts = new TimeServer(host, port);
		ts.open();

		System.out.println("Serveur initialisé.");
	}
}
