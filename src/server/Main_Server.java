package server;

import server.TimeServer;

public class Main_Server {

	public static void main(String[] args) {

		String host = "192.168.1.35";
		int port = 35150;

		TimeServer ts = TimeServer.getInstance();
		ts.open();
	   }
}

