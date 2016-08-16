package common;

public class Constants{
	
	private static final String[] listCommands = {"OK", "2 MIN", "NO", "CLOSE"};
	private static final String serverQuestion = "Time to eat !";

	private static final String host = "192.168.1.35"; //Adapt for your network, give a fix IP by DHCP
	private static final int port = 35150;

	public static String[] getCommands(){ return listCommands; }
	public static String getServerQuestion(){ return serverQuestion; }
	public static int getPort(){ return port; }
	public static String getHost(){ return host; }
}