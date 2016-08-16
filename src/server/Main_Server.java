package server;

import server.TimeServer;
import static common.Constants.*;

public class Main_Server {

	public static void main(String[] args) {

		String host = getHost();
		int port = getPort();

		TimeServer ts = TimeServer.getInstance();
		ts.open();
	}
}

